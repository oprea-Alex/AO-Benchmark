package ro.ao.benchmark.database.repo;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import ro.ao.benchmark.database.db.BenchmarkDatabase;
import ro.ao.benchmark.task.AOThread;
import ro.ao.benchmark.database.dao.BenchmarkDao;
import ro.ao.benchmark.model.benchmark.LocalBenchmarkResult;

public class BenchmarkRepository {
    private BenchmarkDao dao;
    private LiveData<List<LocalBenchmarkResult>> results;

    public BenchmarkRepository(Application app) {
        BenchmarkDatabase database = BenchmarkDatabase.getInstance(app);
        dao = database.benchmarkDao();
        results = dao.getAllResults();
    }

    public void insert(LocalBenchmarkResult result) {
        new AOThread(new AOThread.AOTask() {
            @Override
            public void doAction() {
                dao.insert(result);
            }
        }).start();
    }

    public void delete(LocalBenchmarkResult result) {
        new AOThread(new AOThread.AOTask() {
            @Override
            public void doAction() {
                dao.delete(result);
            }
        }).start();
    }

    public void deleteAllResults() {
        new AOThread(new AOThread.AOTask() {
            @Override
            public void doAction() {
                dao.deleteAll();
            }
        }).start();
    }

    public LiveData<List<LocalBenchmarkResult>> getResults() {
        return results;
    }
}
