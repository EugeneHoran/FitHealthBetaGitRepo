package com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogWeight;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eugene.fithealthmaingit.R;

import java.text.DecimalFormat;
import java.util.List;

public class WeightLogAdapter extends ArrayAdapter<WeightLog> {
    Context mContext;
    public static List<WeightLog> mLogs;

    public WeightLogAdapter(Context context, int textViewResourceId, List<WeightLog> logs) {
        super(context, textViewResourceId);
        mContext = context;
        mLogs = logs;
    }

    public void setLogs(List<WeightLog> logs) {
        mLogs = logs;
    }

    public List<WeightLog> getLogs() {
        return mLogs;
    }

    public void add(WeightLog log) {
        mLogs.add(log);
    }

    public void remove(WeightLog log) {
        WeightLogAdapter.mLogs.remove(log);
    }

    public int getCount() {
        return mLogs.size();
    }

    public WeightLog getItem(int position) {
        return mLogs.get(position);
    }

    DecimalFormat df = new DecimalFormat("0");

    public View getView(int position, View convertView, ViewGroup parent) {
        WeightRow view = (WeightRow) convertView;
        if (view == null) {
            view = new WeightRow(mContext);
        }
        WeightLog log = getItem(position);
        view.setLog(log);
        TextView textView = (TextView) view.findViewById(R.id.weightStart);
        if (position == 0) {
            textView.setText("Start Weight");
            textView.setTextColor(mContext.getResources().getColor(R.color.text_color));
        } else {
            double lastWeight = mLogs.get(position - 1).getCurrentWeight() - mLogs.get(position).getCurrentWeight();
            if (mLogs.get(position - 1).getCurrentWeight() < mLogs.get(position).getCurrentWeight()) {
                textView.setText("Gained " + df.format(lastWeight * -1) + " Lbs");
                textView.setTextColor(mContext.getResources().getColor(R.color.primary_dark));
            }
            if (mLogs.get(position - 1).getCurrentWeight() > mLogs.get(position).getCurrentWeight()) {
                textView.setText("Lost " + df.format(lastWeight) + " Lbs");
                textView.setTextColor(mContext.getResources().getColor(R.color.primary));
            }
            if (mLogs.get(position - 1).getCurrentWeight() == mLogs.get(position).getCurrentWeight()) {
                textView.setText("Maintained");
                textView.setTextColor(mContext.getResources().getColor(R.color.accent));
            }
        }
        LinearLayout circle = (LinearLayout) view.findViewById(R.id.circle1);
        circle.bringToFront();
        if (position == 0) {
            LinearLayout topLine = (LinearLayout) view.findViewById(R.id.topLine);
            topLine.setVisibility(View.INVISIBLE);
        } else {
            LinearLayout topLine = (LinearLayout) view.findViewById(R.id.topLine);
            topLine.setVisibility(View.VISIBLE);
        }
        if (position + 1 == getCount()) {
            LinearLayout bottomLine = (LinearLayout) view.findViewById(R.id.bottomLine);
            bottomLine.setVisibility(View.INVISIBLE);
        } else {
            LinearLayout bottomLine = (LinearLayout) view.findViewById(R.id.bottomLine);
            bottomLine.setVisibility(View.VISIBLE);
        }
        TextView weightPosition = (TextView) view.findViewById(R.id.weightPosition);
        weightPosition.setText(position + 1 + "");
        return view;
    }
}
