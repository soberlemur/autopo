package ooo.autopo.app.config;

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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.inject.Named;
import ooo.autopo.model.ui.StageStatus;
import org.pdfsam.injector.Provides;
import org.pdfsam.persistence.DefaultEntityRepository;
import org.pdfsam.persistence.EntityRepository;
import org.pdfsam.persistence.PreferencesRepository;

/**
 * @author Andrea Vacondio
 */
public class PersistenceConfig {

    @Provides
    public ObjectMapper jsonMapper() {
        return JsonMapper.builder()
                .addModule(new Jdk8Module())
                .addModule(new JavaTimeModule())
                .enable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS)
                .enable(SerializationFeature.INDENT_OUTPUT)
                .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .visibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .serializationInclusion(JsonInclude.Include.NON_EMPTY)
                .build();
    }

    @Provides
    @Named("stageStatusRepo")
    public EntityRepository<StageStatus> stageStatusRepo(ObjectMapper mapper) {
        return new DefaultEntityRepository<>("/ooo/autopo/cleanable/stage", mapper, StageStatus.class);
    }

    @Provides
    @Named("recentRepository")
    PreferencesRepository recentRepository() {
        return new PreferencesRepository("/ooo/autopo/user/projects");
    }
}
