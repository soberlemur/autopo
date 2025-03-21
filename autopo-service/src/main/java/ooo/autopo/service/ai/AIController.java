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
import jakarta.inject.Inject;
import javafx.application.Platform;
import ooo.autopo.model.ai.TranslationRequest;
import ooo.autopo.model.ui.SetStatusLabelRequest;
import ooo.autopo.service.ServiceExceptionHandler;
import org.pdfsam.eventstudio.annotation.EventListener;
import org.pdfsam.injector.Auto;
import org.tinylog.Logger;

import static ooo.autopo.i18n.I18nContext.i18n;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;
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
        requireNotNullArg(request.poEntry(), "Cannot translate a null poEntry");
        requireNotNullArg(request.descriptor(), "Cannot translate with a null descriptor");
        Thread.ofVirtual().name("translation-thread-", 0).start(() -> {
            try {
                var result = aiService.translate(request.poFile(), request.poEntry(), request.descriptor(), request.projectDescription());
                requireState(result.finishReason() == FinishReason.STOP, i18n().tr("Invalid finish reason: {0}", result.finishReason().name()));
                Platform.runLater(() -> request.poEntry().translatedValue().set(result.content().trim()));
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
}
