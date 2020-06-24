package ro.ao.benchmark.ui.main.benchmark.app_tests.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro.ao.benchmark.R;
import ro.ao.benchmark.model.benchmark.real_apps_testing.ItemAppInfo;
import ro.ao.benchmark.util.OSUtil;

public class ApplicationsAdapter extends RecyclerView.Adapter<ApplicationsAdapter.ApplicationViewHolder> {

    private Context context;
    private List<ItemAppInfo> appsList;
    private Map<ItemAppInfo, Boolean> checkedApps = new HashMap<>();

    public ApplicationsAdapter(Context context, List<ItemAppInfo> appsList) {
        this.context = context;
        this.appsList = appsList;
    }

    @NonNull
    @Override
    public ApplicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_app_info,
                parent,
                false
        );

        return new ApplicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationViewHolder holder, int position) {

        /* Update item view */
        ItemAppInfo appInfo = appsList.get(position);
        boolean isAppChecked = false;
        if (checkedApps.containsKey(appInfo))
            isAppChecked = checkedApps.get(appInfo);
        holder.updateView(appInfo, isAppChecked);

        /* Add checkbox listener */
        holder.appCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checkedApps.put(appsList.get(holder.getAdapterPosition()), isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return appsList.size();
    }

    public List<ItemAppInfo> getCheckedApps() {
        List<ItemAppInfo> checkedApplications = new ArrayList<>();

        for (Map.Entry<ItemAppInfo, Boolean> entry : checkedApps.entrySet())
            if (entry.getValue()) // is checked
                checkedApplications.add(entry.getKey());

            return checkedApplications;
    }

    class ApplicationViewHolder extends RecyclerView.ViewHolder {
        private TextView packageNameTextView;
        private ImageView iconImageview;
        private AppCompatCheckBox appCheckBox;

        void updateView(ItemAppInfo app, boolean isChecked) {
            packageNameTextView.setText(app.getName());
            iconImageview.setImageDrawable(OSUtil.getApplicationIcon(context, app.getPackageName()));
            appCheckBox.setChecked(isChecked);
        }

        ApplicationViewHolder(@NonNull View itemView) {
            super(itemView);
            packageNameTextView = itemView.findViewById(R.id.appNameTextView);
            iconImageview = itemView.findViewById(R.id.appIconImageView);
            appCheckBox = itemView.findViewById(R.id.appCheckBox);
        }
    }
}
