package ooo.autopo.model.po;

/*
 * This file is part of the Autopo project
 * Created 31/01/25
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

import com.soberlemur.potentilla.Message;
import com.soberlemur.potentilla.MessageKey;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Subscription;
import ooo.autopo.model.consistency.ConsistencyValidator;
import org.sejda.commons.util.RequireUtils;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import static javafx.util.Subscription.combine;

/**
 * @author Andrea Vacondio
 */
public class PoEntry {

    private final SimpleBooleanProperty modified = new SimpleBooleanProperty();
    private final MessageKey key;
    private final Message message;
    private final SimpleStringProperty value;
    private final ObservableList<String> comments = FXCollections.observableArrayList();
    private final ObservableList<String> warnings = FXCollections.observableArrayList();
    private Subscription warningsUpdaterSubscription;

    public PoEntry(Message message) {
        RequireUtils.requireNotNullArg(message, "Message cannot be null");
        this.key = new MessageKey(message);
        this.message = message;
        this.value = new SimpleStringProperty(message.getMsgstr());
        this.comments.addAll(message.getComments());
        var compositeSubscription = combine(this.value.subscribe((o, n) -> modified.set(true)), this.comments.subscribe(() -> modified.set(true)));
        var oneTime = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    compositeSubscription.unsubscribe();
                    modified.removeListener(this);
                }
            }
        };
        modified.addListener(oneTime);
        this.value.subscribe((o, n) -> message.setMsgstr(n));
        //TODO listener for comments updating the message comments
    }

    public MessageKey key() {
        return key;
    }

    public ObservableValue<String> untranslatedValue() {
        return new SimpleStringProperty(message.getMsgId());
    }

    public SimpleStringProperty translatedValue() {
        return value;
    }

    public ObservableList<String> comments() {
        return comments;
    }

    public ObservableBooleanValue modifiedProperty() {
        return modified;
    }

    /**
     * Notifies this entry that the target locale for this entry has changed. Consistency validators change depending on the target locale and this method takes
     * care of it.
     */
    public void onLocaleUpdate(Locale targetLocale) {
        if (Objects.nonNull(targetLocale)) {
            Optional.ofNullable(warningsUpdaterSubscription).ifPresent(Subscription::unsubscribe);
            this.warningsUpdaterSubscription = this.value.subscribe(v -> {
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
}
