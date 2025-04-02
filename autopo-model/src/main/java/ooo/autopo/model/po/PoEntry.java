package ooo.autopo.model.po;

/*
 * This file is part of the Autopo project
 * Created 31/01/25
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

import com.soberlemur.potentilla.Message;
import com.soberlemur.potentilla.MessageKey;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Subscription;
import ooo.autopo.model.ai.TranslationAssessment;
import ooo.autopo.model.consistency.ConsistencyValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.sejda.commons.util.RequireUtils.requireNotNullArg;

/**
 * Represents an entry of the .po file.
 *
 * @author Andrea Vacondio
 */
public class PoEntry {

    private final MessageKey key;
    private final SimpleStringProperty untranslatedValue = new SimpleStringProperty();
    private final SimpleStringProperty translatedValue = new SimpleStringProperty();
    private final SimpleObjectProperty<TranslationAssessment> assessment = new SimpleObjectProperty<>();
    private final List<String> comments = new ArrayList<>();
    private final List<String> formats = new ArrayList<>();
    private final List<String> extractedComments = new ArrayList<>();
    private final List<String> sourceReferences = new ArrayList<>();
    private final ObservableList<String> warnings = FXCollections.observableArrayList();
    private Subscription warningsUpdaterSubscription;

    public PoEntry(Message message) {
        requireNotNullArg(message, "Message cannot be null");
        this.key = new MessageKey(message);
        this.translatedValue.set(message.getMsgstr());
        this.untranslatedValue.set(message.getMsgId());
        this.comments.addAll(message.getComments());
        this.formats.addAll(message.getFormats());
        this.extractedComments.addAll(message.getExtractedComments());
        this.sourceReferences.addAll(message.getSourceReferences());
        this.translatedValue.subscribe((o, n) -> message.setMsgstr(defaultString(n)));
    }

    public MessageKey key() {
        return key;
    }

    public ObservableValue<String> untranslatedValue() {
        return untranslatedValue;
    }

    public SimpleStringProperty translatedValue() {
        return translatedValue;
    }

    public List<String> comments() {
        return comments;
    }

    public List<String> extractedComments() {
        return extractedComments;
    }

    public List<String> sourceReferences() {
        return sourceReferences;
    }

    public List<String> formats() {
        return formats;
    }

    /**
     * Search the needle inside the translated and untranslated value
     *
     * @return true if the translated or untranslated value contains the needle (case-insensitive)
     */
    public boolean contains(String needle) {
        if (isNotBlank(needle)) {
            var searchLowerCase = needle.toLowerCase();
            return ofNullable(untranslatedValue.get()).map(String::toLowerCase).map(s -> s.contains(searchLowerCase)).orElse(false) || ofNullable(
                    translatedValue.get()).map(String::toLowerCase).map(s -> s.contains(searchLowerCase)).orElse(false);
        }
        return false;
    }

    /**
     * Notifies this entry that the target locale for this entry has changed. Consistency validators change depending on the target locale and this method takes
     * care of it.
     */
    public void notifyLocaleChange(Locale targetLocale) {
        if (Objects.nonNull(targetLocale)) {
            ofNullable(warningsUpdaterSubscription).ifPresent(Subscription::unsubscribe);
            this.warningsUpdaterSubscription = this.translatedValue.subscribe(v -> {
                warnings.clear();
                ConsistencyValidator.VALIDATORS.accept(this, targetLocale);
            });
        }
    }

    public boolean addWarning(String warning) {
        return warnings.add(warning);
    }

    public ObservableList<String> warnings() {
        return warnings;
    }

    /**
     * @return a {@link TranslationAssessment} if available
     */
    public SimpleObjectProperty<TranslationAssessment> assessment() {
        return assessment;
    }

    /**
     * If a suggested replacement is available, it updates the translated value of this {@code PoEntry} to the suggested replacement.
     */
    public void acceptSuggestion() {
        ofNullable(assessment.get()).map(TranslationAssessment::suggestedReplacement).ifPresent(this.translatedValue::set);
    }
}
