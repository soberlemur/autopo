package ooo.autopo.app;

/*
 * This file is part of the Autopo project
 * Created 14/02/25
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

import ooo.autopo.model.AppDescriptor;
import ooo.autopo.model.AppDescriptorProperty;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * @author Andrea Vacondio
 */
public class AutopoDescriptor implements AppDescriptor {
    private final Properties properties = new Properties();

    public AutopoDescriptor() throws IOException {
        try (InputStream stream = this.getClass().getResourceAsStream("/autopo.properties")) {
            properties.load(stream);
        }
    }

    public String property(AppDescriptorProperty prop, String defaultValue) {
        return properties.getProperty(prop.prop, defaultValue);
    }

    public String property(AppDescriptorProperty prop) {
        return properties.getProperty(prop.prop, EMPTY);
    }
}
