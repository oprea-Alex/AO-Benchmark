package ro.ao.benchmark.ui.main.benchmark.app_tests;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import ro.ao.benchmark.R;
import ro.ao.benchmark.model.benchmark.real_apps_testing.ItemAppInfo;
import ro.ao.benchmark.service.AOAccessibilityService;
import ro.ao.benchmark.ui.BaseFragment;
import ro.ao.benchmark.ui.main.benchmark.app_tests.adapter.ApplicationsAdapter;
import ro.ao.benchmark.util.Constants;
import ro.ao.benchmark.util.SanitizeUtil;

public class AppSelectorFragment extends BaseFragment {

    private View rootView;
    private ProgressBar progressBar;
    private RecyclerView appsRecyclerView;
    private Button startBenchmarkButton;
    private ApplicationsAdapter adapter;

    private AppSelectorViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_benchmark, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(Constants.TAG_BENCHMARK, "ON CREATE");

        /* Views */
        rootView = view;
        config();

        /* ViewModel */
        viewModel = ViewModelProviders.of(this).get(AppSelectorViewModel.class);
        observeViewModel();

        /* Get all applications */
        getSystemApps();

        startAccessibilityService();
    }

    private void config() {
        progressBar = rootView.findViewById(R.id.progressBar);
        appsRecyclerView = rootView.findViewById(R.id.appsRecyclerView);
        appsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        appsRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        startBenchmarkButton = rootView.findViewById(R.id.startBenchmarkButton);
        startBenchmarkButton.setVisibility(View.GONE);
        startBenchmarkButton.setOnClickListener(v -> {
            startBenchmark();
        });
    }

    private void startBenchmark() {
        List<ItemAppInfo> checkedApps = adapter.getCheckedApps();

        if (SanitizeUtil.isNullOrEmpty(checkedApps)) {
            Snackbar.make(rootView, R.string.select_apps_msg, Snackbar.LENGTH_SHORT).show();
            return;
        }

        /* Store only apps packages because we will use them in order to start intents */
        String[] packageNames = new String[checkedApps.size()];
        for (int i = 0; i < checkedApps.size(); ++i)
            packageNames[i] = checkedApps.get(i).getPackageName();

        /* Start tests */
        NavDirections directions = AppSelectorFragmentDirections.navToTests(packageNames);
        Navigation.findNavController(rootView).navigate(
                directions
        );
    }

    private void getSystemApps() {
        progressBar.setVisibility(View.VISIBLE);
        viewModel.getSystemApps();
    }

    private void observeViewModel() {
        viewModel.getExceptionLiveData().observe(getViewLifecycleOwner(), e -> {
            progressBar.setVisibility(View.GONE);
            displayAlert(getString(R.string.benchmark_test_failed), e.getMessage());
        });
        viewModel.getAppsLiveData().observe(getViewLifecycleOwner(), appsList -> {
            progressBar.setVisibility(View.GONE);
            startBenchmarkButton.setVisibility(View.VISIBLE);
            adapter = new ApplicationsAdapter(getActivity(), appsList);
            appsRecyclerView.setAdapter(adapter);
        });
    }

    private void startAccessibilityService() {
        if (!AOAccessibilityService.isStarted) {
            displayAlert("Enable Accessibility Service", "Accessibility Service is not enabled for AO Benchmark. Please start it in order to proceed!", (dialog, which) -> {
                Intent openSettings = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                openSettings.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(openSettings);
            });
        }
    }
}
