package com.example.dht22_v2;

import android.util.Log;

import com.example.dht22_v2.data.model.Feed;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TSData {

    private static final String LOG_TAG = TSData.class.getName();

    private Double mTemperature;
    private Double mHumidity;
    private LineGraphSeries<DataPoint> mTemperatureSeries;
    private LineGraphSeries<DataPoint> mHumiditySeries;

    public TSData(List<Feed> feeds){
        mTemperature = Double.parseDouble(feeds.get(feeds.size()-1).getField1());
        mHumidity = Double.parseDouble(feeds.get(feeds.size()-1).getField2());

        mTemperatureSeries = new LineGraphSeries<DataPoint>();
        mHumiditySeries = new LineGraphSeries<DataPoint>();

        Date date = new Date();
        for (int i=0; i<feeds.size(); i++){
            if (feeds.get(i).getField1()!=null && feeds.get(i).getField2()!=null){
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(feeds.get(i).getCreatedAt());
                    // + 7.2e+6 because of GMT+2 zone
                    date = new Date( (long)(date.getTime()+ 7.2*Math.pow(10,6)));
                } catch (Exception ex) {
                    Log.i(LOG_TAG,"Error parsing the date");
                }
                mTemperatureSeries.appendData(new DataPoint(date, Double.parseDouble(feeds.get(i).getField1())), true, feeds.size());
                mHumiditySeries.appendData(new DataPoint(date, Double.parseDouble(feeds.get(i).getField2())), true, feeds.size());
            }
        }
    }



    public Double getTemperature(){
        return mTemperature;
    }

    public Double getHumidity(){
        return mHumidity;
    }

    public LineGraphSeries<DataPoint> getTemperatureSeries(){
        return mTemperatureSeries;
    }

    public LineGraphSeries<DataPoint> getHumiditySeries(){
        return mHumiditySeries;
    }

    public double getMinDate() {
        return getTemperatureSeries().getLowestValueX();
    }

    public double getMaxDate() {
        return getTemperatureSeries().getHighestValueX();
    }
}
