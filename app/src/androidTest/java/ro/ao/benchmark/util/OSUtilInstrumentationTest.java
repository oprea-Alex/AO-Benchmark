package ro.ao.benchmark.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.BatteryManager;
import android.os.Build;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.spongycastle.util.Pack;

import java.io.File;
import java.util.List;

import static android.content.Context.BATTERY_SERVICE;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class OSUtilInstrumentationTest {

    private Context context;
    private String packageName;
    private PackageManager packageManager;
    private Drawable applicationIcon;
    private String deviceName;

    @Before
    public void setUp() throws Exception {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        packageName = context.getPackageName();
        packageManager = context.getPackageManager();
        applicationIcon = packageManager.getApplicationIcon(packageName);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getApplicationName() throws PackageManager.NameNotFoundException {
        String appName = "";
        appName = (String) context.getPackageManager().getApplicationLabel(context
                .getPackageManager()
                .getApplicationInfo(
                        packageName,
                        PackageManager.GET_META_DATA
                ));
        assertEquals(appName, OSUtil.getApplicationName(context, packageName));
    }

    @Test
    public void getApplicationSize_valid() throws PackageManager.NameNotFoundException {
        ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
        File file = new File(applicationInfo.publicSourceDir);
        assertEquals(file.length(), OSUtil.getApplicationSize(context, packageName));
    }

    @Test
    public void getApplicationSize_empty() throws PackageManager.NameNotFoundException {
        ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
        File file = new File(applicationInfo.publicSourceDir);
        assertNotEquals(file.length(), 0);
    }

    @Test
    public void getApplicationIcon_correct() {
        assertNotEquals(null, OSUtil.getApplicationIcon(context, packageName));
    }

    @Test
    public void getApplicationIcon_null() {
        assertNotEquals(null, OSUtil.getApplicationIcon(context, packageName));
    }

    @Test
    public void isAppRunning() {
        boolean isRunning = false;
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if (procInfos != null) {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                if (processInfo.processName.equals(packageName)) {
                    isRunning = true;
                }
            }
        }
        assertEquals(isRunning, OSUtil.isAppRunning(context, packageName));
    }

    @Test
    public void getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            deviceName = SanitizeUtil.capitalize(model);
        } else {
            deviceName = SanitizeUtil.capitalize(manufacturer) + " " + model;
        }
        assertEquals(deviceName, OSUtil.getDeviceName());
    }

    @Test
    public void getBatteryPercentage() {
        int batteryPercentage = 0;
        BatteryManager bm = (BatteryManager) context.getSystemService(BATTERY_SERVICE);
        if (!bm.isCharging())
            batteryPercentage = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        else
            batteryPercentage = 0;
        assertEquals(batteryPercentage, OSUtil.getBatteryPercentage(context), 0);
    }
}