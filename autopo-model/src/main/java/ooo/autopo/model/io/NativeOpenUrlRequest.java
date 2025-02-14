/*
 * This file is part of the PDF Black project
 * Created on 28 mag 2020
 * Copyright 2020 by Sober Lemur S.r.l. (info@soberlemur.com).
 *
 * You are not permitted to distribute it in any form unless explicit
 * consent is given by Sober Lemur S.r.l..
 * You are not permitted to modify it.
 *
 * PDF Black is distributed WITHOUT ANY WARRANTY;
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package ooo.autopo.model.io;

/**
 * Request to natively open the given url
 *
 * @author Andrea Vacondio
 */
public record NativeOpenUrlRequest(String url) {
    // NOOP
}
