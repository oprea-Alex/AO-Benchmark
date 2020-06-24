package ro.ao.benchmark.util;

import org.easymock.EasyMock;
import org.easymock.Mock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.FileOutputStream;
import java.util.Random;
import static org.junit.Assert.*;

public class IOUtilTest {

    private String filePath = "./test.file";

    @Before
    public void setUp() throws Exception {
        int i = 1024 * 100;

        FileOutputStream outputStream = new FileOutputStream(filePath);
        while (i != -1) {
            byte[] buffer = new byte[1024];
            new Random().nextBytes(buffer);
            outputStream.write(buffer, 0, buffer.length);

            i --;
        }

        outputStream.close();
    }

    @After
    public void tearDown() throws Exception {
        IOUtil.deleteFile(filePath);
    }


    @Test
    public void deleteFile() {
        assertTrue(IOUtil.deleteFile(filePath));
    }

}