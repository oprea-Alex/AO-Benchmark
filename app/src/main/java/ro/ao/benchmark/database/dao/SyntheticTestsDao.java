package ro.ao.benchmark.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ro.ao.benchmark.model.benchmark.synthetic_testing.FakeDBEntry;

@Dao
public interface SyntheticTestsDao {

    @Insert
    void insert(FakeDBEntry entry);

    @Delete
    void delete(FakeDBEntry entry);

    @Query("DELETE FROM fakes")
    void deleteAll();

    @Query("SELECT * from fakes ORDER BY id DESC")
    LiveData<List<FakeDBEntry>> getAllResults();
}
