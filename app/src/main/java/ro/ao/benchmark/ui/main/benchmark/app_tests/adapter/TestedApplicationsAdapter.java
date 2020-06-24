package ro.ao.benchmark.ui.main.benchmark.app_tests.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

import ro.ao.benchmark.R;
import ro.ao.benchmark.model.benchmark.real_apps_testing.ItemAppInfo;
import ro.ao.benchmark.util.OSUtil;

public class TestedApplicationsAdapter extends RecyclerView.Adapter<TestedApplicationsAdapter.TestedApplicationViewHolder> {

    private Context context;
    private List<ItemAppInfo> testedAppsList;
    private Map<String, Long> launchedTimes;

    public TestedApplicationsAdapter(Context context, List<ItemAppInfo> testedAppsList, Map<String, Long> launchedTimes) {
        this.context = context;
        this.testedAppsList = testedAppsList;
        this.launchedTimes = launchedTimes;
    }


    @NonNull
    @Override
    public TestedApplicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_app_info_test,
                parent,
                false
        );

        return new TestedApplicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestedApplicationViewHolder holder, int position) {
        ItemAppInfo appInfo = testedAppsList.get(position);
        String pckgName = appInfo.getPackageName();
        Long launchTime = launchedTimes.get(pckgName);
        holder.updateView(pckgName, launchTime);
    }

    @Override
    public int getItemCount() {
        return this.testedAppsList.size();
    }

    class TestedApplicationViewHolder extends RecyclerView.ViewHolder {
        private TextView packageNameTextView, launchTimeTextView;
        private ImageView iconImageView;

        TestedApplicationViewHolder(@NonNull View itemView) {
            super(itemView);
            packageNameTextView = itemView.findViewById(R.id.testedAppNameTextView);
            launchTimeTextView = itemView.findViewById(R.id.launchTimeTextView);
            iconImageView = itemView.findViewById(R.id.testedAppIconImageView);
        }

        void updateView(String appPackageName, Long launchTime) {
            packageNameTextView.setText(OSUtil.getApplicationName(context, appPackageName));
            launchTimeTextView.setText(launchTime + " ms");
            iconImageView.setImageDrawable(OSUtil.getApplicationIcon(context, appPackageName));
        }
    }

}
