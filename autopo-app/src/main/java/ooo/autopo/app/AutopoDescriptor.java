package ooo.autopo.app;

/*
 * This file is part of the Autopo project
 * Created 14/02/25
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
