package com.eugene.fithealthmaingit.UI.NutritionPagerTesting;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;
import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood.LogAdapterAll;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood.LogMeal;
import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.Utilities.Globals;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;


public class FragmentNutritionHolder extends Fragment {
    public static FragmentNutritionHolder newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable("DATE", date);
        FragmentNutritionHolder fragment = new FragmentNutritionHolder();
        fragment.setArguments(args);
        return fragment;
    }

    private View v;
    Date d;
    private DecimalFormat df = new DecimalFormat("0");
    private DecimalFormat dfT = new DecimalFormat("0.0");
    private Date mDate = new Date();
    private Date mDateDoesNotChange = new Date();
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.testing_nutrition_holder, container, false);
        d = (Date) getArguments().getSerializable("DATE");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handleTextViews(d);

    }

    private void handleTextViews(Date d) {
        LogAdapterAll logMealArrayAdapter = new LogAdapterAll(getActivity(), 0, LogMeal.logsByDate(d));
        int breakfastCalorieCount = 0;
        int breakFatCount = 0;
        int breakfastSaturatedFatCount = 0;
        int breakCholesterolCount = 0;
        int breakSodiumCount = 0;
        int breakCarbsCount = 0;
        int breakFiberCount = 0;
        int breakSugarCount = 0;
        int breakProteinCount = 0;
        int breakVitACount = 0;
        int breakVitCCount = 0;
        int breakCalciumCount = 0;
        int breakIronCount = 0;
        for (LogMeal log : logMealArrayAdapter.getLogs()) {
            breakfastCalorieCount += log.getCalorieCount();
            breakFatCount += log.getFatCount();
            breakfastSaturatedFatCount += log.getSaturatedFat();
            breakCholesterolCount += log.getCholesterol();
            breakSodiumCount += log.getSodium();
            breakCarbsCount += log.getCarbCount();
            breakFiberCount += log.getFiber();
            breakSugarCount += log.getSugars();
            breakProteinCount += log.getProteinCount();
            breakVitACount += log.getVitA();
            breakVitCCount += log.getVitC();
            breakCalciumCount += log.getCalcium();
            breakIronCount += log.getIron();
        }
        // All TextViews.  Formatted like this because they are extremely local and are not changed after the OnCreate
        //   ((TextView) v.findViewById(R.id.servingCOnsumed)).setText(df.format(mLogMeal.getServingSize()));
        ((TextView) v.findViewById(R.id.caloriesNutrition)).setText(df.format(breakfastCalorieCount));
        ((TextView) v.findViewById(R.id.fatNutrition)).setText(df.format(breakFatCount) + " g");
        ((TextView) v.findViewById(R.id.saturatedFat)).setText(df.format(breakfastSaturatedFatCount) + " g");
        ((TextView) v.findViewById(R.id.cholesterol)).setText(df.format(breakCholesterolCount) + " mg");
        ((TextView) v.findViewById(R.id.sodium)).setText(df.format(breakSodiumCount) + " mg");
        ((TextView) v.findViewById(R.id.carbohydratesNutrition)).setText(df.format(breakCarbsCount) + " g");
        ((TextView) v.findViewById(R.id.fiber)).setText(df.format(breakFiberCount) + " g");
        ((TextView) v.findViewById(R.id.sugars)).setText(df.format(breakSugarCount) + " g");
        ((TextView) v.findViewById(R.id.proteinNutrition)).setText(df.format(breakProteinCount) + " g");
        ((TextView) v.findViewById(R.id.vitiminA)).setText(df.format(breakVitACount) + "%");
        ((TextView) v.findViewById(R.id.vitiminC)).setText(df.format(breakVitCCount) + "%");
        ((TextView) v.findViewById(R.id.calcium)).setText(df.format(breakCalciumCount) + "%");
        ((TextView) v.findViewById(R.id.iron)).setText(df.format(breakIronCount) + "%");

               /*
        Fat: 1 gram = 9 calories
        Protein: 1 gram = 4 calories
        Carbohydrates: 1 gram = 4 calories
        Alcohol: 1 gram = 7 calories
         */
        TextView fat = (TextView) v.findViewById(R.id.fat);
        TextView carb = (TextView) v.findViewById(R.id.carb);
        TextView pro = (TextView) v.findViewById(R.id.pro);

        PieGraph pg = (PieGraph) v.findViewById(R.id.graph);
        pg.removeSlices();
        // Fat
        int fatCount = breakFatCount * 9;
        PieSlice slice = new PieSlice();
        slice.setColor(Color.parseColor("#99CC00"));
        if (fatCount == 0) {
            slice.setValue(1);
        } else {
            slice.setValue(fatCount);
        }
        pg.addSlice(slice);

        // Carbs
        int carbCount = breakCarbsCount * 4;
        slice = new PieSlice();
        slice.setColor(Color.parseColor("#FFBB33"));
        if (carbCount == 0) {
            slice.setValue(1);
        } else {
            slice.setValue(carbCount);
        }
        pg.addSlice(slice);

        // Pro
        int proCount = breakProteinCount * 4;
        slice = new PieSlice();
        slice.setColor(Color.parseColor("#AA66CC"));
        if (proCount == 0) {
            slice.setValue(1);
        } else {
            slice.setValue(proCount);
        }
        pg.addSlice(slice);


        double percentage = fatCount + carbCount + proCount;

        double fatPer = (fatCount / percentage) * 100;
        double carbPer = (carbCount / percentage) * 100;
        double proPer = (proCount / percentage) * 100;

        boolean fatNaN = fatPer != fatPer;
        boolean carbNaN = carbPer != carbPer;
        boolean proNaN = proPer != proPer;
        if (fatNaN == true) {
            fatPer = 0;
        }
        if (carbNaN == true) {
            carbPer = 0;
        }
        if (proNaN == true) {
            proPer = 0;
        }
        fat.setText(dfT.format(fatPer) + "%");
        carb.setText(dfT.format(carbPer) + "%");
        pro.setText(dfT.format(proPer) + "%");


        // Bar Graph
        double FatGoal = Double.valueOf(sharedPreferences.getString(Globals.USER_DAILY_FAT, ""));
        double CarbGoal = Double.valueOf(sharedPreferences.getString(Globals.USER_DAILY_CARBOHYDRATES, ""));
        double ProteinGoal = Double.valueOf(sharedPreferences.getString(Globals.USER_DAILY_PROTEIN, ""));

        ArrayList<Bar> points = new ArrayList<Bar>();

        // Fat
        Bar dd = new Bar();
        dd.setColor(Color.parseColor("#99CC00"));
        dd.setValue(breakFatCount);

        Bar d2 = new Bar();
        d2.setColor(Color.parseColor("#FFBB33"));
        d2.setValue(Integer.valueOf(df.format(FatGoal)));

        // Carbs
        Bar dd1 = new Bar();
        dd1.setColor(Color.parseColor("#99CC00"));
        dd1.setValue(breakCarbsCount / 2);


        Bar d21 = new Bar();
        d21.setColor(Color.parseColor("#FFBB33"));
        d21.setValue(Integer.valueOf(df.format(CarbGoal)) / 2);

        // Pro
        Bar dd11 = new Bar();
        dd11.setColor(Color.parseColor("#99CC00"));
        dd11.setValue(breakProteinCount);

        Bar d211 = new Bar();
        d211.setColor(Color.parseColor("#FFBB33"));
        d211.setValue(Integer.valueOf(df.format(ProteinGoal)));


        points.add(dd);
        points.add(d2);
        points.add(dd1);
        points.add(d21);
        points.add(dd11);
        points.add(d211);
        BarGraph g = (BarGraph) v.findViewById(R.id.graphBar);
        g.setShowPopup(false);
        g.setShowBarText(false);
        g.setShowAxis(false);
        g.setBars(points);

        TextView barFat = (TextView) v.findViewById(R.id.barFat);
        TextView barCarb = (TextView) v.findViewById(R.id.barCarb);
        TextView barPro = (TextView) v.findViewById(R.id.barPro);
        barFat.setText(df.format(breakFatCount) + "g : " + df.format(FatGoal) + "g");
        barCarb.setText(df.format(breakCarbsCount) + "g : " + df.format(CarbGoal) + "g");
        barPro.setText(df.format(breakProteinCount) + "g : " + df.format(ProteinGoal) + "g");

        RelativeLayout macrosInfo = (RelativeLayout) v.findViewById(R.id.macrosInfo);
        macrosInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Macros Info")
                    .setPositiveButton("Done", null);
                LayoutInflater inflater = getActivity().getLayoutInflater();
                FrameLayout f1 = new FrameLayout(getActivity());
                f1.addView(inflater.inflate(R.layout.dialog_macros_info, f1, false));
                builder.setView(f1);
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
}
