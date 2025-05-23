package ooo.autopo.app;

/*
 * This file is part of the Autopo project
 * Created 12/02/25
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

import javafx.application.Application;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Andrea Vacondio
 */
public class App {
    public static void main(String[] args) {
        if (Arrays.stream(args).filter(Objects::nonNull).map(String::toLowerCase)
                  .anyMatch(s -> "--verbose".equals(s) || "-verbose".equals(s) || "-v".equals(s))) {
            System.setProperty("tinylog.configuration", "tinylog-verbose.properties");
        }

        Application.launch(AutopoApp.class, args);
    }
}
