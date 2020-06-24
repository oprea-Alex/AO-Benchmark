package ro.ao.benchmark.database.repo;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import ro.ao.benchmark.database.dao.SyntheticTestsDao;
import ro.ao.benchmark.database.db.BenchmarkDatabase;
import ro.ao.benchmark.model.benchmark.synthetic_testing.FakeDBEntry;
import ro.ao.benchmark.task.AOThread;

public class SyntheticTestsRepo {
    private SyntheticTestsDao dao;
    private LiveData<List<FakeDBEntry>> entries;

    public SyntheticTestsRepo(Application app) {
        BenchmarkDatabase database = BenchmarkDatabase.getInstance(app);
        dao = database.fakeDao();
        entries = dao.getAllResults();
    }

    public void insert(FakeDBEntry entry) {
        new AOThread(new AOThread.AOTask() {
            @Override
            public void doAction() {
                dao.insert(entry);
            }
        }).start();
    }

    public void delete(FakeDBEntry entry) {
        new AOThread(new AOThread.AOTask() {
            @Override
            public void doAction() {
                dao.delete(entry);
            }
        }).start();
    }

    public void deleteAllEntries() {
        new AOThread(new AOThread.AOTask() {
            @Override
            public void doAction() {
                dao.deleteAll();
            }
        }).start();
    }

    public LiveData<List<FakeDBEntry>> getAllEntries() {
        return entries;
    }
}
