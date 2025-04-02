package ooo.autopo.service.ai;

/*
 * This file is part of the Autopo project
 * Created 25/03/25
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
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * @author Andrea Vacondio
 */
public interface LocaleDetectionServiceAI {

    @SystemMessage("You are a language identification expert with deep knowledge of linguistic patterns, scripts, and language codes. Given a set of input strings in an unidentified language, return the most appropriate IETF BCP 47 language tag that best represents the detected language. Consider script variations, regional distinctions, and standardization when selecting the tag. If multiple tags are possible, return the most widely accepted or commonly used variant. Your answer will be automatically processed therefore you need to return the language tag only and nothing more, no comments, no additional quotes, trailing or leading spaces, or full stop just the language tag. Example input: ['Bonjour tout le monde', 'Ceci est un test']. Expected output: fr")
    @UserMessage("These are the input strings: {{text}}")
    Result<String> languageTagFor(@V("text") String text);
}
