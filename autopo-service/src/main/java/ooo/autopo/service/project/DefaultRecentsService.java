package ooo.autopo.service.project;

/*
 * This file is part of the Autopo project
 * Created 24/02/25
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

public class DefaultRecentsService implements RecentsService {

    static final int MAX_CAPACITY = 20;
    private final PreferencesRepository repo;
    private final Map<String, String> cache = new LRUMap<>(MAX_CAPACITY);

    @Inject
    public DefaultRecentsService(@Named("recentRepository") PreferencesRepository repo) {
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
