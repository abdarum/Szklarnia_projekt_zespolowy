package com.example.szklarniaparapetowa;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class HumidityFragmentView extends Fragment
{
    private GraphView graph;
    protected Context mContext;

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
            dataPoint[countData] = new DataPoint(countData, data.getDouble(0));
            countData++;
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoint);

        graph.addSeries(series);
        graph.getViewport().setScalable(true);
    }
}
