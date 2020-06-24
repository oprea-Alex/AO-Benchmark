package ro.ao.benchmark.ui.main.benchmark.app_tests;

import android.app.Application;
import android.content.Intent;
import android.content.pm.ResolveInfo;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import ro.ao.benchmark.model.benchmark.real_apps_testing.ItemAppInfo;
import ro.ao.benchmark.ui.BaseViewModel;
import ro.ao.benchmark.util.OSUtil;

public class AppsTestingViewModel extends BaseViewModel {

    /* Testings apps live data */
    private MutableLiveData<List<ItemAppInfo>> appsLiveData;

    public AppsTestingViewModel(Application app) {
        super(app);
        appsLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<ItemAppInfo>> getAppsLiveData() {
        return appsLiveData;
    }

    public void getAppsInfo(String[] appsPackages) {
        List<ItemAppInfo> apps = new ArrayList<>();

        for (String pkg : appsPackages) {
            apps.add(new ItemAppInfo(
                    pkg,
                    OSUtil.getApplicationName(app, pkg),
                    OSUtil.getApplicationSize(app, pkg)
            ));
        }

        appsLiveData.postValue(apps);
    }

    public int getNoOfInstalledApps(){
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = app.getPackageManager().queryIntentActivities(mainIntent, 0);
        return pkgAppsList.size();
    }
}