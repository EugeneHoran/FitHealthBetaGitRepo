package com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogWeight;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eugene.fithealthmaingit.R;

import java.text.DecimalFormat;

public class WeightRow extends LinearLayout {
    Context mContext;
    WeightLog mLog;

    public WeightRow(Context context) {
        super(context);
        mContext = context;
        setup();
    }

    public WeightRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setup();
    }

    private void setup() {
        LayoutInflater inflater1 = (LayoutInflater) mContext
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater1.inflate(R.layout.list_weight_row, this);
    }

    DecimalFormat df = new DecimalFormat("0");
    DateFormat dateFormat = new DateFormat();

    public void setLog(WeightLog log) {
        mLog = log;
        TextView currentWeight = (TextView) findViewById(R.id.weightCurrent);
        currentWeight.setText(df.format(mLog.getCurrentWeight()) + " Pounds");
        TextView date = (TextView) findViewById(R.id.weightDate);
        date.setText(dateFormat.format("MMM dd, yyyy", mLog.getDate()));
    }
}