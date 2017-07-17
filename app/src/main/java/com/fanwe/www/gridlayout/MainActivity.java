package com.fanwe.www.gridlayout;

import android.os.Bundle;

import com.fanwe.library.SDLibrary;
import com.fanwe.library.activity.SDBaseActivity;
import com.fanwe.library.gridlayout.SDGridLayout;
import com.fanwe.library.listener.SDSimpleIterateCallback;
import com.fanwe.library.model.SelectableModel;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.www.gridlayout.adapter.ListViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends SDBaseActivity
{

    private SDGridLayout view_grid;
    private List<SelectableModel> mListModel = new ArrayList<>();

    @Override
    protected void init(Bundle savedInstanceState)
    {
        SDLibrary.getInstance().init(getApplication());
        setContentView(R.layout.activity_main);
        view_grid = (SDGridLayout) findViewById(R.id.view_grid);

        SDCollectionUtil.foreach(10, new SDSimpleIterateCallback()
        {
            @Override
            public boolean next(int i)
            {
                mListModel.add(new SelectableModel());
                return false;
            }
        });
        ListViewAdapter adapter = new ListViewAdapter(mListModel, this);


        view_grid.setColumnCount(3);
        view_grid.setVerticalSpacing(10);
        view_grid.setHorizontalSpacing(10);
        view_grid.setAdapter(adapter);
    }
}
