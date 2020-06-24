package ro.ao.benchmark.ui.main.benchmark.synthetic_tests.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import ro.ao.benchmark.R;
import ro.ao.benchmark.model.benchmark.synthetic_testing.SyntheticTestResult;

public class SyntheticTestsAdapter extends RecyclerView.Adapter<SyntheticTestsAdapter.SyntheticTestViewHolder> {

    private Context context;
    private List<SyntheticTestResult> testsList;

    public SyntheticTestsAdapter(Context context, List<SyntheticTestResult> testsList) {
        this.context = context;
        this.testsList = testsList;
    }

    public long getTotalTimeSpent() {
        long time = 0;
        for (SyntheticTestResult test : testsList)
            time += test.getSpentTime();

        return time;
    }

    public double calculateTotalScore(long timeSpent, double batteryDrain) {
        return (1.0 / (double) timeSpent) * (1.0 / Math.max(1, batteryDrain)) * 10000000000.0;
    }

    @NonNull
    @Override
    public SyntheticTestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_synthetic_test_info,
                parent,
                false
        );

        return new SyntheticTestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SyntheticTestViewHolder holder, int position) {
        SyntheticTestResult test = testsList.get(position);
        holder.updateView(
                test.getTest().getDetails(),
                test.getDescription(),
                String.valueOf(test.getSpentTime()));
    }

    @Override
    public int getItemCount() {
        return testsList.size();
    }

    static class SyntheticTestViewHolder extends RecyclerView.ViewHolder {
        private TextView testTypeTextView;
        private TextInputEditText spentTimeEditText, descriptionEditText;

        public void updateView(String test, String description, String spentTime) {
            testTypeTextView.setText(test);
            descriptionEditText.setText(description);
            spentTimeEditText.setText(spentTime);
        }

        SyntheticTestViewHolder(@NonNull View itemView) {
            super(itemView);
            testTypeTextView = itemView.findViewById(R.id.testTypeTextView);
            descriptionEditText = itemView.findViewById(R.id.testDescriptionEditText);
            spentTimeEditText = itemView.findViewById(R.id.testSpentTimeEditText);
        }
    }
}
