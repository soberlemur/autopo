package ooo.autopo.ai.gemini;

/*
 * This file is part of the Autopo project
 * Created 18/03/25
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
public enum GeminiAIPersistentProperty {
    API_KEY,
    MODEL_NAME;

    public String key() {
        return this.name().toLowerCase();
    }
}