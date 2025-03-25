package ooo.autopo.service.ai;

/*
 * This file is part of the Autopo project
 * Created 20/03/25
 * Copyright 2025 by Sober Lemur S.r.l. (info@soberlemur.com).
 *
 * You are not permitted to distribute it in any form unless explicit
 * consent is given by Sober Lemur S.r.l..
 * You are not permitted to modify it.
 *
 * Autopo is distributed WITHOUT ANY WARRANTY;
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.service.Result;
import jakarta.inject.Inject;
import javafx.application.Platform;
import ooo.autopo.model.ai.AIModelDescriptor;
import ooo.autopo.model.ai.TranslationRequest;
import ooo.autopo.model.po.PoEntry;
import ooo.autopo.model.po.PoFile;
import ooo.autopo.model.ui.SetStatusLabelRequest;
import ooo.autopo.service.ServiceExceptionHandler;
import org.pdfsam.eventstudio.annotation.EventListener;
import org.pdfsam.injector.Auto;
import org.tinylog.Logger;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.StructuredTaskScope;
import java.util.function.Function;
import java.util.function.Supplier;

import static ooo.autopo.i18n.I18nContext.i18n;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;
import static org.sejda.commons.util.RequireUtils.requireArg;
import static org.sejda.commons.util.RequireUtils.requireNotNullArg;
import static org.sejda.commons.util.RequireUtils.requireState;

/**
 * @author Andrea Vacondio
 */
@Auto
public class AIController {

    private final ServiceExceptionHandler exceptionHandler;
    private final AIService aiService;

    @Inject
    public AIController(AIService aiService, ServiceExceptionHandler exceptionHandler) {
        this.aiService = aiService;
        this.exceptionHandler = exceptionHandler;
        eventStudio().addAnnotatedListeners(this);
    }

    @EventListener
    public void translate(TranslationRequest request) {
        Logger.trace("AI translation request received");
        requireNotNullArg(request.poFile(), "Cannot translate a null poFile");
        requireNotNullArg(request.poEntries(), "Cannot translate a null poEntry");
        requireArg(!request.poEntries().isEmpty(), "Cannot translate an empty poEntry collection");
        requireNotNullArg(request.descriptor(), "Cannot translate with a null descriptor");
        if (request.poEntries().size() == 1) {
            translateSingle(request);
        } else {
            translateMultiple(request);
        }

    }

    private void translateMultiple(TranslationRequest request) {
        Thread.ofVirtual().name("translation-thread-", 0).start(() -> {

            try (var scope = new StructuredTaskScope()) {
                var callables = request.poEntries()
                                       .stream()
                                       .map(poEntry -> (Callable<Result<String>>) () -> translateEntry(request.poFile(),
                                                                                                       poEntry,
                                                                                                       request.descriptor(),
                                                                                                       request.projectDescription()))
                                       .toList();
                List<? extends Supplier<Result<String>>> suppliers = callables.stream()
                                                                              .map((Function<? super Callable<Result<String>>, ? extends StructuredTaskScope.Subtask<Result<String>>>) scope::fork)
                                                                              .toList();
                scope.join();
                var totalTokens = suppliers.stream().map(Supplier::get).map(result -> result.tokenUsage().totalTokenCount()).reduce(0, Integer::sum);
                eventStudio().broadcast(new SetStatusLabelRequest(i18n().tr("Total token used {0})", totalTokens.toString())));
            } catch (Throwable e) {
                exceptionHandler.accept(e, i18n().tr("An error occurred while translating with AI"));
            }
            Platform.runLater(() -> request.complete().set(true));

        });
    }

    private void translateSingle(TranslationRequest request) {
        Thread.ofVirtual().name("translation-thread-", 0).start(() -> {
            try {
                var result = translateEntry(request.poFile(), request.poEntries().getFirst(), request.descriptor(), request.projectDescription());
                eventStudio().broadcast(new SetStatusLabelRequest(i18n().tr("Total token used {0} (input: {1}, output: {2})",
                                                                            result.tokenUsage().totalTokenCount().toString(),
                                                                            result.tokenUsage().inputTokenCount().toString(),
                                                                            result.tokenUsage().outputTokenCount().toString())));
            } catch (Throwable e) {
                exceptionHandler.accept(e, i18n().tr("An error occurred while translating with AI"));
            }
            Platform.runLater(() -> request.complete().set(true));
        });
    }

    private Result<String> translateEntry(PoFile poFile, PoEntry poEntry, AIModelDescriptor descriptor, String projectDescription) {
        requireNotNullArg(poEntry, "Cannot translate a null poEntry");
        var result = aiService.translate(poFile, poEntry, descriptor, projectDescription);
        requireState(result.finishReason() == FinishReason.STOP, i18n().tr("Invalid finish reason: {0}", result.finishReason().name()));
        Platform.runLater(() -> {
            poEntry.translatedValue().set(result.content().trim());
            poFile.modified(true);
        });
        return result;
    }
}
