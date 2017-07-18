package com.fanwe.www.gridlayout;

import android.os.Bundle;
import android.widget.GridView;

import com.fanwe.library.SDLibrary;
import com.fanwe.library.activity.SDBaseActivity;
import com.fanwe.library.gridlayout.SDGridLayout;
import com.fanwe.library.listener.SDSimpleIterateCallback;
import com.fanwe.library.model.SelectableModel;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.www.gridlayout.adapter.ListViewAdapter;
import com.fanwe.www.gridlayout.adapter.ListViewListAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends SDBaseActivity
{

    private GridView view_grid;

    @Override
    protected void init(Bundle savedInstanceState)
    {
        SDLibrary.getInstance().init(getApplication());
        setContentView(R.layout.activity_main);
        view_grid = (GridView) findViewById(R.id.view_grid);

        final List<List<DataModel>> listModel = new ArrayList<>();

        SDCollectionUtil.foreach(30, new SDSimpleIterateCallback()
        {
            @Override
            public boolean next(int i)
            {
                listModel.add(DataModel.get(10));
                return false;
            }
        });
        ListViewListAdapter adapter = new ListViewListAdapter(listModel, this);


        view_grid.setNumColumns(1);
        view_grid.setVerticalSpacing(10);
        view_grid.setHorizontalSpacing(10);
        view_grid.setAdapter(adapter);
    }
}
