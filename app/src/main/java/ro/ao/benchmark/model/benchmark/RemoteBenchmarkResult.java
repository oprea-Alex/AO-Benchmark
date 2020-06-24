package ro.ao.benchmark.model.benchmark;

import androidx.annotation.NonNull;

public class RemoteBenchmarkResult {
    private long score;
    private String deviceModel;
    private String benchmarkType;

    public RemoteBenchmarkResult() {
    }

    public RemoteBenchmarkResult(long score, String deviceModel, String benchmarkType) {
        this.score = score;
        this.deviceModel = deviceModel;
        this.benchmarkType = benchmarkType;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getBenchmarkType() {
        return benchmarkType;
    }

    public void setBenchmarkType(String benchmarkType) {
        this.benchmarkType = benchmarkType;
    }

    @NonNull
    @Override
    public String toString() {
        return "{score=" + score + "benchmarkType=" + benchmarkType + ", deviceModel=" + deviceModel + "}";
    }

}
