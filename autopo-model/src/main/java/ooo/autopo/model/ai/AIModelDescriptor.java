package ooo.autopo.model.ai;

/*
 * This file is part of the Autopo project
 * Created 17/03/25
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

import dev.langchain4j.model.chat.ChatLanguageModel;
import javafx.scene.layout.Pane;

/**
 * @author Andrea Vacondio
 */
public interface AIModelDescriptor {

    String id();

    String name();

    /**
     * @return the pane where users can set options for this AI model
     */
    Pane settingsPane();

    /**
     * @return {@link ChatLanguageModel} to be used to perform translations
     */
    ChatLanguageModel translationModel();

    /**
     * @return {@link ChatLanguageModel} to be used to perform translations validation
     */
    ChatLanguageModel validationModel();

    /**
     * @return true if the model is properly configured and can be used
     */
    boolean isUsable();
}
