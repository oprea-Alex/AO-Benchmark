package ro.ao.benchmark.ui.main.benchmark.app_tests;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro.ao.benchmark.R;
import ro.ao.benchmark.model.benchmark.real_apps_testing.ItemAppInfo;
import ro.ao.benchmark.service.AppRunnerService;
import ro.ao.benchmark.ui.BaseFragment;
import ro.ao.benchmark.ui.main.benchmark.app_tests.adapter.TestedApplicationsAdapter;
import ro.ao.benchmark.util.Constants;
import ro.ao.benchmark.util.OSUtil;

public class AppsTestingFragment extends BaseFragment {

    private static final String TAG = "PerformTestsFragment";

    private View rootView;
    private ProgressBar progressBar;
    private TextView testDetailsTextView;
    private RecyclerView testedAppsRecyclerView;

    private AppsTestingViewModel viewModel;

    private Map<String, Long> launchTimes = new HashMap<>();
    private long score = 0;
    private double initialBatteryLevel = 0;
    private double afterBatteryLevel = 0;
    private double batteryDrain = 0;
    private int noOfInstalledApps = 0;

    public static final String BENCHMARK_SCORE_ACTION = "benchmark.score.action";
    public static final String BENCHMARK_SCORE_DETAILS_EXTRAS = "benchmark.score.details";
    public static final String BENCHMARK_SCORE_VALUE = "benchmark.score.value";
    public static final String BENCHMARK_SCORE_LAUNCH_TIME = "benchmark.score.launchtime";

    private IntentFilter intentFilter = new IntentFilter(BENCHMARK_SCORE_ACTION);

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            Log.d(TAG, "onReceived action=" + action);


            if (BENCHMARK_SCORE_ACTION.equals(action)) {
                Bundle options = intent.getExtras();
                List<ItemAppInfo> testedApps = (List<ItemAppInfo>) options.getSerializable(BENCHMARK_SCORE_DETAILS_EXTRAS);
                score = options.getLong(BENCHMARK_SCORE_VALUE, -1);
                score /= OSUtil.getNumCores();
                launchTimes = (Map<String, Long>) options.getSerializable(BENCHMARK_SCORE_LAUNCH_TIME);
                afterBatteryLevel = OSUtil.getBatteryPercentage(getContext());
                batteryDrain = 0;
                if (initialBatteryLevel == -1 || afterBatteryLevel == -1)
                    batteryDrain = 1;
                else
                    batteryDrain = initialBatteryLevel - afterBatteryLevel;
                if (batteryDrain != 0)
                    score /= batteryDrain;
                noOfInstalledApps = viewModel.getNoOfInstalledApps();
                //score *= noOfInstalledApps;

                testDetailsTextView.setText("Tested " + testedApps.size() + " apps:");
                TestedApplicationsAdapter adapter = new TestedApplicationsAdapter(getActivity(), testedApps, launchTimes);
                testedAppsRecyclerView.setAdapter(adapter);

                saveScoreInLocalDB(testedApps, score);
                saveScoreOnRemoteDB(score);

                displayAlert(
                        "Your Score",
                        score + " POINTS",
                        (dialog, which) -> {
                            //getActivity().onBackPressed();
                        }
                );
            }
        }
    };


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_perform_tests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(Constants.TAG_PERFORM_TESTS, "ON CREATE");

        /* Arguments */
        String[] appsPackages = getAppsPackages();
        if (appsPackages == null) {
            getActivity().onBackPressed();
            return;
        }

        /* Views */
        rootView = view;
        config();

        /* ViewModel */
        viewModel = ViewModelProviders.of(this).get(AppsTestingViewModel.class);
        observeViewModel();
        viewModel.getAppsInfo(appsPackages);
    }

    private String[] getAppsPackages() {
        try {
            return getArguments().getStringArray("packageNames");
        } catch (Exception e) {
            Log.e(Constants.TAG_PERFORM_TESTS, "Could not retrieve packages list", e);
        }
        return null;
    }

    private void config() {
        progressBar = rootView.findViewById(R.id.progressBar);
        testDetailsTextView = rootView.findViewById(R.id.testDetailsTextView);
        testedAppsRecyclerView = rootView.findViewById(R.id.testedAppsRecyclerView);
        testedAppsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        testedAppsRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
    }

    private void observeViewModel() {
        viewModel.getExceptionLiveData().observe(getViewLifecycleOwner(), e -> {
            progressBar.setVisibility(View.GONE);
            displayAlert(getString(R.string.benchmark_test_failed), e.getMessage());
        });
        viewModel.getAppsLiveData().observe(getViewLifecycleOwner(), apps -> {
            /* Perform tests */
            startApps();
        });
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "REGISTER BR");
        getActivity().registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "UNREGISTER BR!!");
        getActivity().unregisterReceiver(broadcastReceiver);

        super.onDetach();
    }

    private void startApps() {

        /* Start App runner service */
        if (AppRunnerService.isStarted) {
            Log.d(Constants.TAG_PERFORM_TESTS, "Service is already started");
            return;
        }

        this.initialBatteryLevel = OSUtil.getBatteryPercentage(getContext());
        this.afterBatteryLevel = 0;
        this.score = 0;

        Intent serviceIntent = new Intent(getActivity(), AppRunnerService.class);
        ArrayList<ItemAppInfo> appInfoArrayList = new ArrayList<>(viewModel.getAppsLiveData().getValue());
        Bundle options = new Bundle();

        options.putSerializable("apps", appInfoArrayList);
        serviceIntent.putExtras(options);
        getActivity().startService(serviceIntent);
    }

    private void saveScoreOnRemoteDB(long score) {
        viewModel.saveAppsScoreRemote(score);
    }

    private void saveScoreInLocalDB(List<ItemAppInfo> apps, long score) {
        viewModel.saveAppsScoreInDB(apps, score);
    }

}
