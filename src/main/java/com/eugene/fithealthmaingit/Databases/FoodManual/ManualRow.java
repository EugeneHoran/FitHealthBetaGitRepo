package com.eugene.fithealthmaingit.Databases.FoodManual;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.eugene.fithealthmaingit.R;

public class ManualRow extends LinearLayout {
    Context mContext;
    LogManual mLog;

    public ManualRow(Context context) {
        super(context);
        mContext = context;
        setup();
    }

    public ManualRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setup();
    }

    private void setup() {
        LayoutInflater inflater1 = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater1.inflate(R.layout.list_manaual, this);
    }


    public void setLog(LogManual log) {
        mLog = log;
    }
}