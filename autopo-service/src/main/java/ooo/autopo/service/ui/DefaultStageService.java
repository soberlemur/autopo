package ooo.autopo.service.ui;
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

import ooo.autopo.model.ui.StageStatus;
import org.pdfsam.persistence.EntityRepository;
import org.pdfsam.persistence.PersistenceException;
import org.pdfsam.persistence.PreferencesRepository;
import org.tinylog.Logger;

import java.util.Optional;

/**
 * Default implementation of a {@link StageService} using a {@link PreferencesRepository} persistence storage.
 *
 * @author Andrea Vacondio
 */
public class DefaultStageService implements StageService {

    static final String STAGE_STATUS_KEY = "stage.status";
    private final EntityRepository<StageStatus> repo;

    public DefaultStageService(EntityRepository<StageStatus> repo) {
        this.repo = repo;
    }

    @Override
    public void save(StageStatus status) {
        try {
            this.repo.save(STAGE_STATUS_KEY, status);
        } catch (PersistenceException e) {
            Logger.error(e, "Unable to store stage status");
        }
    }

    @Override
    public Optional<StageStatus> getLatestStatus() {
        try {
            return this.repo.get(STAGE_STATUS_KEY);
        } catch (PersistenceException e) {
            Logger.error(e, "Unable to get latest stage status");
        }
        return Optional.empty();
    }

    @Override
    public void clean() {
        try {
            this.repo.clean();
        } catch (PersistenceException e) {
            Logger.error(e, "Unable to clean the persisted stage status");
        }
    }
}
