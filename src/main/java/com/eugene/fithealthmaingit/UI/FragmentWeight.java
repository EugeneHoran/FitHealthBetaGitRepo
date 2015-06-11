package com.eugene.fithealthmaingit.UI;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.LineChartView;
import com.db.chart.view.XController;
import com.db.chart.view.YController;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogWeight.WeightLog;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogWeight.WeightLogAdapter;
import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.UI.Dialogs.UpdateWeightDialogFragment;
import com.eugene.fithealthmaingit.Utilities.Equations;
import com.eugene.fithealthmaingit.Utilities.Globals;
import com.eugene.fithealthmaingit.Utilities.SetListHeight.SetWeightListHeight;

import org.apache.commons.math3.util.Precision;

import java.text.DecimalFormat;

public class FragmentWeight extends Fragment {
    private View v;
    DecimalFormat df = new DecimalFormat("0");
    WeightLogAdapter weightLogAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_weight, container, false);
        findViews();
        chart();
        return v;
    }

    LineChartView mLineChart;
    ListView listWeight;
    int max;
    int min;
    float goalWeightLine;
    int goalPosition;
    double weightPerWeek;
    double timeTillGoal;

    private void findViews() {
        Toolbar mToolbar = (Toolbar) v.findViewById(R.id.toolbar_weight);
        mToolbar.setTitle("Your Weight");
        mToolbar.inflateMenu(R.menu.menu_weight);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_create) {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    UpdateWeightDialogFragment suggestionFragment = new UpdateWeightDialogFragment();
                    suggestionFragment.show(fm, "Fragment");
                }
                return false;
            }
        });
        mToolbar.setNavigationIcon(R.mipmap.ic_menu);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.openNavigationDrawer();
            }
        });
        weightLogAdapter = new WeightLogAdapter(getActivity(), 0, WeightLog.all());
        listWeight = (ListView) v.findViewById(R.id.listWeight);
        listWeight.setAdapter(weightLogAdapter);
        SetWeightListHeight.setListViewHeight(listWeight);
        WeightLog weightLogStart = weightLogAdapter.getItem(0);
        TextView startWeight = (TextView) v.findViewById(R.id.startWeight);
        startWeight.setText(df.format(weightLogStart.getCurrentWeight()) + " lbs");
        WeightLog weightLogCurrent = weightLogAdapter.getItem(weightLogAdapter.getCount() - 1);
        TextView currentWeight = (TextView) v.findViewById(R.id.currentWeight);
        currentWeight.setText(df.format(weightLogCurrent.getCurrentWeight()) + " lbs");
        double totalWeightLoss = weightLogStart.getCurrentWeight() - weightLogCurrent.getCurrentWeight();
        TextView lossWeight = (TextView) v.findViewById(R.id.lossWeight);
        lossWeight.setText(df.format(totalWeightLoss) + " lbs");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        TextView goalWeight = (TextView) v.findViewById(R.id.goalWeight);
        goalWeight.setText(sharedPreferences.getString(Globals.USER_WEIGHT_GOAL, "") + " lbs");
        double weightRemaining = weightLogCurrent.getCurrentWeight() - Double.valueOf(sharedPreferences.getString(Globals.USER_WEIGHT_GOAL, ""));
        TextView remainderWeight = (TextView) v.findViewById(R.id.remainderWeight);
        remainderWeight.setText(df.format(weightRemaining) + " lbs");
        max = (int) Precision.round(weightLogStart.getCurrentWeight(), -1) + 10;
        if (Double.valueOf(sharedPreferences.getString(Globals.USER_WEIGHT_GOAL, "")) < weightLogCurrent.getCurrentWeight()) {
            min = (int) Precision.round(Integer.valueOf(sharedPreferences.getString(Globals.USER_WEIGHT_GOAL, "")), -1) - 10;
        } else {
            min = (int) Precision.round(weightLogCurrent.getCurrentWeight(), -1) - 10;
        }
        goalWeightLine = Float.valueOf(sharedPreferences.getString(Globals.USER_WEIGHT_GOAL, ""));

        goalPosition = Integer.valueOf(sharedPreferences.getString(Globals.USER_WEIGHT_LOSS_GOAL, ""));
        if (goalPosition == 0 || goalPosition == 8) {
            weightPerWeek = 2;
        }
        if (goalPosition == 1 || goalPosition == 7) {
            weightPerWeek = 1.5;
        }
        if (goalPosition == 2 || goalPosition == 6) {
            weightPerWeek = 1;
        }
        if (goalPosition == 3 || goalPosition == 5) {
            weightPerWeek = .5;
        }
        if (goalPosition == 4) {
            weightPerWeek = 0;
        }
        timeTillGoal = weightRemaining / weightPerWeek;

        TextView timeRem = (TextView) v.findViewById(R.id.timeRem);
        timeRem.setText(df.format(timeTillGoal) + " Week(s)");
    }

    Paint mLineGridPaint;
    LineSet dataSet;
    float[] simpleArray;
    String[] dates;

    private void chart() {
        mLineChart = (LineChartView) v.findViewById(R.id.linechart);
        mLineChart.reset();
        dataSet = new LineSet();
        simpleArray = new float[weightLogAdapter.getCount()];
        dates = new String[weightLogAdapter.getCount()];
        for (int i = 0; i < weightLogAdapter.getCount(); i++) {
            WeightLog weightLogWeight = weightLogAdapter.getItem(i);
            simpleArray[i] = (float) weightLogWeight.getCurrentWeight();
            dates[i] = String.valueOf(DateFormat.format("MMM dd", weightLogWeight.getDate()));
        }
        dataSet.addPoints(dates, simpleArray);
        mLineGridPaint = new Paint();
        mLineGridPaint.setColor(this.getResources().getColor(R.color.accent));
        mLineGridPaint.setStyle(Paint.Style.FILL);
        mLineGridPaint.setAntiAlias(true);
        /**
         * Controlling the data set and setting it to the chart.
         */
        dataSet
            .setDots(true)
            .setDotsColor(this.getResources().getColor(R.color.primary))
            .setDotsRadius(Tools.fromDpToPx(3))
            .setDotsStrokeThickness(Tools.fromDpToPx(1))
            .setDotsStrokeColor(this.getResources().getColor(R.color.primary))
            .setLineColor(this.getResources().getColor(R.color.primary_dark))
            .setLineThickness(Tools.fromDpToPx(1))
            .beginAt(0).endAt(weightLogAdapter.getCount());
        mLineChart.addData(dataSet);

        mLineChart
            .setBorderSpacing(Tools.fromDpToPx(0))
            .setGrid(LineChartView.GridType.HORIZONTAL, mLineGridPaint)
            .setXAxis(false)
            .setXLabels(XController.LabelPosition.OUTSIDE)
            .setYAxis(false)
            .setYLabels(YController.LabelPosition.OUTSIDE)
            .setAxisBorderValues(min, max, 5)
            .setLabelColor(this.getResources().getColor(R.color.text_color))
            .setLabelsFormat(new DecimalFormat("##' lbs'"))
            .show();

        Paint paint = new Paint();
        paint.setStrokeWidth((float) Equations.dpToPx(getActivity(), 1));
        paint.setColor(getActivity().getResources().getColor(R.color.light_grey));
        mLineChart.setThresholdLine(goalWeightLine, paint);
    }

    private FragmentCallbacks mCallbacks;

    public interface FragmentCallbacks {
        void openNavigationDrawer();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (FragmentCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement Fragment One.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

}
