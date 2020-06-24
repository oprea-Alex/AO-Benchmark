package ro.ao.benchmark.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ro.ao.benchmark.model.benchmark.LocalBenchmarkResult;

/* Benchmark results Data access object */
@Dao
public interface BenchmarkDao {

    @Insert
    void insert(LocalBenchmarkResult result);

    @Delete
    void delete(LocalBenchmarkResult result);

    @Query("DELETE FROM benchmark_result")
    void deleteAll();

    @Query("SELECT * from benchmark_result ORDER BY id DESC")
    LiveData<List<LocalBenchmarkResult>> getAllResults();
}
