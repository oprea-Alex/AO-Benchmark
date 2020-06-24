package ro.ao.benchmark.ui.main.global_ranking.adapter;

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.List;

public class ChartXAxisFormatter extends IndexAxisValueFormatter {
    public ChartXAxisFormatter(List<String> devices) {
        super(devices);
    }
}
