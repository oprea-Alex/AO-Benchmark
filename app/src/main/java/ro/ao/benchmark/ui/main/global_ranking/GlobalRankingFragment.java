package ro.ao.benchmark.ui.main.global_ranking;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

import ro.ao.benchmark.R;
import ro.ao.benchmark.model.benchmark.RemoteBenchmarkResult;
import ro.ao.benchmark.ui.BaseFragment;
import ro.ao.benchmark.ui.main.global_ranking.adapter.ChartXAxisFormatter;
import ro.ao.benchmark.util.Constants;

public class GlobalRankingFragment extends BaseFragment {

    /* Views */
    private View rootView;
    private ProgressBar progressBar;
    private HorizontalBarChart barChart;

    /* ViewModel */
    private GlobalRankingViewModel viewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        /* Display layout in landscape mode for better chart analysis */
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        /* Display layout in portrait mode again */
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onDetach();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_global_ranking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(Constants.TAG_GLOBAL_RANKING, "ON CREATE");

        /* Views */
        rootView = view;
        config();

        /* ViewModel */
        viewModel = ViewModelProviders.of(this).get(GlobalRankingViewModel.class);
        observeViewModel();
    }

    private void config() {
        progressBar = rootView.findViewById(R.id.progressBar);

        barChart = rootView.findViewById(R.id.barChart);
        barChart.getLegend().setEnabled(true);
        barChart.getDescription().setEnabled(true);
        barChart.getDescription().setText(getString(R.string.top_scores));
        barChart.getDescription().setTextColor(Color.RED);
        barChart.setVisibleXRangeMaximum(5.0f);
        barChart.enableScroll();
    }

    private void getTopScores() {
        viewModel.retrieveRemoteScores();
    }

    private void observeViewModel() {
        viewModel.getExceptionLiveData().observe(getViewLifecycleOwner(), e -> {
            progressBar.setVisibility(View.GONE);
            displayAlert(getString(R.string.retrieve_online_scores_failed), e.getMessage());
        });

        viewModel.getAccountLiveData().observe(getViewLifecycleOwner(), user -> {
            if (user != null)
                getTopScores();
        });

        viewModel.getTopScoresLiveData().observe(getViewLifecycleOwner(), results -> {
            Log.d(Constants.TAG_GLOBAL_RANKING, "Top Apps Tests Scores: " + results);
            progressBar.setVisibility(View.GONE);
            updateChart(results);
        });
    }

    private void updateChart(List<RemoteBenchmarkResult> results) {
        BarDataSet set1 = new BarDataSet(new ArrayList<>(), getString(R.string.real_apps_scores));
        BarDataSet set2 = new BarDataSet(new ArrayList<>(), getString(R.string.synthetic_tests_scores));
        List<String> devices = new ArrayList<>();

        set1.setColor(Color.RED);
        set2.setColor(Color.BLUE);

        for (int i = 0; i < results.size(); ++i) {
            devices.add(results.get(i).getDeviceModel());

            /* Synthetic testing results */
            if (results.get(i).getBenchmarkType().equals(Constants.SYNTHETIC_TESTING_BENCHMARK))
                set2.addEntry(new BarEntry(i, results.get(i).getScore()));
            else /* Apps testing results */
                set1.addEntry(new BarEntry(i, results.get(i).getScore()));
        }

        barChart.setData(new BarData(set1, set2));
        barChart.getXAxis().setValueFormatter(new ChartXAxisFormatter(devices));
        barChart.getXAxis().setLabelCount(devices.size());
        barChart.animateY(Constants.CHART_ANIMATION_AXIS);
        barChart.notifyDataSetChanged();
        barChart.invalidate();
    }
}
