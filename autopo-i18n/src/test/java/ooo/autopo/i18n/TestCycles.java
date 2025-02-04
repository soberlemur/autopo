package org.pdfsam.i18n;
/*
 * This file is part of the Autopo project
 * Created 30/01/25
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

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;

/**
 * @author Andrea Vacondio
 */
@AnalyzeClasses(packages = "ooo.autopo.i18n", importOptions = { ImportOption.DoNotIncludeTests.class })
public class TestCycles {

    @ArchTest
    public static final ArchRule myRule = SlicesRuleDefinition.slices().matching("ooo.autopo.(*)..").should()
                                                              .beFreeOfCycles();

}
