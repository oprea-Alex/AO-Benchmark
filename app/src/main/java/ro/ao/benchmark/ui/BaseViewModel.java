package ro.ao.benchmark.ui;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ro.ao.benchmark.database.repo.BenchmarkRepository;
import ro.ao.benchmark.model.benchmark.LocalBenchmarkResult;
import ro.ao.benchmark.model.benchmark.RemoteBenchmarkResult;
import ro.ao.benchmark.model.benchmark.real_apps_testing.ItemAppInfo;
import ro.ao.benchmark.model.benchmark.synthetic_testing.SyntheticTestResult;
import ro.ao.benchmark.util.Constants;
import ro.ao.benchmark.util.DateUtil;
import ro.ao.benchmark.util.OSUtil;

public class BaseViewModel extends AndroidViewModel {

    /* Application context */
    protected Application app;

    /* User live data */
    protected MutableLiveData<FirebaseUser> accountLiveData;

    /* Exception live data */
    protected MutableLiveData<Exception> exceptionLiveData;

    /* Repo */
    private BenchmarkRepository localRepo;

    public BaseViewModel(Application app) {
        super(app);
        this.app = app;
        exceptionLiveData = new MutableLiveData<>();
        accountLiveData = new MutableLiveData<>();
        localRepo = new BenchmarkRepository(app);
    }

    public MutableLiveData<Exception> getExceptionLiveData() {
        return exceptionLiveData;
    }

    public MutableLiveData<FirebaseUser> getAccountLiveData() {
        return accountLiveData;
    }

    public void saveAppsScoreInDB(List<ItemAppInfo> apps, long score) {
        List<String> description = new ArrayList<>();
        for (ItemAppInfo app : apps)
            description.add(app.getName());

        saveScoreInDb("Runned apps: " + description.toString(), score);
    }

    public void saveSyntheticScoreInDb(List<SyntheticTestResult> tests, long score) {
        List<String> description = new ArrayList<>();
        for (SyntheticTestResult test : tests)
            description.add(test.getTest().getDetails());

        saveScoreInDb("Synthetic Testing: " + description.toString(), score);
    }

    private void saveScoreInDb(String description, long score) {
        Log.d(Constants.TAG_LOCAL_RANKING, "Save local score: " + score);

        LocalBenchmarkResult result = new LocalBenchmarkResult(
                description,
                score,
                DateUtil.getCurrentDate()
        );

        try {
            localRepo.insert(result);
        } catch (Exception e) {
            exceptionLiveData.postValue(e);
        }
    }

    public void saveSyntheticScoreRemote(long score) {
        saveRemoteScore(score, Constants.SYNTHETIC_TESTING_BENCHMARK);
    }

    public void saveAppsScoreRemote(long score) {
        saveRemoteScore(score, Constants.APPS_TESTING_BENCHMARK);
    }

    private void saveRemoteScore(long score, String benchmarkType) {
        Log.d(Constants.TAG_GLOBAL_RANKING, "Save score on server: " + score);

        String deviceModel = OSUtil.getDeviceName();
        RemoteBenchmarkResult result = new RemoteBenchmarkResult(score, deviceModel, benchmarkType);

        FirebaseDatabase.getInstance()
                .getReference(Constants.APP_TESTS_RESULTS_TABLE)
                .child(deviceModel + benchmarkType)
                .setValue(result)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Score saved !
                    } else {
                        Log.e(Constants.TAG_GLOBAL_RANKING, "Could not save the result on the server", task.getException());
                        exceptionLiveData.postValue(task.getException());
                    }
                });
    }
}
