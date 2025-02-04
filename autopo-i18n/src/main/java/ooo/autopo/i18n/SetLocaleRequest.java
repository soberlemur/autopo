package ooo.autopo.i18n;
/*
 * This file is part of the Autopo project
 * Created 30/01/25
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

import static org.sejda.commons.util.RequireUtils.requireNotBlank;

/**
 * Request to set a locale to a given IETF BCP 47 language tag string
 *
 * @author Andrea Vacondio
 */
public record SetLocaleRequest(String languageTag) {

    public SetLocaleRequest {
        requireNotBlank(languageTag, "Locale string cannot be blank");
    }

}
