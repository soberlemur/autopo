package ooo.autopo.app;

/*
 * This file is part of the Autopo project
 * Created 12/02/25
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
