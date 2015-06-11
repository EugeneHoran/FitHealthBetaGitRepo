package com.eugene.fithealthmaingit.UI.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogWeight.WeightLog;
import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.Utilities.Globals;

import java.util.Date;

/**
 * Created by Eugene on 6/10/2015.
 */
public class UpdateWeightDialogFragment  extends android.support.v4.app.DialogFragment {
    View v;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        v = getActivity().getLayoutInflater().inflate(R.layout.dialog_weight, null);
        findViews();
        // alert Dialog builder implementation
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                WeightLog logMeal = new WeightLog();
                logMeal.setDate(new Date());
                EditText editText = (EditText) v.findViewById(R.id.updateWeight);
                logMeal.setCurrentWeight(Double.valueOf(editText.getText().toString()));
                logMeal.save();
                savePreferences(Globals.USER_WEIGHT, editText.getText().toString());
                mCallbacks.updateWeight();
            }
        });
        builder.setNegativeButton("Cancel", null);
        return builder.create();
    }

    private void savePreferences(String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value).apply();
    }

    private void findViews() {

    }

    private FragmentCallbacks mCallbacks;

    public interface FragmentCallbacks {
        void updateWeight();
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
