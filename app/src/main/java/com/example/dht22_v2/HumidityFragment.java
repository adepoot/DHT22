package com.example.dht22_v2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.SimpleDateFormat;
import java.util.Date;


public class HumidityFragment extends Fragment{

    private static final String LOG_TAG = HumidityFragment.class.getName();

    private View rootView;
    private TextView mNowTextView;
    private ProgressBar mNowProgressBar;
    private ProgressBar mGraphProgressBar;
    private GraphView mGraphGraphView;

    public HumidityFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_details, container, false);

        mNowTextView = rootView.findViewById(R.id.tvNow);
        mNowProgressBar = rootView.findViewById(R.id.pbNow);
        mGraphProgressBar = rootView.findViewById(R.id.pbGraph);
        mGraphGraphView = rootView.findViewById(R.id.gvGraph);

        final StatusFragmentViewModel model = ViewModelProviders.of(this).get(StatusFragmentViewModel.class);
        final LiveData<TSData> data = model.getLoadedData();

        data.observe(this, new Observer<TSData>() {
            @Override
            public void onChanged(TSData tsdata) {
                mNowProgressBar.setVisibility(View.GONE);
                mGraphProgressBar.setVisibility(View.GONE);

                mNowTextView.setText(""+tsdata.getHumidity()+"%");

                buildGraph(tsdata);

                //Snackbar.make(rootView, "Data updated", Snackbar.LENGTH_SHORT).show();

                Log.i(LOG_TAG, "LiveData updated in UI");
            }
        });
        model.getDataFromTS();

        return rootView;
    }

    private void buildGraph(TSData tsdata){

        mGraphGraphView.addSeries(tsdata.getHumiditySeries());
        tsdata.getTemperatureSeries().setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Date date = new Date((long)dataPoint.getX());
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH'h'mm");
                Toast.makeText(getActivity(), sdf.format(date) + " - " + dataPoint.getY() + "%", Toast.LENGTH_SHORT).show();
            }
        });

        // Custom label formatter for X and Y values
        mGraphGraphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    Date date = new Date((long)value);
                    SimpleDateFormat sdf = new SimpleDateFormat("HH'h'mm");
                    return sdf.format(date);
                } else {
                    // show currency for y values
                    return super.formatLabel(value, isValueX) + "%";
                }
            }
        });
        mGraphGraphView.getGridLabelRenderer().setNumHorizontalLabels(3);

        // set manual x bounds to have nice steps
        mGraphGraphView.getViewport().setMinX(tsdata.getMinDate());
        mGraphGraphView.getViewport().setMaxX(tsdata.getMaxDate());
        mGraphGraphView.getViewport().setXAxisBoundsManual(true);

        // activate horizontal zooming and scrolling
        mGraphGraphView.getViewport().setScalable(true);

        // activate horizontal scrolling
        mGraphGraphView.getViewport().setScrollable(true);

        // activate horizontal and vertical zooming and scrolling
        mGraphGraphView.getViewport().setScalableY(true);

        // activate vertical scrolling
        mGraphGraphView.getViewport().setScrollableY(true);
    }
}