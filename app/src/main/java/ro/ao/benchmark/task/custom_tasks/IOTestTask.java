package ro.ao.benchmark.task.custom_tasks;

import android.util.Log;

import ro.ao.benchmark.model.benchmark.synthetic_testing.SyntheticTest;
import ro.ao.benchmark.model.benchmark.synthetic_testing.SyntheticTestResult;
import ro.ao.benchmark.task.AOThread;
import ro.ao.benchmark.task.TaskListener;
import ro.ao.benchmark.util.Constants;
import ro.ao.benchmark.util.IOUtil;

public class IOTestTask extends AOThread.AOTask {
    private volatile long startTime, endTime;
    private TaskListener listener;

    public IOTestTask(TaskListener listener) {
        this.listener = listener;
    }

    @Override
    public void doAction() {
        startTime = System.currentTimeMillis();

        try {
            /* File creation test */
            IOUtil.generateRandomFile(Constants.FAKE_FILE_PATH);

            /* File deletion test */
            IOUtil.deleteFile(Constants.FAKE_FILE_PATH);

        } catch (Exception e) {
            Log.e(Constants.TAG_SYNTHETIC_TESTS, "IO Exception", e);
            listener.onException(e);
        }
    }

    @Override
    public void onFinish() {
        endTime = System.currentTimeMillis();

        SyntheticTestResult result = new SyntheticTestResult(
                SyntheticTest.IO_TEST,
                "Created a big file filled with random values",
                (endTime - startTime),
                startTime,
                endTime
        );

        Log.d(Constants.TAG_SYNTHETIC_TESTS, "IO TEST RESULT: " + result);
        listener.onTaskDone(result);
    }
}
