package com.example.szklarniaparapetowa;

import android.database.Cursor;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity
{
    private TabLayout tablayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tablayout = findViewById(R.id.tablayout_id);
        viewPager = findViewById(R.id.viewpager_id);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(new temperatureFragmentView(), getString(R.string.temperature));

        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);
    }


    private void putDataOnGraph()
    {
        DatabaseHelper databaseHelper = new DatabaseHelper(this.getApplicationContext());
        Cursor data = databaseHelper.getData("TEMPERATURE");


    }
}
