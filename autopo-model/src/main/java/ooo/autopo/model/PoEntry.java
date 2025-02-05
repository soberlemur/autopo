package ooo.autopo.model;

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

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ooo.autopo.model.consistency.ConsistencyValidator;
import org.sejda.commons.util.RequireUtils;

import java.util.Locale;

import static javafx.util.Subscription.combine;

/**
 * @author Andrea Vacondio
 */
public class PoEntry {

    private SimpleBooleanProperty modified = new SimpleBooleanProperty();
    private final ReadOnlyStringProperty key;
    private final SimpleStringProperty value;
    private final SimpleStringProperty comment;
    private ObservableList<String> warnings = FXCollections.observableArrayList();

    public PoEntry(String key, String value, String comment, Locale targetLocale) {
        RequireUtils.requireNotBlank(key, "Key cannot be blank");
        this.key = new SimpleStringProperty(key);
        this.value = new SimpleStringProperty(value);
        this.comment = new SimpleStringProperty(comment);
        var compositeSubscription = combine(this.value.subscribe((o, n) -> modified.set(true)), this.comment.subscribe((o, n) -> modified.set(true)));
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
        modified.subscribe(o -> updateWarnings(targetLocale));
    }

    public ObservableValue<String> key() {
        return key;
    }

    public SimpleStringProperty value() {
        return value;
    }

    public SimpleStringProperty comment() {
        return comment;
    }

    public ObservableBooleanValue modifiedProperty() {
        return modified;
    }

    private void updateWarnings(Locale targetLocale) {
        warnings.clear();
        ConsistencyValidator.VALIDATORS.accept(this, targetLocale);
    }

    public void clearWarnings() {
        warnings.clear();
    }

    public boolean addWarning(String warning) {
        return warnings.add(warning);
    }

    public ObservableList<String> warnings() {
        return warnings;
    }
}
