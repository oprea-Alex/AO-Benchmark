package ro.ao.benchmark.model.benchmark.synthetic_testing;

import androidx.annotation.NonNull;

public class SyntheticTestResult {
    private SyntheticTest test;
    private String description;
    private long spentTime;
    private long startTime;
    private long endTime;

    public SyntheticTestResult(SyntheticTest test, String description, long score, long startTime, long endTime) {
        this.test = test;
        this.description = description;
        this.spentTime = score;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public SyntheticTest getTest() {
        return test;
    }

    public void setTest(SyntheticTest test) {
        this.test = test;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getSpentTime() {
        return spentTime;
    }

    public void setSpentTime(long spentTime) {
        this.spentTime = spentTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    @NonNull
    @Override
    public String toString() {
        return "{test=" + test.name() + ", description=" + description
                + ", score=" + spentTime + ", startTime=" + startTime + ", endTime=" + endTime + "}";
    }
}
