package ro.ao.benchmark.model.benchmark;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "benchmark_result")
public class LocalBenchmarkResult {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "score")
    private long score;

    @ColumnInfo(name = "date")
    private String date;

    public LocalBenchmarkResult(String description, long score, String date) {
        this.description = description;
        this.score = score;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @NonNull
    @Override
    public String toString() {
        return "{id=" + id + ", description=" + description + ", score=" + score + ", date=" + date + "}";
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof LocalBenchmarkResult))
            return false;
        LocalBenchmarkResult res = (LocalBenchmarkResult) obj;
        return res.id == id && res.score == score && res.description.equals(description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, score, date);
    }
}
