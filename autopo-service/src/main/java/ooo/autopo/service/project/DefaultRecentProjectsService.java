package ooo.autopo.service.project;

/*
 * This file is part of the Autopo project
 * Created 24/02/25
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

import jakarta.inject.Inject;
import jakarta.inject.Named;
import ooo.autopo.model.lifecycle.ShutdownEvent;
import org.apache.commons.lang3.StringUtils;
import org.pdfsam.eventstudio.annotation.EventListener;
import org.pdfsam.persistence.PersistenceException;
import org.pdfsam.persistence.PreferencesRepository;
import org.sejda.commons.collection.LRUMap;
import org.tinylog.Logger;

import java.nio.file.Path;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Collections.reverseOrder;
import static java.util.Optional.ofNullable;
import static ooo.autopo.i18n.I18nContext.i18n;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;
import static org.sejda.commons.util.RequireUtils.requireNotNullArg;

/**
 * @author Andrea Vacondio
 */
public class DefaultRecentProjectsService implements RecentProjectsService {

    static final int MAX_CAPACITY = 20;
    private final PreferencesRepository repo;
    private final Map<String, String> cache = new LRUMap<>(MAX_CAPACITY);

    @Inject
    public DefaultRecentProjectsService(@Named("recentRepository") PreferencesRepository repo) {
        this.repo = repo;
        populateCache();
        eventStudio().addAnnotatedListeners(this);
    }

    private void populateCache() {
        try {
            Arrays.stream(repo.keys()).sorted().forEach(k -> {
                ofNullable(repo.getString(k, EMPTY)).filter(StringUtils::isNotBlank).ifPresent(v -> cache.put(v, k));
            });
        } catch (PersistenceException e) {
            Logger.error(e, i18n().tr("There was an error retrieving recently used projects"));
        }
    }

    @Override
    public void addProject(Path projectPath) {
        requireNotNullArg(projectPath, "Null project not allowed");
        cache.put(projectPath.toAbsolutePath().toString(), Long.toString(Instant.now().toEpochMilli()));
        Logger.trace("Added recently used projects {}", projectPath.toString());
    }

    @Override
    public List<String> getRecentProjects() {
        return cache.entrySet().stream().sorted(Map.Entry.comparingByValue(reverseOrder())).map(Map.Entry::getKey).toList();
    }

    @Override
    public void clear() {
        this.cache.clear();
        try {
            this.repo.clean();
        } catch (PersistenceException e) {
            Logger.error(e, "Unable to clear recently used projects");
        }
    }

    @EventListener
    public void onShutdown(ShutdownEvent event) {
        Logger.trace("Flushing recently used projects");
        try {
            this.repo.clean();
            this.cache.forEach((k, v) -> this.repo.saveString(v, k));
        } catch (PersistenceException e) {
            Logger.error("Error storing recently used projects", e);
        }
    }
}
