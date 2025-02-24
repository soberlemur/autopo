package ooo.autopo.app.config;

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

import jakarta.inject.Named;
import ooo.autopo.model.ui.StageStatus;
import ooo.autopo.service.ai.AIService;
import ooo.autopo.service.ai.DefaultAIService;
import ooo.autopo.service.io.DefaultIOService;
import ooo.autopo.service.io.IOController;
import ooo.autopo.service.io.IOService;
import ooo.autopo.service.io.NativeOpenUrlController;
import ooo.autopo.service.project.DefaultRecentsService;
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
@Components({ IOController.class, StageServiceController.class, NativeOpenUrlController.class })

public class ServicesConfig {
    @Provides
    IOService io(DefaultAIService aiService) {
        return new DefaultIOService(aiService);
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
