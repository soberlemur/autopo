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

import java.util.Optional;

/**
 * Service providing stage related functionality
 *
 * @author Andrea Vacondio
 */
public interface StageService {

    /**
     * Saves the given status
     *
     * @param status
     */
    void save(StageStatus status);

    /**
     * @return the latest {@link StageStatus}
     */
    Optional<StageStatus> getLatestStatus();

    /**
     * clear all stored information about the latest {@link StageStatus}
     */
    void clean();

}
