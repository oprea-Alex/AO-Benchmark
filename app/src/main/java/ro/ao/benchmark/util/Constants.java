package ro.ao.benchmark.util;

import android.os.Environment;

public interface Constants {
    /**
     * Tags
     */
    String TAG_DEBUG = "AO_DBG";
    String TAG_EVENT_OBSERVE = "AO_EVENT_OBSERVE";
    String TAG_AUTHENTICATION = "AO_AUTHENTICATION";
    String TAG_BENCHMARK = "AO_BENCHMARK";
    String TAG_PERFORM_TESTS = "AO_PERFORM_TESTS";
    String TAG_GLOBAL_RANKING = "AO_GLOBAL_RANKING";
    String TAG_LOCAL_RANKING = "AO_LOCAL_RANKING";
    String TAG_USER_PROFILE = "AO_USER_PROFILE";
    String TAG_SYNTHETIC_TESTS = "AO_SYNTHETIC_TESTS";


    /**
     * Intervals
     */
    int SPLASH_SCREEN_DURATION = 1500; // 1.5 seconds
    int CHART_ANIMATION_AXIS = 1500; // 1.5 second


    /**
     * Local Database
     */
    String BENCHMARK_DATABASE = "benchmark_database";


    /**
     * Remote Database
     */
    String USERS_TABLE = "users";
    String APP_TESTS_RESULTS_TABLE = "benchmark_results";

    /**
     * Benchmark types
     */
    String APPS_TESTING_BENCHMARK = "apps_testing";
    String SYNTHETIC_TESTING_BENCHMARK = "synthetic_testing";

    /**
     * Paths
     */
    String FAKE_FILE_PATH = Environment.getExternalStorageDirectory() + "/AO_FAKE_FILE.ao";

    /**
     * URLs
     */
    String FAKE_API = "https://jsonplaceholder.typicode.com/";
}
