package ooo.autopo.model;

/*
 * This file is part of the Autopo project
 * Created 31/01/25
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

import org.apache.commons.configuration2.PropertiesConfiguration;

import java.io.File;
import java.util.Locale;

/**
 * @author Andrea Vacondio
 */
public class PoFile {

    private File poFile;
    private Locale locale;
    private PropertiesConfiguration propertiesConfiguration;
}
