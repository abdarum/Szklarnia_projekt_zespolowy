package com.example.szklarniaparapetowa;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HumidityFragmentView extends Fragment
{
    private GraphView graph;
    protected Context mContext;
    private final String TAG = "HumFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }



    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        super.onAttach(context);
        mContext = context;

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        View view =  inflater.inflate(R.layout.humidity_layout, container, false);

        graph = view.findViewById(R.id.graph);
        putDataOnGraph();

        MultiStateToggleButton button = view.findViewById(R.id.toggleButton);
        button.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int position) {
                Log.d(TAG, "Position: " + position);
            }
        });
        button.setValue(0);

        return view;
    }

    /**
     * Take data from database and put it on graph
     */
    private void putDataOnGraph()
    {
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        Cursor data = databaseHelper.getData("HUMIDITY");


        DataPoint[] dataPoint = new DataPoint[data.getCount()];
        int countData = 0;

        while(data.moveToNext())
        {
            String s= data.getString(0);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMAN);
            try {
                Date d=  dateFormat.parse(s);
                dataPoint[countData] = new DataPoint(d, data.getDouble(1));
            } catch (ParseException e) {
                e.printStackTrace();
            }


            countData++;
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoint);

        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX){
                if(isValueX)
                {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM HH:ss", Locale.GERMAN);
                    return sdf.format(new Date((long)value));
                }else{
                    return super.formatLabel(value, isValueX);
                }
            }
        });
        graph.getGridLabelRenderer().setHorizontalLabelsAngle(90);
        graph.addSeries(series);
        graph.getViewport().setScalable(true);
    }
}
