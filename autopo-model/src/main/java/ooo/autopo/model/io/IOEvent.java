package ooo.autopo.model.io;

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

import java.nio.file.Path;

/**
 * @author Andrea Vacondio
 */
public record IOEvent(Path path, IOEventType type, FileType fileType) {
}