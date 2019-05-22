package com.example.szklarniaparapetowa;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity
{
    private TabLayout tablayout;
    private ViewPager viewPager;
    JsonHandling json;
    DatabaseHelper db = new DatabaseHelper(this);
    String jsonStr;
    public static Context mycontext;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mycontext = getApplicationContext();

        tablayout = findViewById(R.id.tablayout_id);
        viewPager = findViewById(R.id.viewpager_id);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(new TemperatureFragmentView(), getString(R.string.temperature));
        adapter.AddFragment(new LightFragmentView(), getString(R.string.light));
        adapter.AddFragment(new HumidityFragmentView(), getString(R.string.humidity));

        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);


        json = new JsonHandling();
        db.removeData();
        try {
            //jsonStr = json.execute().get();
            json.getData(jsonStr = json.execute().get());
        }catch(Exception e){}
    }

}
