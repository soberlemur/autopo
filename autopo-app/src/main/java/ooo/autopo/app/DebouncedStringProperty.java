package ooo.autopo.app;
/*
 * This file is part of the Autopo project
 * Created 03/03/25
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

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Objects.nonNull;

/**
 * A SimpleStringProperty that debounces the value changes. The value is set only after a certain delay has passed since the last change. This is not thread
 * safe but it shouldn't be an issue since it's going to be used in the UI thread
 *
 * @author Andrea Vacondio
 */
public class DebouncedStringProperty extends SimpleStringProperty {

    private final int debounceDelay;
    private final TimeUnit timeUnit;
    private volatile ValueSetter valueSetter;

    public DebouncedStringProperty() {
        this(150, TimeUnit.MILLISECONDS);
    }

    public DebouncedStringProperty(int debounceDelay, TimeUnit timeUnit) {
        this.debounceDelay = debounceDelay;
        this.timeUnit = timeUnit;
    }

    @Override
    public void set(String newValue) {
        scheduleDebouncedUpdate(newValue);
    }

    private void scheduleDebouncedUpdate(String newValue) {
        // Cancel any pending debounce task
        if (nonNull(this.valueSetter)) {
            this.valueSetter.cancel();
        }
        this.valueSetter = new ValueSetter(newValue);
        Thread.ofVirtual().start(() -> {
            try {
                var currentValue = this.valueSetter;
                timeUnit.sleep(debounceDelay);
                currentValue.run();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    private class ValueSetter implements Runnable {
        private final AtomicBoolean cancelled = new AtomicBoolean(false);
        private final String value;

        public ValueSetter(String value) {
            this.value = value;
        }

        @Override
        public void run() {
            if (!cancelled.get()) {
                Platform.runLater(() -> DebouncedStringProperty.super.set(value));
            }
        }

        void cancel() {
            this.cancelled.set(true);
        }
    }

}