package com.example.bmi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrackerFragment extends Fragment {

    private ProgressBar progressBar;
    private LineChart lineChart;
    private TextView tvSummary, tvNoData;
    private RecyclerView recyclerView;
    private BmiHistoryAdapter adapter;
    private List<BmiHistoryItem> historyList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracker, container, false);

        progressBar = view.findViewById(R.id.progress_bar);
        lineChart   = view.findViewById(R.id.line_chart);
        tvSummary   = view.findViewById(R.id.tv_summary);
        tvNoData    = view.findViewById(R.id.tv_no_data);
        recyclerView = view.findViewById(R.id.rv_history);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BmiHistoryAdapter(historyList);
        recyclerView.setAdapter(adapter);

        MaterialButton btnWorkout = view.findViewById(R.id.btn_view_workout);
        btnWorkout.setOnClickListener(v -> {
            String category = historyList.isEmpty() ? "" :
                historyList.get(historyList.size() - 1).getCategory();
            Intent intent = new Intent(getActivity(), WorkoutActivity.class);
            intent.putExtra("bmi_category", category);
            startActivity(intent);
        });

        loadHistory();
        return view;
    }

    private void loadHistory() {
        if (getContext() == null) return;
        SharedPreferences prefs = requireContext().getSharedPreferences("BMI_PREFS", android.content.Context.MODE_PRIVATE);
        String uid = prefs.getString("user_uid", null);
        if (uid == null) {
            showNoData();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("users").child(uid).child("bmi_history");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.GONE);
                historyList.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    String date = child.getKey();
                    Double bmiVal = child.child("bmi").getValue(Double.class);
                    String category = child.child("category").getValue(String.class);
                    Long timestamp = child.child("timestamp").getValue(Long.class);
                    if (bmiVal != null && category != null && date != null) {
                        historyList.add(new BmiHistoryItem(
                                date, bmiVal.floatValue(), category,
                                timestamp != null ? timestamp : 0L));
                    }
                }
                Collections.sort(historyList, (a, b) -> a.getDate().compareTo(b.getDate()));
                adapter.notifyDataSetChanged();

                if (historyList.isEmpty()) {
                    showNoData();
                } else {
                    tvNoData.setVisibility(View.GONE);
                    lineChart.setVisibility(View.VISIBLE);
                    tvSummary.setVisibility(View.VISIBLE);
                    setupChart();
                    setupSummary();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                showNoData();
            }
        });
    }

    private void showNoData() {
        if (getView() == null) return;
        tvNoData.setVisibility(View.VISIBLE);
        lineChart.setVisibility(View.GONE);
        tvSummary.setVisibility(View.GONE);
    }

    private void setupChart() {
        List<Entry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        for (int i = 0; i < historyList.size(); i++) {
            BmiHistoryItem item = historyList.get(i);
            entries.add(new Entry(i, item.getBmi()));
            labels.add(item.getDate().length() >= 10 ? item.getDate().substring(5) : item.getDate());
        }

        LineDataSet dataSet = new LineDataSet(entries, "BMI");
        dataSet.setColor(Color.parseColor("#006a2b"));
        dataSet.setCircleColor(Color.parseColor("#006a2b"));
        dataSet.setLineWidth(2.5f);
        dataSet.setCircleRadius(5f);
        dataSet.setValueTextSize(10f);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.parseColor("#72fe8f"));
        dataSet.setFillAlpha(40);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        lineChart.setBackgroundColor(Color.parseColor("#FFFFFF"));
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.parseColor("#595c5b"));
        xAxis.setDrawGridLines(false);

        lineChart.getAxisLeft().setTextColor(Color.parseColor("#595c5b"));
        lineChart.getAxisLeft().setGridColor(Color.parseColor("#e7e8e8"));
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.animateX(800);
        lineChart.invalidate();
    }

    private void setupSummary() {
        if (historyList.isEmpty()) return;
        BmiHistoryItem last = historyList.get(historyList.size() - 1);
        String text = "Соңғы BMI: " + String.format("%.1f", last.getBmi()) + "  •  " + last.getCategory();
        if (historyList.size() >= 2) {
            BmiHistoryItem prev = historyList.get(historyList.size() - 2);
            if (last.getBmi() > prev.getBmi()) text += "  ↑";
            else if (last.getBmi() < prev.getBmi()) text += "  ↓";
        }
        tvSummary.setText(text);
    }
}
