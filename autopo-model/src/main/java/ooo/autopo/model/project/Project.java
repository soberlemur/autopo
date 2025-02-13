package ooo.autopo.model.project;

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

import java.nio.file.Path;
import java.util.Properties;

/**
 * @author Andrea Vacondio
 */
public class Project {

    private final Path location;
    private final Properties properties;

    public Project(Path location, Properties properties) {
        this.location = location;
        this.properties = properties;
    }

    /**
     * @return the value for the property or null
     */
    public String property(ProjectProperty property) {
        return properties.getProperty(property.key());
    }
}
