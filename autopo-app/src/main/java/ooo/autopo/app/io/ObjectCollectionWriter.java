package ooo.autopo.app.io;
/*
 * This file is part of the Autopo project
 * Created 14/02/25
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

import javafx.scene.input.ClipboardContent;
import org.tinylog.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

import static java.util.Optional.ofNullable;
import static ooo.autopo.i18n.I18nContext.i18n;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;

/**
 * Component allowing to fluently write {@link Collection} of {@link Object} content to a {@link File} or {@link ClipboardContent}.
 *
 * @author Andrea Vacondio
 */
public final class ObjectCollectionWriter implements OngoingWrite {

    private final Collection<?> content;

    private ObjectCollectionWriter(Collection<?> content) {
        this.content = content;
    }

    public static OngoingWrite writeContent(Collection<?> content) {
        return new ObjectCollectionWriter(content);
    }

    @Override
    public void to(Path file) {
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            for (Object item : content) {
                writer.append(defaultLineSeparator(item.toString()));
            }
        } catch (Exception e) {
            Logger.error(e, i18n().tr("Error saving content to file {}", ofNullable(file).map(Path::toString).orElse("")));
        }
        Logger.info(i18n().tr("File {} saved", file.toString()));
    }

    @Override
    public void to(ClipboardContent clipboard) {
        try (StringWriter writer = new StringWriter()) {
            for (Object item : content) {
                writer.append(defaultLineSeparator(item.toString()));
            }
            clipboard.putString(writer.toString());
        } catch (Exception e) {
            Logger.error(e, i18n().tr("Error saving content to clipboard"));
        }
    }

    private String defaultLineSeparator(String line) {
        return trimToEmpty(line).concat(System.lineSeparator());
    }
}
