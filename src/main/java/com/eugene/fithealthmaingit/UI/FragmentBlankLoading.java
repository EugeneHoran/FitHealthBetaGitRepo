package com.eugene.fithealthmaingit.UI;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eugene.fithealthmaingit.R;

import java.text.DecimalFormat;
import java.util.Date;

public class FragmentBlankLoading extends Fragment {
    View v;
    int position = 0;
    Toolbar toolbar_blank;
    ProgressBar pbLoad, circularProgressbar;
    RelativeLayout nutrition;
    LinearLayout shadow;
    RelativeLayout main;
    RelativeLayout fit_bit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_blank_loading, container, false);
        Bundle extras = getArguments();
        if (extras != null) {
            position = extras.getInt("position");
        }
        main = (RelativeLayout) v.findViewById(R.id.main);
        shadow = (LinearLayout) v.findViewById(R.id.shadow);
        pbLoad = (ProgressBar) v.findViewById(R.id.pbLoad);
        fit_bit = (RelativeLayout) v.findViewById(R.id.fit_bit);
        toolbar_blank = (Toolbar) v.findViewById(R.id.toolbar_blank);
        nutrition = (RelativeLayout) v.findViewById(R.id.nutrition);
        circularProgressbar = (ProgressBar) v.findViewById(R.id.circularProgressbar);
        switch (position) {
            case 0:
                pbLoad.setVisibility(View.VISIBLE);
                toolbar_blank.inflateMenu(R.menu.menu_main_journal);
                SpannableString s = new SpannableString("Fit Journal");
                s.setSpan(new TypefaceSpan("Roboto-Thin.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                toolbar_blank.setTitle(s);
                toolbar_blank.setSubtitle("Today");
                break;
            case 1:
                nutrition.setVisibility(View.VISIBLE);
                break;
            case 2:
                toolbar_blank.inflateMenu(R.menu.menu_weight);
                toolbar_blank.setTitle("Your Weight");
                break;
            case 3:
                toolbar_blank.setTitle("Your Health");
                break;
            case 4:
                circularProgressbar.setVisibility(View.GONE);
                toolbar_blank.setVisibility(View.GONE);
                shadow.setVisibility(View.GONE);
                pbLoad.setVisibility(View.GONE);
                Toolbar toolbar_fitbit = (Toolbar) v.findViewById(R.id.toolbar_fitbit);
                toolbar_fitbit.inflateMenu(R.menu.menu_fit_bit_fragment);
                fit_bit.setVisibility(View.VISIBLE);
                finViews();
                break;
            default:
                break;
        }
        return v;
    }

    /**
     * FitBit
     */
    DecimalFormat df = new DecimalFormat("0");
    TextView device;
    TextView date;
    TextView lastSync;
    TextView caloriesBurned;
    ProgressBar progressSteps;
    ProgressBar progressDistance;
    ImageView deviceType, batteryStatus;
    TextView sedentary;
    TextView light;
    TextView fairly;
    TextView very;
    TextView noActivities;
    String productName = "No Device";

    private void finViews() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        TextView stepsGoal = (TextView) v.findViewById(R.id.stepsGoal);
        TextView stepsWalked = (TextView) v.findViewById(R.id.stepsWalked);
        TextView diatanceGoal = (TextView) v.findViewById(R.id.diatanceGoal);
        TextView diatanceWalked = (TextView) v.findViewById(R.id.diatanceWalked);
        device = (TextView) v.findViewById(R.id.device);
        deviceType = (ImageView) v.findViewById(R.id.deviceType);
        batteryStatus = (ImageView) v.findViewById(R.id.batteryStatus);
        lastSync = (TextView) v.findViewById(R.id.lastSync);
        caloriesBurned = (TextView) v.findViewById(R.id.caloriesBurned);
        date = (TextView) v.findViewById(R.id.date);
        date.setText("Today, " + DateFormat.format("MMM d", new Date()));
        progressSteps = (ProgressBar) v.findViewById(R.id.progressSteps);
        progressDistance = (ProgressBar) v.findViewById(R.id.progressDistance);

        /**
         * Activities
         */
        sedentary = (TextView) v.findViewById(R.id.sedentary);
        light = (TextView) v.findViewById(R.id.light);
        fairly = (TextView) v.findViewById(R.id.fairly);
        very = (TextView) v.findViewById(R.id.very);
        noActivities = (TextView) v.findViewById(R.id.noActivities);
        noActivities.setVisibility(View.VISIBLE);


        stepsGoal.setText(sharedPreferences.getString("STEPS_GOAL", "..."));
        stepsWalked.setText(sharedPreferences.getString("STEPS_ACTUAL", "..."));
        diatanceGoal.setText(sharedPreferences.getString("DISTANCE_GOAL", "..."));
        diatanceWalked.setText(sharedPreferences.getString("DISTANCE_ACTUAL", "..."));
        progressSteps.setMax(Integer.valueOf(sharedPreferences.getString("STEPS_GOAL", "0")));
        progressSteps.setProgress(Integer.valueOf(sharedPreferences.getString("STEPS_ACTUAL", "0")));
        double distanceMax = Double.valueOf(sharedPreferences.getString("DISTANCE_GOAL", "0")) * 100;
        double distanceCurrent = Double.valueOf(sharedPreferences.getString("DISTANCE_ACTUAL", "0")) * 100;
        progressDistance.setMax(Integer.valueOf(df.format(distanceMax)));
        progressDistance.setProgress(Integer.valueOf(df.format(distanceCurrent)));

        lastSync.setText(sharedPreferences.getString("DEVICE_SYNC", "Last Sync: "));
        device.setText(sharedPreferences.getString("DEVICE_NAME", "Device"));
        productName = sharedPreferences.getString("DEVICE_NAME", "Device");
        switch (productName) {
            case "Flex":
                deviceType.setImageResource(R.drawable.flex);
                break;
            case "Zip":
                deviceType.setImageResource(R.drawable.zip);
                break;
            case "One":
                deviceType.setImageResource(R.drawable.one);
                break;
            case "Charge":
                deviceType.setImageResource(R.drawable.charge);
                break;
            case "Charge HR":
                deviceType.setImageResource(R.drawable.chargehr);
                break;
            case "Surge":
                deviceType.setImageResource(R.drawable.surge);
                break;
            default:
                deviceType.setImageResource(R.drawable.flex);
                break;
        }

        caloriesBurned.setText(sharedPreferences.getString("CALORIES_BURNED", "0"));
        sedentary.setText(sharedPreferences.getString("SEDENTARY", "0") + " minutes");
        light.setText(sharedPreferences.getString("LIGHT_ACTIVITY", "0") + " minutes");
        fairly.setText(sharedPreferences.getString("FAIR_ACTIVITY", "0") + " minutes");
        very.setText(sharedPreferences.getString("VERY_ACTIVITY", "0") + " minutes");
    }
}
