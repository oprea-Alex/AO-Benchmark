package ro.ao.benchmark.task;

import android.app.Application;

import ro.ao.benchmark.model.benchmark.synthetic_testing.SyntheticTestResult;

public abstract class TaskListener {

    private Application context;

    public void setContext(Application context) {
        this.context = context;
    }

    public Application getContext() {
        return context;
    }

    public abstract void onException(Exception e);

    public abstract void onTaskDone(SyntheticTestResult result);
}
