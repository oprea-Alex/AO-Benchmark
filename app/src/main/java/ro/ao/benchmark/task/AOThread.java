package ro.ao.benchmark.task;

import android.os.Handler;
import android.os.Looper;

public class AOThread extends Thread {

    public AOThread(AOTask r) {
        super(r);
    }

    public abstract static class AOTask implements Runnable {

        /**
         * Generic method called in run()
         */
        public abstract void doAction();

        /**
         * Generic method called on UI thread
         */
        public void onFinish() {}

        @Override
        public void run() {
            doAction();
            new Handler(Looper.getMainLooper()).post(this::onFinish);
        }
    }
}
