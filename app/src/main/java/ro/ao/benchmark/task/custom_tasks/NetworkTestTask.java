package ro.ao.benchmark.task.custom_tasks;

import android.util.Log;

import ro.ao.benchmark.model.benchmark.synthetic_testing.SyntheticTest;
import ro.ao.benchmark.model.benchmark.synthetic_testing.SyntheticTestResult;
import ro.ao.benchmark.networking.RetrofitClient;
import ro.ao.benchmark.task.AOThread;
import ro.ao.benchmark.task.TaskListener;
import ro.ao.benchmark.util.Constants;

public class NetworkTestTask extends AOThread.AOTask {
    private volatile long startTime, endTime;
    private TaskListener listener;

    private volatile int HTTP_REQUESTS_COUNT_TIMES = 10;

    public NetworkTestTask(TaskListener listener) {
        this.listener = listener;
    }

    @Override
    public void doAction() {

        /* Save start time */
        if (HTTP_REQUESTS_COUNT_TIMES == 10)
            startTime = System.currentTimeMillis();

        /* Decrease counter */
        HTTP_REQUESTS_COUNT_TIMES--;

        /* Perform request */
        try {
            RetrofitClient.getApi().getFakeResponse().execute();
        } catch (Exception e) {
            Log.e(Constants.TAG_SYNTHETIC_TESTS, "Network Exception", e);
            listener.onException(e);
        }
    }

    @Override
    public void onFinish() {

        /* Counter != 0 => Then perform the remaining HTTP requests */
        if (HTTP_REQUESTS_COUNT_TIMES > 0) {
            new AOThread(this).start();
        } else {

            /* Else, save the result info */
            endTime = System.currentTimeMillis();

            SyntheticTestResult result = new SyntheticTestResult(
                    SyntheticTest.NETWORK_TEST,
                    "Perform multiple HTTP requests",
                    (endTime - startTime),
                    startTime,
                    endTime
            );

            Log.d(Constants.TAG_SYNTHETIC_TESTS, "NETWORK TEST RESULT: " + result);
            listener.onTaskDone(result);
        }
    }
}
