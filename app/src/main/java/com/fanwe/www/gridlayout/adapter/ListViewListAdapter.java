package com.fanwe.www.gridlayout.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.library.adapter.SDSimpleAdapter;
import com.fanwe.lib.gridlayout.FGridLayout;
import com.fanwe.www.gridlayout.DataModel;
import com.fanwe.www.gridlayout.R;

import java.util.List;

/**
 * Created by Administrator on 2017/7/18.
 */

public class ListViewListAdapter extends SDSimpleAdapter<List<DataModel>>
{
    public ListViewListAdapter(List<List<DataModel>> listModel, Activity activity)
    {
        super(listModel, activity);
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent)
    {
        return R.layout.item_listview_list;
    }

    @Override
    public void bindData(int position, View convertView, ViewGroup parent, List<DataModel> model)
    {
        FGridLayout view_grid = get(R.id.view_grid, convertView);
        view_grid.setSpanCount(3);
        view_grid.setVerticalSpacing(10);
        view_grid.setHorizontalSpacing(10);
        ListViewAdapter adapter = new ListViewAdapter(model, getActivity());
        view_grid.setAdapter(adapter);
    }
}
