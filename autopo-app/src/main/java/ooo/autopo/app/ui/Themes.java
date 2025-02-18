package ooo.autopo.app.ui;

/*
 * This file is part of the Autopo project
 * Created 17/02/25
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

import ooo.autopo.theme.Theme;
import org.apache.commons.lang3.SystemUtils;

import java.util.Collections;
import java.util.ServiceLoader;
import java.util.SortedMap;
import java.util.TreeMap;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.sejda.commons.util.RequireUtils.require;

/**
 * @author Andrea Vacondio
 */
public class Themes {
    private static final TreeMap<String, Theme> THEMES = ServiceLoader.load(Theme.class)
                                                                      .stream()
                                                                      .map(ServiceLoader.Provider::get)
                                                                      .collect(toMap(Theme::id, identity(), (a, b) -> a, TreeMap::new));

    /**
     * @param id the theme id
     * @return the theme with the given id or a default theme if no theme with the given id is found
     * @throws IllegalStateException if no theme is available
     */
    public static Theme getOrDefault(String id) {
        return ofNullable(get(id)).orElseGet(Themes::defaultTheme);
    }

    /**
     * @param id the theme id
     * @return the theme with the given id or null
     */
    public static Theme get(String id) {
        if (nonNull(id)) {
            return THEMES.get(id);
        }
        return null;
    }

    //TODO replace with some logic to at least detect light/dark theme and provide a sensible default
    private static Theme defaultTheme() {
        require(!THEMES.isEmpty(), () -> new IllegalStateException("No theme available"));
        if (isDarkTheme()) {
            for (Theme theme : THEMES.values()) {
                if (theme.isDark()) {
                    return theme;
                }
            }
        }
        for (Theme theme : THEMES.values()) {
            if (!theme.isDark()) {
                return theme;
            }
        }
        return THEMES.get(THEMES.firstKey());
    }

    private static boolean isDarkTheme() {
        if (SystemUtils.IS_OS_WINDOWS) {

        }
        if (SystemUtils.IS_OS_MAC) {

        }
        return false;
    }

    /**
     * @return an unmodifiable sorted view of the themes available for the application
     */
    public static SortedMap<String, Theme> themes() {
        return Collections.unmodifiableSortedMap(THEMES);
    }
}
