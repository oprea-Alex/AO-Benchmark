package ro.ao.benchmark.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class DateUtil {

    public static String getCurrentDate() {
        String pattern = "yy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        return simpleDateFormat.format(new Date());
    }
}
