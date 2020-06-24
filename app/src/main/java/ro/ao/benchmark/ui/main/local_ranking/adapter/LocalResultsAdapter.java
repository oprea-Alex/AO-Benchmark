package ro.ao.benchmark.ui.main.local_ranking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import ro.ao.benchmark.R;
import ro.ao.benchmark.model.benchmark.LocalBenchmarkResult;

public class LocalResultsAdapter extends RecyclerView.Adapter<LocalResultsAdapter.LocalResultViewHolder> {

    private Context context;
    private List<LocalBenchmarkResult> results;

    public LocalResultsAdapter(Context context, List<LocalBenchmarkResult> results) {
        this.context = context;
        this.results = results;
    }

    @NonNull
    @Override
    public LocalResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_local_result,
                parent,
                false
        );

        return new LocalResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocalResultViewHolder holder, int position) {
        /* Update item view */
        LocalBenchmarkResult appInfo = results.get(position);
        holder.updateView(appInfo);
    }

    @Override
    public int getItemCount() {
        return results.size();
    }


    class LocalResultViewHolder extends RecyclerView.ViewHolder {

        private TextInputEditText scoreEditText;
        private TextInputEditText descriptionEditText;
        private TextInputEditText dateEditText;


        public LocalResultViewHolder(@NonNull View itemView) {
            super(itemView);
            scoreEditText = itemView.findViewById(R.id.scoreEditText);
            descriptionEditText = itemView.findViewById(R.id.descriptionEditText);
            dateEditText = itemView.findViewById(R.id.dateEditText);

        }

        public void updateView(LocalBenchmarkResult appInfo) {
            scoreEditText.setText(String.valueOf(appInfo.getScore()));
            descriptionEditText.setText(appInfo.getDescription());
            dateEditText.setText(appInfo.getDate());
        }
    }
}
