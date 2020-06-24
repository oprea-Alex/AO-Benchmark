package ro.ao.benchmark.service;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import ro.ao.benchmark.util.SanitizeUtil;

public class AOAccessibilityService extends AccessibilityService {

    private static final String TAG = "AOAccessibilityService";
    public static boolean isStarted = false;

    /* Store launched app package name */
    private String currentAppPackage = null;

    /* State observer utils */
    private final Object lock = new Object();
    private boolean stateChanged = false, contentChanged = false,
            viewScrolled = false, viewFocused = false;
    private int triggeredApp = 0;

    /* BR utils */
    public static final String APP_PACKAGE_ACTION = "current.package.action";
    public static final String APP_PACKAGE_EXTRAS = "current.package";
    private IntentFilter packageIntentFilter = new IntentFilter(AOAccessibilityService.APP_PACKAGE_ACTION);
    private PackageReceiveBR packageReceiveBR = null;

    private class PackageReceiveBR extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            switch (action) {
                case AOAccessibilityService.APP_PACKAGE_ACTION:
                    /* Set current package */
                    currentAppPackage = intent.getStringExtra(AOAccessibilityService.APP_PACKAGE_EXTRAS);
                    triggeredApp = 0;
                    stateChanged = false;
                    contentChanged = false;
                    viewScrolled = false;
                    viewFocused = false;
                    Log.d(TAG, "onReceive: Current package=" + currentAppPackage);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onServiceConnected() {
        Log.v(TAG, "Service is connected");
        isStarted = true;
        Log.v(TAG, "Service is started");

        packageReceiveBR = new PackageReceiveBR();
        getApplicationContext().registerReceiver(packageReceiveBR, packageIntentFilter);
        super.onServiceConnected();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        String eventPackage = event.getPackageName() == null ? null : event.getPackageName().toString();

        if (SanitizeUtil.isNullOrEmpty(eventPackage) || SanitizeUtil.isNullOrEmpty(currentAppPackage))
            return;
        if (!eventPackage.equals(currentAppPackage))
            return;

        Log.v(TAG, "Event related to " + currentAppPackage + "; Event details:" + event);

        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_FOCUSED && !viewFocused)
            viewFocused = true;

        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && !stateChanged) {
            stateChanged = true;
            if (viewFocused){
                notifyAppRunner();
            }

        }
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED && !contentChanged && stateChanged) {
            contentChanged = true;
        }
        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_SCROLLED && stateChanged && !viewScrolled) {
            viewScrolled = true;
        }

        if (contentChanged || viewScrolled) {
            notifyAppRunner();
        }
    }

    private void notifyAppRunner(){
        synchronized (lock) {
            if (triggeredApp == 0) {
                triggeredApp = 1;
                Intent intent = new Intent(AppRunnerService.APP_FINISHED_ACTION);
                intent.putExtra(AppRunnerService.APP_FINISHED_PACKAGE_EXTRAS, currentAppPackage);
                sendBroadcast(intent);
            }
        }
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "Service destroyed!");
        isStarted = false;
        super.onDestroy();
    }

    @Override
    public void onInterrupt() {
        Log.v(TAG, "Service interrupted!");
    }
}
