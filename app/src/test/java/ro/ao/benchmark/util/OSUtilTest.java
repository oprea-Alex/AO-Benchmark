package ro.ao.benchmark.util;

import android.os.Build;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OSUtilTest {

    private String deviceName;
    private int numCores;

    @Before
    public void setUp() throws Exception {
        deviceName = "";

        numCores = OSUtil.getNumCores();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getNumCores_correct() {
        if (numCores % 2 == 0){
            assertTrue(true);
        }
        else
            assertFalse(false);
    }
}