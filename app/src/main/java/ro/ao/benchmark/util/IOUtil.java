package ro.ao.benchmark.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public abstract class IOUtil {

    public static void generateRandomFile(String filePath) throws IOException {
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

    public static boolean deleteFile(String path) {
        File file = new File(path);
        boolean res= file.delete();

        return res;
    }

}
