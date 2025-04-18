package ooo.autopo.app.ui.logs;
/*
 * This file is part of the Autopo project
 * Created 13/02/25
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

import org.tinylog.Level;
import org.tinylog.core.LogEntry;
import org.tinylog.core.LogEntryValue;
import org.tinylog.writers.AbstractFormatPatternWriter;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Scanner;

import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * Writer that broadcasts log entries using event studio so that a UI component can listen to them and show them in lost view or similar
 *
 * @author Andrea Vacondio
 */
public class BroadcastingWriter extends AbstractFormatPatternWriter {

    public BroadcastingWriter() {
        this(Collections.emptyMap());
    }

    /**
     * @param properties Configuration for writer
     */
    public BroadcastingWriter(Map<String, String> properties) {
        super(properties);
    }

    @Override
    public Collection<LogEntryValue> getRequiredLogEntryValues() {
        Collection<LogEntryValue> logEntryValues = super.getRequiredLogEntryValues();
        logEntryValues.add(LogEntryValue.LEVEL);
        return logEntryValues;
    }

    @Override
    public void write(final LogEntry logEntry) {
        try (Scanner scanner = new Scanner(render(logEntry))) {
            while (scanner.hasNextLine()) {
                eventStudio().broadcast(new LogMessage(scanner.nextLine(), LogLevel.toLogLevel(logEntry.getLevel())));
            }
        }
        if (logEntry.getLevel().ordinal() == Level.ERROR.ordinal()) {
            eventStudio().broadcast(new ErrorLoggedEvent());
        }
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() {
    }
}