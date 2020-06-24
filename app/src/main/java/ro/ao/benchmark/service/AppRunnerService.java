package ro.ao.benchmark.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro.ao.benchmark.model.benchmark.real_apps_testing.ItemAppInfo;
import ro.ao.benchmark.ui.main.benchmark.app_tests.AppsTestingFragment;
import ro.ao.benchmark.util.Constants;

public class AppRunnerService extends Service {

    private static final String TAG = "AppRunnerService";
    public static boolean isStarted = false;

    private Map<String, Long> startTimes = new HashMap<>();
    private Map<String, Long> endTimes = new HashMap<>();
    private Map<String, Long> launchTimes = new HashMap<>();

    private List<ItemAppInfo> runnedApps = new ArrayList<>();

    public static final String APP_FINISHED_ACTION = "app.finished.action";
    public static final String APP_FINISHED_PACKAGE_EXTRAS = "app.finished.package";
    private IntentFilter appFinishedIntentFilter = new IntentFilter(AppRunnerService.APP_FINISHED_ACTION);
    private AppRunnerBR appRunnerBR = null;

    private class AppRunnerBR extends BroadcastReceiver {

        private List<ItemAppInfo> apps;

        public AppRunnerBR(List<ItemAppInfo> apps) {
            this.apps = apps;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            switch (action) {
                case AppRunnerService.APP_FINISHED_ACTION:
                    String finishedAppPackage = intent.getStringExtra(AppRunnerService.APP_FINISHED_PACKAGE_EXTRAS);
                    Log.d(TAG, "onReceive: Finished app" + finishedAppPackage);

                    /* Delete from list the finishing app */
                    ItemAppInfo finishedApp = null;
                    for (ItemAppInfo app : apps) {
                        if (app.getPackageName().equals(finishedAppPackage)) {
                            finishedApp = app;
                        }
                    }
                    if (finishedApp == null) {
                        Log.d(TAG, "onReceive: Smth went wrong!");
                        return;
                    }
                    apps.remove(finishedApp);

                    /* Update end time */
                    long endTime = System.currentTimeMillis();
                    endTimes.put(finishedAppPackage, endTime);
                    Log.d(TAG, "Update end time for " + finishedAppPackage + " to " + endTime);

                    /* Start next one */
                    if (!apps.isEmpty())
                        launchApp(apps.get(0).getPackageName(), true);
                    else {
                        launchApp(getPackageName(), false);
                        saveScore();
                        stopSelf();
                    }
                    break;
                default:
                    break;
            }
        }
    }


    private void saveScore() {
        long score = 0;
        for (Map.Entry<String, Long> entry : startTimes.entrySet()) {
            String appPackage = entry.getKey();
            long startTime = entry.getValue();
            long endTime = endTimes.get(appPackage);

            long lT = endTime - startTime;
            long launchTime = lT / 10;
            launchTimes.put(appPackage, launchTime);

            ItemAppInfo appInfo = getAppByPackageName(appPackage);

            try {
                score += (appInfo.getSize() / 100) / launchTime ;
            } catch (Exception e) {
                Log.d(TAG, "Update score exception", e);
            }
        }

        Log.d(TAG, "Send broadcast with score=" + score + "; details=" + runnedApps);

        Intent intent = new Intent(AppsTestingFragment.BENCHMARK_SCORE_ACTION);
        intent.putExtra(AppsTestingFragment.BENCHMARK_SCORE_DETAILS_EXTRAS, (Serializable) runnedApps);
        intent.putExtra(AppsTestingFragment.BENCHMARK_SCORE_VALUE, score);
        intent.putExtra(AppsTestingFragment.BENCHMARK_SCORE_LAUNCH_TIME, (Serializable) launchTimes);

        sendBroadcast(intent);
    }


    private ItemAppInfo getAppByPackageName(String packageName) {
        for (ItemAppInfo app : runnedApps) {
            if (app.getPackageName().equals(packageName))
                return app;
        }

        return null;
    }


    public AppRunnerService() {
        super();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "Service is started");

        if (intent == null)
            return super.onStartCommand(null, flags, startId);

        Bundle options = intent.getExtras();
        List<ItemAppInfo> apps = (List<ItemAppInfo>) options.getSerializable("apps");


        /* Save apps */
        runnedApps.clear();
        runnedApps.addAll(apps);

        /* Init br */
        appRunnerBR = new AppRunnerBR(apps);
        getApplicationContext().registerReceiver(appRunnerBR, appFinishedIntentFilter);

        launchApp(apps.get(0).getPackageName(), true);

        isStarted = true;

        return Service.START_NOT_STICKY;
    }


    private void launchApp(String packageName, boolean observe) {
        Log.d(TAG, "Launch app: " + packageName);

        if (observe) {
            /* Notify Accessibility Service with the current app */
            Intent intent = new Intent(AOAccessibilityService.APP_PACKAGE_ACTION);
            intent.putExtra(AOAccessibilityService.APP_PACKAGE_EXTRAS, packageName);
            sendBroadcast(intent);
        }

        if (observe) {
            /* Save start time */
            long startTime = System.currentTimeMillis();
            startTimes.put(packageName, startTime);
            Log.d(TAG, "Update end time for " + packageName + " to " + startTime);
        }


        /* Launch app */
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
        launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(launchIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "Service destroyed!");
        System.out.println(Constants.TAG_EVENT_OBSERVE + "Service is destroyed!");
        isStarted = false;
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
