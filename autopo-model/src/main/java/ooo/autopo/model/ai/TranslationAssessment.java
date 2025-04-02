package ooo.autopo.model.ai;

/*
 * This file is part of the Autopo project
 * Created 26/03/25
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

import dev.langchain4j.model.output.structured.Description;

/**
 * Model for a translation assessment that the AI will provide
 *
 * @author Andrea Vacondio
 */
public record TranslationAssessment(@Description("A score from 1 to 10, with 10 being a perfect translation") int score,
                                    @Description("A brief explanation of any errors or areas for improvement") String feedback,
                                    @Description("A suggested replacement translation that better fits the context") String suggestedReplacement) {

}
