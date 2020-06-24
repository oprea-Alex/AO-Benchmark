package ro.ao.benchmark.task.custom_tasks;

import android.util.Log;

import java.util.Random;

import ro.ao.benchmark.model.benchmark.synthetic_testing.SyntheticTest;
import ro.ao.benchmark.model.benchmark.synthetic_testing.SyntheticTestResult;
import ro.ao.benchmark.task.AOThread;
import ro.ao.benchmark.task.TaskListener;
import ro.ao.benchmark.util.Constants;

public class FloatingPointTestTask extends AOThread.AOTask {
    private volatile long startTime, endTime;
    private TaskListener listener;

    public FloatingPointTestTask(TaskListener listener) {
        this.listener = listener;
    }

    @Override
    public void doAction() {
        startTime = System.currentTimeMillis();

        try {
            double a = new Random().nextDouble();
            double b = new Random().nextDouble();

            for (double i = 0.1; i <= 999999999.0; i += 1.21) {
                b += a * 2.3412 + b * 33412.12;
                a *= b / 2.1234213;
                a /= b * i;
                a += b * 31283.11 / i;
                b -= 129381912.3121 - i;
            }

        } catch (Exception e) {
            Log.e(Constants.TAG_SYNTHETIC_TESTS, "FloatingPoint Exception", e);
            listener.onException(e);
        }
    }

    @Override
    public void onFinish() {
        endTime = System.currentTimeMillis();

        SyntheticTestResult result = new SyntheticTestResult(
                SyntheticTest.FLOATING_POINT_TEST,
                "Perform math operations using huge floating numbers",
                (endTime - startTime),
                startTime,
                endTime
        );

        Log.d(Constants.TAG_SYNTHETIC_TESTS, "FloatingPoint TEST RESULT: " + result);
        listener.onTaskDone(result);
    }
}
