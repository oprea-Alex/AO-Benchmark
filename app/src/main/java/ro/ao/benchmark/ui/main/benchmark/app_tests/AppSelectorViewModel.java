package ro.ao.benchmark.ui.main.benchmark.app_tests;

import android.app.Application;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import ro.ao.benchmark.model.benchmark.real_apps_testing.ItemAppInfo;
import ro.ao.benchmark.task.AOThread;
import ro.ao.benchmark.ui.BaseViewModel;
import ro.ao.benchmark.util.Constants;
import ro.ao.benchmark.util.OSUtil;

public class AppSelectorViewModel extends BaseViewModel {

    private MutableLiveData<List<ItemAppInfo>> appsLiveData;

    public AppSelectorViewModel(Application app) {
        super(app);
        appsLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<ItemAppInfo>> getAppsLiveData() {
        return appsLiveData;
    }

    public void getSystemApps() {
        /* Launch a new task on a separate thread in order to avoid main thread freeze */
        new AOThread(new AOThread.AOTask() {
            @Override
            public void doAction() {
                getAllApps();
            }
        }).start();
    }

    private void getAllApps() {
        try {
            Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> pkgAppsList = app.getPackageManager().queryIntentActivities(mainIntent, 0);

            ArrayList<ItemAppInfo> appsList = new ArrayList<>();
            for (ResolveInfo item : pkgAppsList) {
                String packageName = item.activityInfo.packageName;
                String appName = OSUtil.getApplicationName(app, packageName);
                long size = OSUtil.getApplicationSize(app, packageName);

                appsList.add(new ItemAppInfo(packageName, appName, size));
            }

            appsLiveData.postValue(appsList);
        } catch (Exception e) {
            Log.e(Constants.TAG_BENCHMARK, "Could not retrieve apps", e);
            exceptionLiveData.postValue(e);
        }
    }
}