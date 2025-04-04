package ooo.autopo.service.io;

/*
 * This file is part of the Autopo project
 * Created 05/02/25
 * Copyright 2025 by Sober Lemur S.r.l. (info@soberlemur.com).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import com.soberlemur.potentilla.Catalog;
import com.soberlemur.potentilla.Header;
import com.soberlemur.potentilla.Message;
import com.soberlemur.potentilla.PoParser;
import com.soberlemur.potentilla.PoWriter;
import com.soberlemur.potentilla.catalog.parse.ParseException;
import dev.langchain4j.service.Result;
import ooo.autopo.model.AppDescriptor;
import ooo.autopo.model.AppDescriptorProperty;
import ooo.autopo.model.ai.AIModelDescriptor;
import ooo.autopo.model.io.FileType;
import ooo.autopo.model.io.IOEvent;
import ooo.autopo.model.io.IOEventType;
import ooo.autopo.model.po.PoFile;
import ooo.autopo.model.po.PotFile;
import ooo.autopo.model.project.Project;
import ooo.autopo.model.project.ProjectProperty;
import ooo.autopo.service.ai.AIService;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.tinylog.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.IllformedLocaleException;
import java.util.List;
import java.util.Locale;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ooo.autopo.i18n.I18nContext.i18n;
import static ooo.autopo.model.LoadingStatus.ERROR;
import static ooo.autopo.model.LoadingStatus.INITIAL;
import static ooo.autopo.model.LoadingStatus.LOADED;
import static ooo.autopo.model.LoadingStatus.LOADING;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;
import static org.sejda.commons.util.RequireUtils.requireIOCondition;

/**
 * @author Andrea Vacondio
 */
public class DefaultIOService implements IOService {

    private final AIService aiService;
    private final AppDescriptor descriptor;

    public DefaultIOService(AIService aiService, AppDescriptor descriptor) {
        this.aiService = aiService;
        this.descriptor = descriptor;
    }

    @Override
    public void load(PoFile poFile, AIModelDescriptor descriptor) throws IOException, ParseException {
        if (poFile.status(INITIAL, LOADING)) {
            Logger.debug(i18n().tr("Loading .po file {}"), poFile.poFile().toString());
            try {
                Catalog catalog = new PoParser().parseCatalog(poFile.poFile().toFile());
                Locale locale = getLocale(catalog, poFile.poFile().getFileName().toString(), descriptor);
                if (isNull(locale)) {
                    Logger.warn(i18n().tr("Unable to find or detect a valid locale"));
                }
                poFile.catalog(catalog);
                poFile.locale(locale);
                poFile.updatePercentageOfTranslation();
                poFile.status(LOADED);
                eventStudio().broadcast(new IOEvent(poFile.poFile(), IOEventType.LOADED, FileType.PO));
                Logger.info(i18n().tr("File {} loaded"), poFile.poFile().toString());
            } catch (IOException | ParseException e) {
                poFile.status(ERROR);
                throw e;
            }
        } else {
            Logger.trace("Skipping .po file {} with status ", poFile.poFile().toString(), poFile.status());
        }
    }

    @Override
    public void updatePoFromTemplate(PoFile poFile, PotFile potFile) throws IOException {
        requireIOCondition(potFile.isLoaded(), "Template is in an invalid state");
        if (poFile.isLoaded()) {
            Logger.debug(i18n().tr("Updating po file {} from template {}"), poFile.poFile().toString(), potFile.potFile().toString());
            poFile.updateFromTemplate(potFile);
            Logger.info(i18n().tr("Po file {} updated"), poFile.poFile().toString());
            save(poFile);
        } else {
            Logger.error(i18n().tr("Cannot update file {} because it is not loaded yet"), poFile.poFile().getFileName().toString());
        }
    }

    @Override
    public void load(PotFile potFile) throws IOException, ParseException {
        if (potFile.status(INITIAL, LOADING)) {
            Logger.debug(i18n().tr("Loading .pot file {}"), potFile.potFile().toString());
            try {
                Catalog catalog = new PoParser().parseCatalog(potFile.potFile().toFile());
                potFile.catalog(catalog);
                potFile.status(LOADED);
                eventStudio().broadcast(new IOEvent(potFile.potFile(), IOEventType.LOADED, FileType.POT));
                Logger.info(i18n().tr("File {} loaded"), potFile.potFile().toString());
            } catch (IOException | ParseException e) {
                potFile.status(ERROR);
                throw e;
            }
        } else {
            Logger.trace("Skipping .pot file {} with status ", potFile.potFile().toString(), potFile.status());
        }
    }

    @Override
    public void save(PoFile poFile) throws IOException {
        Logger.debug(i18n().tr("Saving po file {}"), poFile.poFile().toAbsolutePath().toString());
        if (nonNull(poFile.catalog().header())) {
            if (!poFile.catalog().header().contains(Header.LANGUAGE) && nonNull(poFile.locale().get())) {
                poFile.catalog().header().setValue(Header.LANGUAGE, localeHeaderFromLocale(poFile.locale().get()));
            }
            poFile.catalog().header().setValue(Header.CONTENT_TYPE, "text/plain; charset=UTF-8");
            poFile.catalog()
                  .header()
                  .setValue("X-Generator", descriptor.property(AppDescriptorProperty.NAME) + " v" + descriptor.property(AppDescriptorProperty.VERSION));
            poFile.catalog().header().updateRevisionDate();
        }
        new PoWriter().withAddHeaderIfMissing().withCharset(StandardCharsets.UTF_8).write(poFile.catalog(), Files.newBufferedWriter(poFile.poFile()));
        poFile.updatePercentageOfTranslation();
        eventStudio().broadcast(new IOEvent(poFile.poFile(), IOEventType.SAVED, FileType.PO));
        Logger.info(i18n().tr("File {} saved"), poFile.poFile().toString());
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
                ofNullable(project.getProperty(ProjectProperty.TEMPLATE_PATH)).map(Paths::get).filter(Files::exists).ifPresent(project::pot);
            } else {
                Logger.debug(i18n().tr("Creating project descriptor file '{}'"), projectDescriptorPath.toAbsolutePath().toString());
                project.setProperty(ProjectProperty.NAME, RandomProjectNameGenerator.instance().getName());
                project.setProperty(ProjectProperty.EXCLUDE, "target");
                project.properties().store(Files.newBufferedWriter(projectDescriptorPath), null);
            }

