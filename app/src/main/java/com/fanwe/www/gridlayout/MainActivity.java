package com.fanwe.www.gridlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fanwe.library.gridlayout.SDGridLayout;

public class MainActivity extends AppCompatActivity
{

    private SDGridLayout view_grid;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view_grid = (SDGridLayout) findViewById(R.id.view_grid);

        view_grid.setColumnCount(3);
        view_grid.setVerticalSpacing(10);
        view_grid.setHorizontalSpacing(10);
    }
}
