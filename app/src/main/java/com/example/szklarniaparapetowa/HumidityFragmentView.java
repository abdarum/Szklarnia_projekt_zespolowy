package com.example.szklarniaparapetowa;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
    private boolean mControl = false;
    private boolean mControlHumiditi = false;
    private boolean mControlTemp = false;
    private boolean mControlLight = false;

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


        final Button controlButton = view.findViewById(R.id.controlButton);
        final Button controlLightButton = view.findViewById(R.id.controlLightButton);
        final Button controlHumidityButton = view.findViewById(R.id.controlHumidityButton);
        final Button controlTempButton = view.findViewById(R.id.controlTempButton);

        if(mControl){
            controlButton.setBackgroundColor(Color.GREEN);
            if (mControlLight)
            {
                controlLightButton.setBackgroundColor(Color.GREEN);
            } else
            {
                controlLightButton.setBackgroundColor(Color.RED);
            }

            if (mControlHumiditi)
            {
                controlHumidityButton.setBackgroundColor(Color.GREEN);
            } else
            {
                controlHumidityButton.setBackgroundColor(Color.RED);
            }

            if (mControlTemp)
            {
                controlTempButton.setBackgroundColor(Color.GREEN);
            } else
            {
                controlTempButton.setBackgroundColor(Color.RED);
            }

        }
        else{
            controlButton.setBackgroundColor(Color.RED);
            controlLightButton.setBackgroundColor(Color.GRAY);
            controlHumidityButton.setBackgroundColor(Color.GRAY);
            controlTempButton.setBackgroundColor(Color.GRAY);

        }


        controlButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    if(mControl){
                        JsonHandling json = new JsonHandling();
                        json.setExecutorUrl("auto=true");
                        Log.e("URL_TEST", "true start");
                        String tmp = json.execute().get();
                        Log.e("URL_TEST", "true complete");
                        mControl = false;
                        mControlHumiditi = false;
                        mControlTemp = false;
                        mControlLight = false;

                        controlButton.setBackgroundColor(Color.RED);
                        controlLightButton.setBackgroundColor(Color.GRAY);
                        controlHumidityButton.setBackgroundColor(Color.GRAY);
                        controlTempButton.setBackgroundColor(Color.GRAY);

                    }
                    else{
                        JsonHandling json = new JsonHandling();
                        //HttpHandler sh = new HttpHandler();
                        //sh.makeServiceCall("http://projektz.heliohost.org/test/excitation.php?light=true");
                        json.setExecutorUrl("auto=false");
                        Log.e("URL_TEST", "false start");
                        String tmp = json.execute().get();
                        Log.e("URL_TEST", "false complete");
                        mControl = true;
                        controlButton.setBackgroundColor(Color.GREEN);

                        if (mControlLight)
                        {
                            controlLightButton.setBackgroundColor(Color.GREEN);
                        } else
                        {
                            controlLightButton.setBackgroundColor(Color.RED);
                        }

                        if (mControlHumiditi)
                        {
                            controlHumidityButton.setBackgroundColor(Color.GREEN);
                        } else
                        {
                            controlHumidityButton.setBackgroundColor(Color.RED);
                        }

                        if (mControlTemp)
                        {
                            controlTempButton.setBackgroundColor(Color.GREEN);
                        } else
                        {
                            controlTempButton.setBackgroundColor(Color.RED);
                        }
                    }
            }catch (Exception e) {
                    Log.e("URL_TEST", "test sens err");
                }
            }
        });

        controlLightButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    if(mControl)
                    {
                        if (mControlLight)
                        {
                            JsonHandling json = new JsonHandling();
                            //HttpHandler sh = new HttpHandler();
                            //sh.makeServiceCall("http://projektz.heliohost.org/test/excitation.php?light=true");
                            json.setExecutorUrl("light=false");
                            Log.e("URL_TEST", "false start");
                            String tmp = json.execute().get();
                            Log.e("URL_TEST", "false complete");
                            mControlLight = false;
                            controlLightButton.setBackgroundColor(Color.RED);
                        } else
                        {
                            JsonHandling json = new JsonHandling();
                            json.setExecutorUrl("light=true");
                            Log.e("URL_TEST", "true start");
                            String tmp = json.execute().get();
                            Log.e("URL_TEST", "true complete");
                            mControlLight = true;
                            controlLightButton.setBackgroundColor(Color.GREEN);
                        }
                    }
            }catch (Exception e) {
                    Log.e("URL_TEST", "test sens err");
                }
            }
        });

        controlHumidityButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    if(mControl)
                    {
                        if (mControlHumiditi)
                        {
                            JsonHandling json = new JsonHandling();
                            json.setExecutorUrl("cooling=false");
                            Log.e("URL_TEST", "false start");
                            String tmp = json.execute().get();
                            Log.e("URL_TEST", "false complete");
                            mControlHumiditi = false;
                            controlHumidityButton.setBackgroundColor(Color.RED);
                        } else
                        {
                            JsonHandling json = new JsonHandling();
                            json.setExecutorUrl("cooling=true");
                            Log.e("URL_TEST", "true start");
                            String tmp = json.execute().get();
                            Log.e("URL_TEST", "true complete");
                            mControlHumiditi = true;
                            controlHumidityButton.setBackgroundColor(Color.GREEN);
                        }
                    }
            }catch (Exception e) {
                    Log.e("URL_TEST", "test sens err");
                }
            }
        });

        controlTempButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    if(mControl)
                    {
                        if (mControlTemp)
                        {
                            JsonHandling json = new JsonHandling();
                            json.setExecutorUrl("cooling=false");
                            Log.e("URL_TEST", "false start");
                            String tmp = json.execute().get();
                            Log.e("URL_TEST", "false complete");
                            mControlTemp = false;
                            controlTempButton.setBackgroundColor(Color.RED);
                        } else
                        {
                            JsonHandling json = new JsonHandling();
                            json.setExecutorUrl("cooling=true");
                            Log.e("URL_TEST", "true start");
                            String tmp = json.execute().get();
                            Log.e("URL_TEST", "true complete");
                            mControlTemp = true;
                            controlTempButton.setBackgroundColor(Color.GREEN);
                        }
                    }
            }catch (Exception e) {
                    Log.e("URL_TEST", "test sens err");
                }
            }
        });
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
