package ro.ao.benchmark.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.BatteryManager;
import android.os.Build;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.regex.Pattern;

import static android.content.Context.BATTERY_SERVICE;

public abstract class OSUtil {

    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            Log.e(Constants.TAG_DEBUG, "Cannot hide keyboard", e);
        }
    }

    public static String getApplicationName(Context context, String packageName) {
        String appName = "";
        try {
            appName = (String) context.getPackageManager().getApplicationLabel(context
                    .getPackageManager()
                    .getApplicationInfo(
                            packageName,
                            PackageManager.GET_META_DATA
                    ));
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(Constants.TAG_DEBUG, "Cannot found app for " + packageName, e);
        }
        return appName;
    }

    public static long getApplicationSize(Context context, String packageName) {
        try {
            final PackageManager pm = context.getPackageManager();
            ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, 0);
            File file = new File(applicationInfo.publicSourceDir);
            return file.length();
        } catch (Exception e) {
            Log.e(Constants.TAG_DEBUG, "Cannot retrieve size for " + packageName, e);
            return -1L;
        }
    }

    public static Drawable getApplicationIcon(Context context, String packageName) {
        try {
            return context.getPackageManager().getApplicationIcon(packageName);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(Constants.TAG_DEBUG, "Cannot find icon for " + packageName, e);
            return null;
        }
    }

    public static boolean isAppRunning(final Context context, final String packageName) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if (procInfos != null)
        {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                if (processInfo.processName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return SanitizeUtil.capitalize(model);
        } else {
            return SanitizeUtil.capitalize(manufacturer) + " " + model;
        }
    }

    /**
     * Gets the number of cores available in this device, across all processors.
     * Requires: Ability to peruse the filesystem at "/sys/devices/system/cpu"
     * @return The number of cores, or 1 if failed to get result
     */
    public static int getNumCores() {
        //Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                //Check if filename is "cpu", followed by one or more digits
                if(Pattern.matches("cpu[0-9]+", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        try {
            //Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            //Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            //Return the number of cores (virtual CPU devices)
            return files.length;
        } catch(Exception e) {
            //Default to return 1 core
            return 1;
        }
    }

    public static double getBatteryPercentage(Context context) {

        BatteryManager bm = (BatteryManager) context.getSystemService(BATTERY_SERVICE);
        if (!bm.isCharging())
            return bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        else
            return 0;
    }

}
