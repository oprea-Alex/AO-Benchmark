package ro.ao.benchmark.ui.main.local_ranking;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ro.ao.benchmark.R;
import ro.ao.benchmark.ui.BaseFragment;
import ro.ao.benchmark.ui.main.local_ranking.adapter.LocalResultsAdapter;
import ro.ao.benchmark.util.Constants;

public class LocalRankingFragment extends BaseFragment {

    private LocalRankingViewModel viewModel;

    private View rootView;
    private ProgressBar progressBar;
    private RecyclerView resultsRecyclerView;
    private LocalResultsAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_local_ranking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(Constants.TAG_LOCAL_RANKING, "ON CREATE");

        rootView = view;
        config();

        viewModel = ViewModelProviders.of(this).get(LocalRankingViewModel.class);
        observeViewModel();
    }

    private void config() {
        progressBar = rootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        resultsRecyclerView = rootView.findViewById(R.id.resultsRecyclerView);
        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        resultsRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }

    private void observeViewModel() {
        viewModel.getExceptionLiveData().observe(getViewLifecycleOwner(), e -> {
            progressBar.setVisibility(View.GONE);
            displayAlert(getString(R.string.retrieve_local_scores_failed), e.getMessage());
        });
        viewModel.getAllResults().observe(getViewLifecycleOwner(), results -> {
            Log.d(Constants.TAG_LOCAL_RANKING, results.toString());
            progressBar.setVisibility(View.GONE);
            adapter = new LocalResultsAdapter(getActivity(), results);
            resultsRecyclerView.setAdapter(adapter);
        });
    }
}
