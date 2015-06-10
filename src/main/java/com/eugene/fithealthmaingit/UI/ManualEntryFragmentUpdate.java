package com.eugene.fithealthmaingit.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.eugene.fithealthmaingit.Databases.FoodManual.LogAdapterManual;
import com.eugene.fithealthmaingit.Databases.FoodManual.LogManual;
import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.Utilities.Globals;

import java.util.Date;
import java.util.Random;

public class ManualEntryFragmentUpdate extends Fragment {

    private View v;
    private Toolbar toolbar_manual;
    private EditText servingName;
    private EditText mealName;
    private EditText mealCalories;
    private EditText mealFat;
    private EditText mealCarbs;
    private EditText mealProtein;
    private EditText mealSatFat;
    private EditText mealCholesterol;
    private EditText mealSodium;
    private EditText mealFiber;
    private EditText mealSugar;
    private EditText mealA;
    private EditText mealC;
    private EditText mealCalcium;
    private EditText mealIron;
    private EditText mealBrand;


    private String sServingName;
    private String sMealName;
    private String sMealCalories;
    private String sMealFat;
    private String sMealCarbs;
    private String sMealProtein;
    private String sMealSatFat;
    private String sMealCholesterol;
    private String sMealSodium;
    private String sMealFiber;
    private String sMealSugar;
    private String sMealA;
    private String sMealC;
    private String sMealCalcium;
    private String sMealIron;
    private String sMealBrand;
    private String mealType;
    private String mealId;
    LogManual logManualUpdate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_manual_entry, container, false);
        // Toolbar
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            mealType = extras.getString(Globals.MEAL_TYPE);
            mealId = extras.getString(Globals.MEAL_ID);
        }
        LogAdapterManual logAdapterManual = new LogAdapterManual(getActivity(), 0, LogManual.logsById(mealId), mealType);
        logManualUpdate = logAdapterManual.getItem(0);

        toolbar_manual = (Toolbar) v.findViewById(R.id.toolbar_manual);
        toolbar_manual.setTitle("Update Entry");
        toolbar_manual.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar_manual.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChooseAddMealActivity.class);
                intent.putExtra(Globals.MEAL_TYPE, mealType);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        toolbar_manual.inflateMenu(R.menu.menu_save_delete);
        toolbar_manual.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItem = item.getItemId();
                if (menuItem == R.id.action_save) {
                    SaveMeal();
                    Intent intent = new Intent(getActivity(), ChooseAddMealActivity.class);
                    intent.putExtra(Globals.MEAL_TYPE, mealType);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                if (menuItem == R.id.action_delete) {
                    logManualUpdate.delete();
                    Intent intent = new Intent(getActivity(), ChooseAddMealActivity.class);
                    intent.putExtra(Globals.MEAL_TYPE, mealType);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                return false;
            }
        });
        // Edit Text
        servingName = (EditText) v.findViewById(R.id.servingName);
        servingName.setText(logManualUpdate.getMealServing());
        mealName = (EditText) v.findViewById(R.id.mealName);
        mealName.setText(logManualUpdate.getMealName());
        mealCalories = (EditText) v.findViewById(R.id.mealCalories);
        mealCalories.setText(logManualUpdate.getCalorieCount() + "");
        mealFat = (EditText) v.findViewById(R.id.mealFat);
        mealFat.setText(logManualUpdate.getFatCount() + "");
        mealCarbs = (EditText) v.findViewById(R.id.mealCarbs);
        mealCarbs.setText(logManualUpdate.getCarbCount() + "");
        mealProtein = (EditText) v.findViewById(R.id.mealProtein);
        mealProtein.setText(logManualUpdate.getProteinCount() + "");
        mealSatFat = (EditText) v.findViewById(R.id.mealSatFat);
        mealSatFat.setText(logManualUpdate.getSaturatedFat() + "");
        mealCholesterol = (EditText) v.findViewById(R.id.mealCholesterol);
        mealCholesterol.setText(logManualUpdate.getCholesterol() + "");
        mealSodium = (EditText) v.findViewById(R.id.mealSodium);
        mealSodium.setText(logManualUpdate.getSodium() + "");
        mealFiber = (EditText) v.findViewById(R.id.mealFiber);
        mealFiber.setText(logManualUpdate.getFiber() + "");
        mealSugar = (EditText) v.findViewById(R.id.mealSugar);
        mealSugar.setText(logManualUpdate.getSugars() + "");
        mealA = (EditText) v.findViewById(R.id.mealA);
        mealA.setText(logManualUpdate.getVitA() + "");
        mealC = (EditText) v.findViewById(R.id.mealC);
        mealC.setText(logManualUpdate.getVitC() + "");
        mealCalcium = (EditText) v.findViewById(R.id.mealCalcium);
        mealCalcium.setText(logManualUpdate.getCalcium() + "");
        mealIron = (EditText) v.findViewById(R.id.mealIron);
        mealIron.setText(logManualUpdate.getIron() + "");
        mealBrand = (EditText) v.findViewById(R.id.mealBrand);
        mealBrand.setText(logManualUpdate.getBrand());
        return v;
    }

    private void SaveMeal() {
        // Strings
        if (sServingName.trim().equals("")) {
            sServingName = "Serving";
        }
        sMealName = mealName.getText().toString();
        if (sMealName.trim().equals("")) {
            sMealName = "No Name";
        }
        sMealCalories = mealCalories.getText().toString();
        sMealFat = mealFat.getText().toString();
        sMealCarbs = mealCarbs.getText().toString();
        sMealProtein = mealProtein.getText().toString();
        sMealSatFat = mealSatFat.getText().toString();
        sMealCholesterol = mealCholesterol.getText().toString();
        sMealSodium = mealSodium.getText().toString();
        sMealFiber = mealFiber.getText().toString();
        sMealSugar = mealSugar.getText().toString();
        sMealA = mealA.getText().toString();
        sMealC = mealC.getText().toString();
        sMealCalcium = mealCalcium.getText().toString();
        sMealIron = mealIron.getText().toString();
        sMealBrand = mealBrand.getText().toString();
        if (sMealBrand.trim().equals("")) {
            sMealBrand = "No Brand";
        }
        //Save

        logManualUpdate.setMealName(sMealName);
        Random r = new Random();

        logManualUpdate.setCalorieCount(checkForNull(sMealCalories));
        logManualUpdate.setFatCount(checkForNull(sMealFat));
        logManualUpdate.setSaturatedFat(checkForNull(sMealSatFat));
        logManualUpdate.setCholesterol(checkForNull(sMealCholesterol));
        logManualUpdate.setSodium(checkForNull(sMealSodium));
        logManualUpdate.setCarbCount(checkForNull(sMealCarbs));
        logManualUpdate.setFiber(checkForNull(sMealFiber));
        logManualUpdate.setSugars(checkForNull(sMealSugar));
        logManualUpdate.setProteinCount(checkForNull(sMealProtein));
        logManualUpdate.setVitA(checkForNull(sMealA));
        logManualUpdate.setVitC(checkForNull(sMealC));
        logManualUpdate.setCalcium(checkForNull(sMealCalcium));
        logManualUpdate.setIron(checkForNull(sMealIron));
        logManualUpdate.setManualEntry("true");
        logManualUpdate.setBrand(sMealBrand);
        logManualUpdate.setServingSize(1);
        logManualUpdate.setMealServing(sServingName);
        logManualUpdate.setDate(new Date());
        logManualUpdate.edit();
    }

    private Double checkForNull(String s) {
        if (s.trim().length() == 0) {
            return (double) 0;
        } else {
            return Double.valueOf(s);
        }
    }
}
