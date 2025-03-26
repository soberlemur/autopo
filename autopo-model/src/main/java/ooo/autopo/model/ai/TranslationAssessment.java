package ooo.autopo.model.ai;

/*
 * This file is part of the Autopo project
 * Created 26/03/25
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

import dev.langchain4j.model.output.structured.Description;

/**
 * @author Andrea Vacondio
 */
public record TranslationAssessment(@Description("A score from 1 to 10, with 10 being a perfect translation") int score,
                                    @Description("A brief explanation of any errors or areas for improvement") String feedback,
                                    @Description("A suggested replacement translation that better fits the context") String suggestedReplacement) {

}
