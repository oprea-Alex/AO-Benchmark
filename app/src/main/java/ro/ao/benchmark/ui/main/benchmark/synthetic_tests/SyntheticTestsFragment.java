package ro.ao.benchmark.ui.main.benchmark.synthetic_tests;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import ro.ao.benchmark.R;
import ro.ao.benchmark.model.benchmark.synthetic_testing.SyntheticTest;
import ro.ao.benchmark.model.benchmark.synthetic_testing.SyntheticTestResult;
import ro.ao.benchmark.ui.BaseFragment;
import ro.ao.benchmark.ui.main.benchmark.synthetic_tests.adapter.SyntheticTestsAdapter;
import ro.ao.benchmark.util.Constants;
import ro.ao.benchmark.util.OSUtil;

public class SyntheticTestsFragment extends BaseFragment {

    /* Views */
    private View rootView;
    private ProgressBar progressBar;
    private TextView currentTestTextView;
    private TextInputEditText scoreEditText, spentTimeEditText, batteryDrainEditText;
    private RecyclerView testsRecyclerView;
    private LinearLayout scoreLayout;

    /* Tests list */
    private List<SyntheticTestResult> testResultList;
    private SyntheticTestsAdapter adapter;

    /* Battery level */
    private double initialBatteryLevel = -1;

    /* ViewModel */
    private SyntheticTestsViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_synthetic_tests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /* Views */
        rootView = view;
        config();

        /* ViewModel */
        viewModel = ViewModelProviders.of(this).get(SyntheticTestsViewModel.class);
        observeViewModel();

        /* Start tests */
        initialBatteryLevel = OSUtil.getBatteryPercentage(getActivity());
        startTests();
    }

    private void config() {
        progressBar = rootView.findViewById(R.id.progressBar);
        currentTestTextView = rootView.findViewById(R.id.currentTestTextView);
        scoreEditText = rootView.findViewById(R.id.totalScoreEditText);
        spentTimeEditText = rootView.findViewById(R.id.spentTimeEditText);
        batteryDrainEditText = rootView.findViewById(R.id.batteryDrainEditText);
        scoreLayout = rootView.findViewById(R.id.scoreInfoLayout);
        testsRecyclerView = rootView.findViewById(R.id.testsRecyclerView);
        testsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        testsRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        testResultList = new ArrayList<>();
        adapter = new SyntheticTestsAdapter(getActivity(), testResultList);
        testsRecyclerView.setAdapter(adapter);
    }

    private void startTests() {
        progressBar.setVisibility(View.VISIBLE);
        viewModel.runTest(SyntheticTest.IO_TEST);
    }

    private void observeViewModel() {
        viewModel.getExceptionLiveData().observe(getViewLifecycleOwner(), e -> {
            progressBar.setVisibility(View.GONE);
            displayAlert(getString(R.string.benchmark_test_failed), e.getMessage());
        });

        viewModel.getRunningTestLiveData().observe(getViewLifecycleOwner(), currentTest -> {
            currentTestTextView.setText(String.format("Running %s", currentTest.getDetails()));
        });
        viewModel.getTestResultLiveData().observe(getViewLifecycleOwner(), result -> {
            /* Update List */
            testResultList.add(result);
            adapter.notifyDataSetChanged();

            /* Establish the NEXT test */
            SyntheticTest nextTest = getNextTest(result.getTest());

            /* Synthetic testing finished */
            if (nextTest == null) {
                Log.d(Constants.TAG_SYNTHETIC_TESTS, "Synthetic tests finished!");
                progressBar.setVisibility(View.GONE);
                currentTestTextView.setText(R.string.well_done);

                /* Display score info */
                long timeSpent = adapter.getTotalTimeSpent();
                double batteryDrain = OSUtil.getBatteryPercentage(getActivity()) - initialBatteryLevel;
                long score = (long) adapter.calculateTotalScore(timeSpent, batteryDrain);
                scoreLayout.setVisibility(View.VISIBLE);
                spentTimeEditText.setText(String.valueOf(timeSpent));
                batteryDrainEditText.setText(String.format("%.2f%%", batteryDrain));
                scoreEditText.setText(String.valueOf(score));

                /* Save score */
                viewModel.saveSyntheticScoreInDb(testResultList, score);
                viewModel.saveSyntheticScoreRemote(score);
            } else {
                /* There are still more tests to perform */
                viewModel.runTest(nextTest);
            }
        });
    }

    private SyntheticTest getNextTest(SyntheticTest finishedTest) {
        switch (finishedTest) {
            case IO_TEST:
                return SyntheticTest.DATABASE_TEST;
            case DATABASE_TEST:
                return SyntheticTest.NETWORK_TEST;
            case NETWORK_TEST:
                return SyntheticTest.FLOATING_POINT_TEST;
            case FLOATING_POINT_TEST:
                return null;
        }

        return null;
    }
}
