package com.example.dht22_v2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


public class StatusFragment extends Fragment{

    public static final String LOG_TAG = StatusFragment.class.getName();

    private View rootView;
    private TextView mTemperatureTextView;
    private TextView mHumidityTextView;
    private ProgressBar mTemperatureProgressBar;
    private ProgressBar mHumidityProgressBar;


    public StatusFragment(){
        // Required empty public constructor
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_status, container, false);

        mTemperatureTextView = rootView.findViewById(R.id.tvTemperature);
        mHumidityTextView = rootView.findViewById(R.id.tvHumidity);
        mTemperatureProgressBar = rootView.findViewById(R.id.pbTemperature);
        mHumidityProgressBar = rootView.findViewById(R.id.pbHumidity);

        final StatusFragmentViewModel model = ViewModelProviders.of(this).get(StatusFragmentViewModel.class);
        final LiveData<TSData> data = model.getLoadedData();

        Log.i(LOG_TAG, "dataList = "+data);

        data.observe(this, new Observer<TSData>() {
            @Override
            public void onChanged(TSData tsdata) {
                mTemperatureProgressBar.setVisibility(View.GONE);
                mHumidityProgressBar.setVisibility(View.GONE);

                mTemperatureTextView.setText(""+tsdata.getTemperature()+"°C");
                mHumidityTextView.setText(""+tsdata.getHumidity()+"%");

                //Snackbar.make(rootView, "Data updated", Snackbar.LENGTH_SHORT).show();

                Log.i(LOG_TAG, "LiveData updated in UI");
            }
        });

        model.getDataFromTS();

        return rootView;
    }

}
