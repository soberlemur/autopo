package ooo.autopo.app.config;

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

import jakarta.inject.Named;
import ooo.autopo.app.NativeOpenUrlController;
import ooo.autopo.model.AppDescriptor;
import ooo.autopo.model.ui.StageStatus;
import ooo.autopo.service.ai.AIController;
import ooo.autopo.service.ai.AIService;
import ooo.autopo.service.ai.DefaultAIService;
import ooo.autopo.service.io.DefaultIOService;
import ooo.autopo.service.io.IOController;
import ooo.autopo.service.io.IOService;
import ooo.autopo.service.project.DefaultRecentsService;
import ooo.autopo.service.project.RecentsProjectsController;
import ooo.autopo.service.project.RecentsService;
import ooo.autopo.service.ui.DefaultStageService;
import ooo.autopo.service.ui.StageService;
import ooo.autopo.service.ui.StageServiceController;
import org.pdfsam.injector.Components;
import org.pdfsam.injector.Provides;
import org.pdfsam.persistence.EntityRepository;

/**
 * @author Andrea Vacondio
 */
@Components({ IOController.class, StageServiceController.class, NativeOpenUrlController.class, RecentsProjectsController.class, AIController.class })

public class ServicesConfig {
    @Provides
    IOService io(DefaultAIService aiService, AppDescriptor descriptor) {
        return new DefaultIOService(aiService, descriptor);
    }

    @Provides
    AIService ai(DefaultAIService aiService) {
        return aiService;
    }

    @Provides
    RecentsService recents(DefaultRecentsService recentsService) {
        return recentsService;
    }

    @Provides
    public StageService stageService(@Named("stageStatusRepo") EntityRepository<StageStatus> repo) {
        return new DefaultStageService(repo);
    }

}
