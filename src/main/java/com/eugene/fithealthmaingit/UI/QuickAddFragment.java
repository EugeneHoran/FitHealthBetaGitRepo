package com.eugene.fithealthmaingit.UI;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood.LogMeal;
import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.MainActivity;
import com.eugene.fithealthmaingit.Utilities.Globals;
import com.eugene.fithealthmaingit.Utilities.OrderFormat;

import java.util.Date;

public class QuickAddFragment extends Fragment {
    private View v;
    String mMealType;

    private EditText mMealName;
    private EditText mMealCalories;
    private EditText mMealFat;
    private EditText mMealCarbs;
    private EditText mMealProtein;
    private EditText mServingSize;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_quick_add, container, false);
        // Get meal type from Journal
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mMealType = bundle.getString(Globals.MEAL_TYPE);
        }

        InitiateToolbar();

        mMealName = (EditText) v.findViewById(R.id.mealName);
        mMealCalories = (EditText) v.findViewById(R.id.mealCalories);
        mMealCalories.requestFocus();
        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        mMealFat = (EditText) v.findViewById(R.id.mealFat);
        mMealCarbs = (EditText) v.findViewById(R.id.mealCarbs);
        mMealProtein = (EditText) v.findViewById(R.id.mealProtein);
        return v;
    }

    /**
     * Setup Toolbar
     */
    private void InitiateToolbar() {
        Toolbar mToolbarQuickAdd = (Toolbar) v.findViewById(R.id.toolbarQuickAdd);
        mToolbarQuickAdd.setTitle("Quick Add");
        mToolbarQuickAdd.setNavigationIcon(R.mipmap.ic_arrow_back);
        mToolbarQuickAdd.inflateMenu(R.menu.menu_user_info);
        mToolbarQuickAdd.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mMealName.getWindowToken(), 0);
                getActivity().finish();
                getActivity().overridePendingTransition(0, 0);
            }
        });

        mToolbarQuickAdd.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                if (menuItem.getItemId() == R.id.action_save) {
                    if (mMealName.getText().toString().trim().length() > 0
                        && mMealCalories.getText().toString().trim().length() > 0) {
                        ServingDialog();
                    } else {
                        if (mMealName.getText().toString().trim().length() == 0)
                            mMealName.setError("Missing Field");
                        if (mMealCalories.getText().toString().trim().length() == 0)
                            mMealCalories.setError("Missing Field");
                    }
                }
                return false;
            }
        });
    }


    private void ServingDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Update Serving Size: ");
        alert.setMessage("Servings Consumed");
        mServingSize = new EditText(getActivity());
        mServingSize.setText("1");
        mServingSize.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mServingSize.selectAll();
        mServingSize.setGravity(Gravity.CENTER_HORIZONTAL);
        alert.setView(mServingSize, 64, 0, 64, 0);
        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mServingSize.getWindowToken(), 0);
                LogMeal logMeals = new LogMeal();
                logMeals.setMealChoice(mMealType);
                logMeals.setMealName(mMealName.getText().toString());
                logMeals.setCalorieCount(Double.valueOf(mMealCalories.getText().toString()) * Double.valueOf(mServingSize.getText().toString()));
                if (mMealFat.getText().toString().trim().length() > 0)
                    logMeals.setFatCount(Double.valueOf(mMealFat.getText().toString()) * Double.valueOf(mServingSize.getText().toString()));
                else
                    logMeals.setFatCount(0);

                if (mMealCarbs.getText().toString().trim().length() > 0)
                    logMeals.setCarbCount(Double.valueOf(mMealCarbs.getText().toString()) * Double.valueOf(mServingSize.getText().toString()));
                else {
                    logMeals.setCarbCount(0);
                }

                if (mMealProtein.getText().toString().trim().length() > 0)
                    logMeals.setProteinCount(Double.valueOf(mMealProtein.getText().toString()) * Double.valueOf(mServingSize.getText().toString()));
                else
                    logMeals.setProteinCount(0);
                logMeals.setDate(new Date());
                logMeals.setServingSize(Double.parseDouble(mServingSize.getText().toString()));
                logMeals.setMealServing("Serving");
                logMeals.setOrderFormat(OrderFormat.setMealFormat(mMealType));
                logMeals.save();
                Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
                mCallbacks.itemAdded("");
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(mServingSize.getWindowToken(), 0);
            }
        });
        alert.setCancelable(false);
        alert.show();
    }

    /**
     * Interface
     */
    private QuickAddItemAdded mCallbacks;

    public interface QuickAddItemAdded {
        void itemAdded(String s);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (QuickAddItemAdded) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

}
