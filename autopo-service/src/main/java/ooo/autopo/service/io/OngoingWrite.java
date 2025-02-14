package ooo.autopo.service.io;
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

import javafx.scene.input.ClipboardContent;

import java.nio.file.Path;

/**
 * Fluent interface to select the destination of a text write.
 *
 * @author Andrea Vacondio
 */
public interface OngoingWrite {

    /**
     * Where the content will be written.
     *
     * @param file
     */
    void to(Path file);

    /**
     * Where the content will be written.
     *
     * @param clipboard
     */
    void to(ClipboardContent clipboard);
}
