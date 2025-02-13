package ooo.autopo.service.ai;

/*
 * This file is part of the Autopo project
 * Created 13/02/25
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

/**
 * @author Andrea Vacondio
 */
public interface AIService {

    /**
     * Given an input String in some unknown locale, it tried to return the languageTag as defined in the {@link java.util.Locale#forLanguageTag(String)}
     */
    String languageTagFor(String string);
}
