package ro.ao.benchmark.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SanitizeUtilTest {

    String emptyString = "";
    String validMail = "a@a.com";
    String nonValidMail_noDomain = "a@a";
    String nonValidMail_noAt = "aa.com";
    String lowerName = "alex";

    @Test
    public void isNullOrEmpty_correct() {
        assertEquals(validMail.isEmpty(), SanitizeUtil.isNullOrEmpty(validMail));
    }

    @Test
    public void isEmailNonValid_noAt(){
        assertNotEquals(true,SanitizeUtil.isEmailValid(nonValidMail_noAt));
    }

    @Test
    public void isEmailNonValid_noDomain(){
        assertNotEquals(true, SanitizeUtil.isEmailValid(nonValidMail_noDomain));
    }

    @Test
    public void isNullOrEmpty_wrong(){
        assertNotEquals(false, SanitizeUtil.isNullOrEmpty(emptyString));
    }

    @Test
    public void isEmailValid_correct() {
        assertTrue(SanitizeUtil.isEmailValid(validMail));
    }

    @Test
    public void capitalize() {
        assertNotEquals(lowerName,SanitizeUtil.capitalize(lowerName));
    }
}