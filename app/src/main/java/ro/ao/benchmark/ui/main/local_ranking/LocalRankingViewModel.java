package ro.ao.benchmark.ui.main.local_ranking;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import ro.ao.benchmark.database.repo.BenchmarkRepository;
import ro.ao.benchmark.model.benchmark.LocalBenchmarkResult;
import ro.ao.benchmark.ui.BaseViewModel;

public class LocalRankingViewModel extends BaseViewModel {

    private BenchmarkRepository localRepo;

    private LiveData<List<LocalBenchmarkResult>> localResults;

    public LocalRankingViewModel(Application app) {
        super(app);
        localRepo = new BenchmarkRepository(app);
        localResults = localRepo.getResults();
    }

    public LiveData<List<LocalBenchmarkResult>> getAllResults() {
        return localResults;
    }
}