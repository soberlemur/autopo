package ooo.autopo.model.ai;

/*
 * This file is part of the Autopo project
 * Created 17/03/25
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
