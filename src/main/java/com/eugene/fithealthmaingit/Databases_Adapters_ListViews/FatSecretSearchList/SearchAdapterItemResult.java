package com.eugene.fithealthmaingit.Databases_Adapters_ListViews.FatSecretSearchList;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eugene.fithealthmaingit.R;

import java.util.ArrayList;

public class SearchAdapterItemResult extends ArrayAdapter<SearchItemResult> {

    private final Context mContext;
    private final ArrayList<SearchItemResult> mItem;

    public SearchAdapterItemResult(Context context, ArrayList<SearchItemResult> itemsArrayList) {
        super(context, R.layout.list_search_row, itemsArrayList);
        this.mContext = context;
        this.mItem = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.list_search_row, parent, false);
        TextView mFoodName = (TextView) v.findViewById(R.id.food_name);
        TextView mBrand = (TextView) v.findViewById(R.id.food_brand);
        mFoodName.setText(mItem.get(position).getTitle());
        mBrand.setText(mItem.get(position).getBrand());
        RelativeLayout back = (RelativeLayout) v.findViewById(R.id.list_back);
        return v;
    }

}
