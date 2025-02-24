package ooo.autopo.service.io;

/*
 * This file is part of the Autopo project
 * Created 05/02/25
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

import com.soberlemur.potentilla.Catalog;
import com.soberlemur.potentilla.Header;
import com.soberlemur.potentilla.Message;
import com.soberlemur.potentilla.PoParser;
import com.soberlemur.potentilla.catalog.parse.ParseException;
import javafx.application.Platform;
import ooo.autopo.model.PoFile;
import ooo.autopo.model.io.FileType;
import ooo.autopo.model.io.IOEvent;
import ooo.autopo.model.io.IOEventType;
import ooo.autopo.model.project.Project;
import ooo.autopo.model.project.ProjectProperty;
import ooo.autopo.service.ai.AIService;
import org.apache.commons.lang3.StringUtils;
import org.tinylog.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.IllformedLocaleException;
import java.util.Locale;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ooo.autopo.i18n.I18nContext.i18n;
import static ooo.autopo.model.LoadingStatus.ERROR;
import static ooo.autopo.model.LoadingStatus.LOADED;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * @author Andrea Vacondio
 */
public class DefaultIOService implements IOService {

    private final AIService aiService;

    public DefaultIOService(AIService aiService) {
        this.aiService = aiService;
    }

    @Override
    public void load(PoFile poFile) throws IOException, ParseException {
        Logger.debug(i18n().tr("Loading .po file {}"), poFile.poFile().getAbsolutePath());
        try {
            Catalog catalog = new PoParser().parseCatalog(poFile.poFile());
            Locale locale = getLocale(catalog, poFile.poFile().getName());
            if (isNull(locale)) {
                Logger.warn(i18n().tr("Unable to find or detect a valid locale"));
            }
            Platform.runLater(() -> {
                poFile.catalog(catalog);
                poFile.locale(locale);
                poFile.moveStatusTo(LOADED);
            });
            eventStudio().broadcast(new IOEvent(poFile.poFile().toPath(), IOEventType.LOADED));
            Logger.info(i18n().tr("File {} loaded"), poFile.poFile().getAbsolutePath());
        } catch (IOException | ParseException e) {
            Platform.runLater(() -> poFile.moveStatusTo(ERROR));
            throw e;
        }
    }

    @Override
    public void load(Project project) throws IOException {
        var projectDescriptorPath = project.location().resolve(Path.of("autopo.ooo"));
        Logger.debug(i18n().tr("Loading project file {}"), projectDescriptorPath.toAbsolutePath().toString());
        try {
            if (Files.exists(projectDescriptorPath)) {
                try (var reader = Files.newBufferedReader(projectDescriptorPath)) {
                    project.properties().load(reader);
                }
            } else {
                Logger.debug(i18n().tr("Creating project descriptor file '{}'"), projectDescriptorPath.toAbsolutePath().toString());
                project.setProperty(ProjectProperty.NAME, RandomProjectNameGenerator.instance().getName());
                try (var stream = Files.list(project.location())) {
                    stream.filter(path -> FileType.POT.matches(path.getFileName().toString()))
                          .findFirst()
                          .ifPresent(p -> project.setProperty(ProjectProperty.TEMPLATE_PATH, p.relativize(project.location()).toString()));

                }
                project.properties().store(Files.newBufferedWriter(projectDescriptorPath), null);
            }
            Platform.runLater(() -> project.moveStatusTo(LOADED));
            eventStudio().broadcast(new IOEvent(projectDescriptorPath, IOEventType.LOADED));
            Logger.info(i18n().tr("Project {} loaded"), projectDescriptorPath.toAbsolutePath().toString());
        } catch (IOException e) {
            Platform.runLater(() -> project.moveStatusTo(ERROR));
            throw e;
        }
    }

    @Override
    public void save(Project project) throws IOException {
        var projectDescriptorPath = project.location().resolve(Path.of("autopo.ooo"));
        Logger.debug(i18n().tr("Saving project file {}"), projectDescriptorPath.toAbsolutePath().toString());
        project.properties().store(Files.newBufferedWriter(projectDescriptorPath), null);
        eventStudio().broadcast(new IOEvent(projectDescriptorPath, IOEventType.SAVED));
        Logger.info(i18n().tr("File {} saved"), projectDescriptorPath.toAbsolutePath().toString());

    }

    @Override
    public void save(PoFile poFile) {
        eventStudio().broadcast(new IOEvent(poFile.poFile().toPath(), IOEventType.SAVED));
        Logger.info(i18n().tr("File {} loaded"), poFile.poFile().getAbsolutePath());
    }

    private Locale getLocale(Catalog catalog, String filename) {
        var locale = localeFromString(ofNullable(catalog.header()).map(h -> h.getValue(Header.LANGUAGE)).orElse(null));
        if (isNull(locale)) {
            // we do what POEdit does, try looking for non-standard Qt extension
            locale = localeFromString(ofNullable(catalog.header()).map(h -> h.getValue("X-Language")).orElse(null));
            if (isNull(locale)) {
                locale = localeFromString(ofNullable(catalog.header()).map(h -> h.getValue("X-Poedit-Language")).orElse(null));
                if (isNull(locale)) {
                    Logger.debug(i18n().tr("Trying to guess locale from filename '{}'", filename));
                    locale = localeFromString(StringUtils.removeEndIgnoreCase(filename, ".po"));
                    if (isNull(locale)) {
                        Logger.debug(i18n().tr("Trying to guess locale from file content with AI"));
                        var concat = new StringBuilder();
                        for (Message message : catalog) {
                            ofNullable(message.getMsgstr()).filter(StringUtils::isNotBlank).ifPresent(concat::append);
                            if (concat.length() > 300) {
                                break;
                            }
                        }
                        locale = ofNullable(aiService.languageTagFor(concat.toString())).filter(StringUtils::isNotBlank)
                                                                                        .map(Locale::forLanguageTag)
                                                                                        .orElse(null);
                    }
                }
            }
        }
        return locale;
    }

    private Locale localeFromString(String languageHeader) {
        if (nonNull(languageHeader) && !languageHeader.isEmpty()) {
            Logger.debug(i18n().tr("Trying to guess locale from '{}'", languageHeader));
            var headerFragments = languageHeader.split("[@_]");
            if (headerFragments.length > 0) {
                try {
                    var builder = new Locale.Builder().setLanguage(headerFragments[0]);
                    if (headerFragments.length > 1) {
                        builder.setRegion(headerFragments[1]);
                        if (headerFragments.length > 2) {
                            builder.setVariant(headerFragments[2]);
                        }
                    }

                    return builder.build();
                } catch (IllformedLocaleException e) {
                    Logger.warn(i18n().tr("Invalid locale: {}", languageHeader));
                }
            }
        }
        return null;
    }
}
