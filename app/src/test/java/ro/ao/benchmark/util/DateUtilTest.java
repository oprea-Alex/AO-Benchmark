package ro.ao.benchmark.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class DateUtilTest {

    String pattern;
    SimpleDateFormat simpleDateFormat;
    String date;

    @Before
    public void setUp() throws Exception {
        pattern = "yy-MM-dd HH:mm:ss";
        simpleDateFormat = new SimpleDateFormat(pattern);
        date = simpleDateFormat.format(new Date());
    }

    @After
    public void tearDown() throws Exception {
        simpleDateFormat = null;
    }

    @Test
    public void getCurrentDate() {
        assertEquals(date, DateUtil.getCurrentDate());
    }
}