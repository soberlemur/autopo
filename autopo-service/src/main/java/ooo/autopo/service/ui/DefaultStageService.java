package ooo.autopo.service.ui;
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
