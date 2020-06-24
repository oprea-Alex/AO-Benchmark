package ro.ao.benchmark.task.custom_tasks;

import android.util.Log;

import java.util.Random;

import ro.ao.benchmark.database.repo.SyntheticTestsRepo;
import ro.ao.benchmark.model.benchmark.synthetic_testing.FakeDBEntry;
import ro.ao.benchmark.model.benchmark.synthetic_testing.SyntheticTest;
import ro.ao.benchmark.model.benchmark.synthetic_testing.SyntheticTestResult;
import ro.ao.benchmark.task.AOThread;
import ro.ao.benchmark.task.TaskListener;
import ro.ao.benchmark.util.Constants;

public class DbTestTask extends AOThread.AOTask {
    private volatile long startTime, endTime;
    private TaskListener listener;

    public DbTestTask(TaskListener listener) {
        this.listener = listener;
    }

    @Override
    public void doAction() {
        startTime = System.currentTimeMillis();
        try {
            SyntheticTestsRepo repo = new SyntheticTestsRepo(listener.getContext());

            /* Insertion Test */
            for (int i = 0; i < 2048; ++i) {
                byte[] data = new byte[2048];
                new Random().nextBytes(data);
                repo.insert(new FakeDBEntry(data));
            }

            /* Deletion Test */
            repo.deleteAllEntries();

        } catch (Exception e) {
            Log.e(Constants.TAG_SYNTHETIC_TESTS, "DB Exception", e);
            listener.onException(e);
        }
    }

    @Override
    public void onFinish() {
        endTime = System.currentTimeMillis();

        SyntheticTestResult result = new SyntheticTestResult(
                SyntheticTest.DATABASE_TEST,
                "Performed multiple db procedures e.g. queries, insertion, deletion",
                (endTime - startTime),
                startTime,
                endTime
        );

        Log.d(Constants.TAG_SYNTHETIC_TESTS, "DB TEST RESULT: " + result);
        listener.onTaskDone(result);
    }
}

