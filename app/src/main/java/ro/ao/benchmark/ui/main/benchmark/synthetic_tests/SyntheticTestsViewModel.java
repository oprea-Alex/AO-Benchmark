package ro.ao.benchmark.ui.main.benchmark.synthetic_tests;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import ro.ao.benchmark.model.benchmark.synthetic_testing.SyntheticTest;
import ro.ao.benchmark.model.benchmark.synthetic_testing.SyntheticTestResult;
import ro.ao.benchmark.task.AOThread;
import ro.ao.benchmark.task.custom_tasks.DbTestTask;
import ro.ao.benchmark.task.custom_tasks.FloatingPointTestTask;
import ro.ao.benchmark.task.custom_tasks.IOTestTask;
import ro.ao.benchmark.task.TaskListener;
import ro.ao.benchmark.task.custom_tasks.NetworkTestTask;
import ro.ao.benchmark.ui.BaseViewModel;
import ro.ao.benchmark.util.Constants;

public class SyntheticTestsViewModel extends BaseViewModel {

    /* Running Test Live Data */
    protected MutableLiveData<SyntheticTest> currentTestLiveData;
    /* Synthetic test result description Live Data */
    protected MutableLiveData<SyntheticTestResult> testResultLiveData;

    /* Tasks listener */
    private TaskListener taskListener = new TaskListener() {
        @Override
        public void onException(Exception e) {
            exceptionLiveData.postValue(e);
        }

        @Override
        public void onTaskDone(SyntheticTestResult result) {
            testResultLiveData.postValue(result);
        }
    };

    public SyntheticTestsViewModel(Application app) {
        super(app);
        currentTestLiveData = new MutableLiveData<>();
        testResultLiveData = new MutableLiveData<>();
        taskListener.setContext(getApplication());
    }

    public MutableLiveData<SyntheticTestResult> getTestResultLiveData() {
        return testResultLiveData;
    }

    public MutableLiveData<SyntheticTest> getRunningTestLiveData() {
        return currentTestLiveData;
    }

    public void runTest(SyntheticTest test) {
        Log.d(Constants.TAG_SYNTHETIC_TESTS, test.getDetails());

        switch (test) {
            case IO_TEST:
                IOTest();
                break;
            case FLOATING_POINT_TEST:
                floatingPointTest();
                break;
            case DATABASE_TEST:
                dbTest();
                break;
            case NETWORK_TEST:
                networkTest();
                break;
            default:
                break;
        }

        currentTestLiveData.postValue(test);
    }

    private void IOTest() {
        new AOThread(new IOTestTask(taskListener)).start();
    }

    private void floatingPointTest() {
        new AOThread(new FloatingPointTestTask(taskListener)).start();
    }

    private void dbTest() {
        new AOThread(new DbTestTask(taskListener)).start();
    }

    private void networkTest() {
        new AOThread(new NetworkTestTask(taskListener)).start();
    }
}
