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

import dev.langchain4j.service.Result;
import ooo.autopo.model.ai.AIModelDescriptor;
import ooo.autopo.model.po.PoEntry;
import ooo.autopo.model.po.PoFile;

/**
 * @author Andrea Vacondio
 */
public interface AIService {

    /**
     * Given an input String in some unknown locale, it tries to return the languageTag as defined in the {@link java.util.Locale#forLanguageTag(String)}
     */
    Result<String> languageTagFor(AIModelDescriptor aiModelDescriptor, String string);

    Result<String> translate(PoFile poFile, PoEntry entry, AIModelDescriptor aiModelDescriptor, String projectDescription);
}
