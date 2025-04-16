package ooo.autopo.model.po;

import org.junit.jupiter.api.Test;

import static java.util.Locale.of;
import static ooo.autopo.model.po.LocaleUtils.languageHeaderFromLocale;
import static ooo.autopo.model.po.LocaleUtils.localeFromFilename;
import static ooo.autopo.model.po.LocaleUtils.localeFromHeader;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LocaleUtilsTest {

    @Test
    public void localeValidLanguageOnly() {
        assertEquals(of("en"), localeFromHeader("en"), "Locale should only have the language set.");
    }

    @Test
    public void localeValidLanguageAndRegion() {
        assertEquals(of("en", "US"), localeFromHeader("en_US"), "Locale should have language and region set.");
    }

    @Test
    public void localeValidLanguageRegionAndVariant() {
        assertEquals(of("en", "US", "POSIX"), localeFromHeader("en_US@POSIX"), "Locale should have language, region, and variant set.");
    }

    @Test
    public void localeInvalidLanguageHeader() {
        assertNull(localeFromHeader("invalid_language_header"), "Invalid language header should return null.");
    }

    @Test
    public void invalidHeaderFormat() {
        assertNull(localeFromHeader("en-US"), "Ill-formed locale should return null.");
    }

    @Test
    public void blankHeader() {
        assertNull(localeFromHeader("  "), "Blank header should return null.");
    }

    @Test
    public void nullHeader() {
        assertNull(localeFromHeader(null), "Null header should return null.");
    }

    @Test
    public void validLanguageOnlyFilename() {
        assertEquals(of("en"), localeFromFilename("en"), "Locale should be parsed correctly from filename with language only.");
    }

    @Test
    public void validLanguageAndCountryFilename() {
        assertEquals(of("en", "US"), localeFromFilename("en_US"), "Locale should be parsed correctly from filename with language and country.");
    }

    @Test
    public void validLanguageCountryAndVariantFilename() {
        assertEquals(of("en", "US", "POSIX"),
                     localeFromFilename("en_US_POSIX"),
                     "Locale should be parsed correctly from filename with language, country, and variant.");
    }

    @Test
    public void invalidFilename() {
        assertNull(localeFromFilename("invalid_locale"), "Invalid locale filename should return null.");
    }

    @Test
    public void validLanguageAndCountryFilenameWithDash() {
        assertEquals(of("en", "US"), localeFromFilename("en-US"), "Locale should be parsed correctly from filename with language and country.");
    }

    @Test
    public void emptyFilename() {
        assertNull(localeFromFilename("  "), "Empty filename should return null.");
    }

    @Test
    public void nullFilename() {
        assertNull(localeFromFilename(null), "Null filename should return null.");
    }

    @Test
    public void headerLanguageOnly() {
        assertEquals("en", languageHeaderFromLocale(of("en")), "Language tag should only contain the language part.");
    }

    @Test
    public void headerLanguageAndCountry() {
        assertEquals("en_US", languageHeaderFromLocale(of("en", "US")), "Language tag should contain language and uppercase country.");
    }

    @Test
    public void headerLanguageCountryAndVariant() {
        assertEquals("en_US@posix",
                     languageHeaderFromLocale(of("en", "US", "POSIX")),
                     "Language tag should contain language, uppercase country, and lowercase variant.");
    }

    @Test
    public void headerEmptyLanguage() {
        assertEquals("_US@posix", languageHeaderFromLocale(of("", "US", "POSIX")), "Language tag should handle empty language part correctly.");
    }

    @Test
    public void headerInvalidCountryAndVariant() {
        assertEquals("en_INVALID_COUNTRY@invalid_variant",
                     languageHeaderFromLocale(of("en", "invalid_country", "INVALID_VARIANT")),
                     "Language tag should handle invalid country and variant parts correctly.");
    }

    @Test
    public void headerNullLocale() {
        assertThrows(NullPointerException.class, () -> languageHeaderFromLocale(null), "Should throw NullPointerException for null Locale.");
    }
}