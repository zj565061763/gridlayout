package com.fanwe.www.gridlayout;

import android.os.Bundle;

import com.fanwe.library.SDLibrary;
import com.fanwe.library.activity.SDBaseActivity;
import com.fanwe.library.gridlayout.SDGridLayout;

public class MainActivity extends SDBaseActivity
{

    private SDGridLayout view_grid;

    @Override
    protected void init(Bundle savedInstanceState)
    {
        SDLibrary.getInstance().init(getApplication());
        setContentView(R.layout.activity_main);
        view_grid = (SDGridLayout) findViewById(R.id.view_grid);

        view_grid.setColumnCount(3);
        view_grid.setVerticalSpacing(10);
        view_grid.setHorizontalSpacing(10);
    }
}
