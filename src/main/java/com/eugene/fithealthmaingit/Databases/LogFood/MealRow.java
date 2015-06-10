package com.eugene.fithealthmaingit.Databases.LogFood;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eugene.fithealthmaingit.R;

import java.text.DecimalFormat;
import java.util.Date;

public class MealRow extends LinearLayout {
    Context mContext;
    LogMeal mLog;

    public MealRow(Context context) {
        super(context);
        mContext = context;
        setup();
    }

    public MealRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setup();
    }

    private void setup() {
        LayoutInflater inflater1 = (LayoutInflater) mContext
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater1.inflate(R.layout.list_meal_row, this);
    }

    DecimalFormat df = new DecimalFormat("0");

    public void setLog(LogMeal log) {
        mLog = log;
        LinearLayout mCircle = (LinearLayout) findViewById(R.id.circle);
        TextView mMealName = (TextView) findViewById(R.id.log_meal_name1);
        TextView mCalories = (TextView) findViewById(R.id.log_calories);
        TextView mServing = (TextView) findViewById(R.id.serving);
        TextView mTime = (TextView) findViewById(R.id.time);
        mCircle.bringToFront();
        mMealName.setText(mLog.getMealName() + "");
        mCalories.setText(df.format(mLog.getCalorieCount()) + " ");
        mServing.setText(mLog.getServingSize() + " " + mLog.getMealServing());
        long lStartTime = new Date().getTime();
        long lEndTime = mLog.getDate().getTime();
        long difference = lEndTime - lStartTime;
        int seconds = (int) (difference / 1000) * (-1);
        int minutes = (int) ((difference / (1000 * 60)));
        int min = minutes * -1;
        int hours = (int) ((difference / (1000 * 60 * 60)) % 24);
        int hour = hours * -1;
        if (seconds < 60 && minutes < 60) {
            mTime.setText("Seconds ago");
        } else if (seconds >= 60 && min < 60 && hour < 1) {
            mTime.setText(min + "  Min");
        } else {
            mTime.setText(DateFormat.format("h:m a", mLog.getDate()));
        }
    }
}