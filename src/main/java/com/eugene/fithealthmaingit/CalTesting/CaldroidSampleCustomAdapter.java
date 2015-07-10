package com.eugene.fithealthmaingit.CalTesting;


import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.Utilities.DateCompare;
import com.roomorama.caldroid.CaldroidGridAdapter;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import hirondelle.date4j.DateTime;


public class CaldroidSampleCustomAdapter extends CaldroidGridAdapter {
    public CaldroidSampleCustomAdapter
            (Context context, int month, int year,
             HashMap<String, Object> caldroidData,
             HashMap<String, Object> extraData) {
        super(context, month, year, caldroidData, extraData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cellView = convertView;
        if (convertView == null) {
            cellView = inflater.inflate(R.layout.custom_cell, null);
        }
        int topPadding = cellView.getPaddingTop();
        int leftPadding = cellView.getPaddingLeft();
        int bottomPadding = cellView.getPaddingBottom();
        int rightPadding = cellView.getPaddingRight();

        TextView tv1 = (TextView) cellView.findViewById(R.id.tv1);
        TextView tv2 = (TextView) cellView.findViewById(R.id.tv2);

        // Get dateTime of this cell
        DateTime dateTime = this.datetimeList.get(position);
        int day1 = dateTime.getDay();

        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DAY_OF_MONTH, day1);
        calendar.set(Calendar.MONTH, dateTime.getMonth() - 1);
        calendar.set(Calendar.YEAR, dateTime.getYear());
        Date today = calendar.getTime();

        String yourCustomData2 = (String) extraData.get(DateCompare.dateToString(today));
        if (yourCustomData2 != null) {
            tv2.setText(yourCustomData2);
            tv2.setBackgroundColor(context.getResources().getColor(R.color.accent_trans));
            tv2.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            tv2.setText("0");
        }
        tv1.setText("" + dateTime.getDay());


        Resources resources = context.getResources();
        if (dateTime.getMonth() != month) {
            tv1.setTextColor(resources.getColor(com.caldroid.R.color.caldroid_gray));
            tv2.setTextColor(resources.getColor(com.caldroid.R.color.caldroid_gray));
        }
        cellView.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
        // Set custom color if required
        setCustomResources(dateTime, cellView, tv1);
        return cellView;
    }

}
