package ooo.autopo.service.ai;

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

import dev.langchain4j.service.Result;
import ooo.autopo.model.ai.AIModelDescriptor;
import ooo.autopo.model.ai.TranslationAssessment;
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

    Result<TranslationAssessment> assess(PoFile poFile, PoEntry entry, AIModelDescriptor aiModelDescriptor, String projectDescription);
}
