package ooo.autopo.service.io;

/*
 * This file is part of the Autopo project
 * Created 05/02/25
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

import com.soberlemur.potentilla.catalog.parse.ParseException;
import ooo.autopo.model.po.PoFile;
import ooo.autopo.model.po.PotFile;
import ooo.autopo.model.project.Project;

import java.io.IOException;

/**
 * @author Andrea Vacondio
 */
public interface IOService {
    void load(Project project) throws IOException;

    void load(PotFile pot) throws IOException;

    void load(PoFile poFile) throws IOException, ParseException;

    void updatePoFromTemplate(PoFile poFile, PotFile potFile) throws IOException;

    void save(PoFile poFile) throws IOException;

    void save(Project project) throws IOException;
}
