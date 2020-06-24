package ro.ao.benchmark.database.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import ro.ao.benchmark.database.dao.BenchmarkDao;
import ro.ao.benchmark.database.dao.SyntheticTestsDao;
import ro.ao.benchmark.model.benchmark.synthetic_testing.FakeDBEntry;
import ro.ao.benchmark.model.benchmark.LocalBenchmarkResult;
import ro.ao.benchmark.util.Constants;

@Database(entities = {LocalBenchmarkResult.class, FakeDBEntry.class}, version = 2, exportSchema = false)
public abstract class BenchmarkDatabase extends RoomDatabase {

    private static BenchmarkDatabase instance;

    public static synchronized BenchmarkDatabase getInstance(Context context) {
        if (instance == null)
            initDB(context);

        return instance;
    }

    private static void initDB(Context context) {
        instance = Room.databaseBuilder(
                context.getApplicationContext(),
                BenchmarkDatabase.class,
                Constants.BENCHMARK_DATABASE
        )
                .fallbackToDestructiveMigration()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                    }
                })
                .build();
    }

    public abstract BenchmarkDao benchmarkDao();

    public abstract SyntheticTestsDao fakeDao();
}