            Files.walkFileTree(project.location(), new SimpleFileVisitor<>() {
                private final List<Path> excludes = ofNullable(project.getProperty(ProjectProperty.EXCLUDE)).stream()
                                                                                                            .flatMap(v -> Arrays.stream(v.split("\\|")))
                                                                                                            .map(project.location()::resolve)
                                                                                                            .map(Path::normalize)
                                                                                                            .toList();

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (FileType.PO.matches(file.getFileName().toString())) {
                        project.addTranslation(new PoFile(file));
                        Logger.trace("Found .po file '{}'", file.toString());
                    }
                    if (FileType.POT.matches(file.getFileName().toString()) && isNull(project.pot().get())) {
                        project.pot(file);
                        Logger.debug(i18n().tr("Found template file '{}'"), project.getProperty(ProjectProperty.TEMPLATE_PATH));
                    }

                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    if (excludes.contains(dir.normalize())) {
                        Logger.trace(i18n().tr("Skipping directory '{}'"), dir.toString());
                        return FileVisitResult.SKIP_SUBTREE;
                    }
                    return FileVisitResult.CONTINUE;
                }
            });

            project.status(LOADED);
            eventStudio().broadcast(new IOEvent(project.location(), IOEventType.LOADED, FileType.OOO));
            Logger.info(i18n().tr("Project {} loaded"), projectDescriptorPath.toAbsolutePath().toString());
        } catch (IOException e) {
            project.status(ERROR);
            throw e;
        }
    }

    @Override
    public void save(Project project) throws IOException {
        var projectDescriptorPath = project.location().resolve(Path.of("autopo.ooo"));
        Logger.debug(i18n().tr("Saving project file {}"), projectDescriptorPath.toAbsolutePath().toString());
        project.properties().store(Files.newBufferedWriter(projectDescriptorPath), null);
        eventStudio().broadcast(new IOEvent(projectDescriptorPath, IOEventType.SAVED, FileType.OOO));
        Logger.info(i18n().tr("File {} saved"), projectDescriptorPath.toAbsolutePath().toString());

    }

    private Locale getLocale(Catalog catalog, String filename, AIModelDescriptor descriptor) {
        var locale = localeFromHeader(ofNullable(catalog.header()).map(h -> h.getValue(Header.LANGUAGE)).orElse(null));
        if (isNull(locale)) {
            // we do what POEdit does, try looking for non-standard Qt extension
            locale = localeFromHeader(ofNullable(catalog.header()).map(h -> h.getValue("X-Language")).orElse(null));
            if (isNull(locale)) {
                locale = localeFromHeader(ofNullable(catalog.header()).map(h -> h.getValue("X-Poedit-Language")).orElse(null));
                if (isNull(locale)) {
                    Logger.debug(i18n().tr("Trying to guess locale from filename '{}'"), filename);
                    locale = localeFromTag(StringUtils.removeEndIgnoreCase(filename, ".po"));
                    if (isNull(locale) && nonNull(descriptor)) {
                        Logger.debug(i18n().tr("Trying to guess locale from file content with AI"));
                        var concat = new StringBuilder();
                        for (Message message : catalog) {
                            ofNullable(message.getMsgstr()).filter(StringUtils::isNotBlank).map(s -> "\"" + s + "\" ").ifPresent(concat::append);
                            if (concat.length() > 300) {
                                break;
                            }
                        }
                        locale = ofNullable(aiService.languageTagFor(descriptor, concat.toString())).map(Result::content).map(this::localeFromTag).orElse(null);
                    }
                }
            }
        }
        return locale;
    }

    private Locale localeFromHeader(String languageHeader) {
        if (isNotBlank(languageHeader)) {
            Logger.debug(i18n().tr("Trying to guess locale from '{}'"), languageHeader);
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
                    Logger.warn(i18n().tr("Invalid locale: {}"), languageHeader);
                }
            }
        }
        return null;
    }

    private Locale localeFromTag(String tag) {
        if (isNotBlank(tag)) {
            Logger.debug(i18n().tr("Trying to guess locale from '{}'"), tag);
            var locale = Locale.forLanguageTag(tag);
            if (LocaleUtils.isAvailableLocale(locale)) {
                return locale;
            }
        }
        return null;
    }

    private String localeHeaderFromLocale(Locale locale) {
        String languageTag = locale.getLanguage();

        if (isNotBlank(locale.getCountry())) {
            languageTag += "_" + locale.getCountry().toUpperCase();
        }

        if (isNotBlank(locale.getVariant())) {
            languageTag += "@" + locale.getVariant().toLowerCase();
        }
        return languageTag;
    }
}
