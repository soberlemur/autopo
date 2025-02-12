package ooo.autopo.service;

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
import com.soberlemur.potentilla.PoParser;
import com.soberlemur.potentilla.catalog.parse.ParseException;
import javafx.application.Platform;
import ooo.autopo.model.PoFile;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.IllformedLocaleException;
import java.util.Locale;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ooo.autopo.i18n.I18nContext.i18n;
import static ooo.autopo.model.LoadingStatus.ERROR;
import static ooo.autopo.model.LoadingStatus.LOADED;
import static ooo.autopo.model.LoadingStatus.LOADING;
import static org.sejda.commons.util.RequireUtils.requireNotNullArg;

/**
 * @author Andrea Vacondio
 */
public class DefaultIOService implements IOService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultIOService.class);

    @Override
    public void load(PoFile poFile) {
        requireNotNullArg(poFile, "Cannot load a null poFile");
        LOG.debug(i18n().tr("Loading .po file {}", poFile.poFile().getAbsolutePath()));
        Platform.runLater(() -> poFile.moveStatusTo(LOADING));
        try {
            Catalog catalog = new PoParser().parseCatalog(poFile.poFile());
            Locale locale = getLocale(catalog, poFile.poFile().getName());
            if (isNull(locale)) {
                LOG.warn(i18n().tr("Unable to find or detect a valid locale"));
            }
            Platform.runLater(() -> {
                poFile.catalog(catalog);
                poFile.locale(locale);
                poFile.moveStatusTo(LOADED);
            });
            LOG.info(i18n().tr("File {} loaded", poFile.poFile().getAbsolutePath()));
        } catch (IOException | ParseException e) {
            LOG.error(i18n().tr("An error occurred parsing the .po file '{}'"), poFile.poFile().getAbsolutePath(), e);
            Platform.runLater(() -> poFile.moveStatusTo(ERROR));
        }
    }

    @Override
    public void save(PoFile poFile) throws IOException {

    }

    private Locale getLocale(Catalog catalog, String filename) {
        var locale = localeFromString(ofNullable(catalog.header()).map(h -> h.getValue(Header.LANGUAGE)).orElse(null));
        if (isNull(locale)) {
            // we do what POEdit does, try looking for non-standard Qt extension
            locale = localeFromString(ofNullable(catalog.header()).map(h -> h.getValue("X-Language")).orElse(null));
            if (isNull(locale)) {
                locale = localeFromString(ofNullable(catalog.header()).map(h -> h.getValue("X-Poedit-Language")).orElse(null));
                if (isNull(locale)) {
                    LOG.debug(i18n().tr("Trying to guess locale from filename '{}'", filename));
                    locale = localeFromString(StringUtils.removeEndIgnoreCase(filename, ".po"));
                }
            }

        }
        return locale;
    }

    private Locale localeFromString(String languageHeader) {
        if (nonNull(languageHeader) && !languageHeader.isEmpty()) {
            LOG.debug(i18n().tr("Trying to guess locale from '{}'", languageHeader));
            var headerFragments = languageHeader.split("[@_]");
            if (headerFragments.length > 0) {
                try {
                    var builder = new Locale.Builder().setLanguageTag(headerFragments[0]);
                    if (headerFragments.length > 1) {
                        builder.setRegion(headerFragments[1]);
                        if (headerFragments.length > 2) {
                            builder.setVariant(headerFragments[2]);
                        }
                    }

                    return builder.build();
                } catch (IllformedLocaleException e) {
                    LOG.warn(i18n().tr("Invalid locale: {}", languageHeader));
                }
            }
        }
        return null;
    }
}
