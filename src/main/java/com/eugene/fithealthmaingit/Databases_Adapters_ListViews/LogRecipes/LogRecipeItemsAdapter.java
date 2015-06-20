package com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogRecipes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood.LogAdapterBreakfast;
import com.eugene.fithealthmaingit.R;

import java.util.List;

public class LogRecipeItemsAdapter extends ArrayAdapter<LogRecipeItems> {
    Context mContext;
    public static List<LogRecipeItems> mLogs;

    public LogRecipeItemsAdapter(Context context, int textViewResourceId, List<LogRecipeItems> logs) {
        super(context, textViewResourceId);
        mContext = context;
        mLogs = logs;
    }

    public void setLogs(List<LogRecipeItems> logs) {
        mLogs = logs;
    }

    public List<LogRecipeItems> getLogs() {
        return mLogs;
    }

    public void add(LogRecipeItems log) {
        mLogs.add(log);
    }

    public void remove(LogRecipeItems log) {
        LogAdapterBreakfast.mLogs.remove(log);
    }

    public int getCount() {
        return mLogs.size();
    }

    public LogRecipeItems getItem(int position) {
        return mLogs.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_recipe_item_row, null, true);
        TextView mealNameManual = (TextView) view.findViewById(R.id.mealNameManual);
        mealNameManual.setText(mLogs.get(position).getMealName());
        TextView servingSize = (TextView) view.findViewById(R.id.servingSize);
        servingSize.setText(mLogs.get(position).getServingSize() + "  " + mLogs.get(position).getMealServing());
        TextView mealNutrition = (TextView) view.findViewById(R.id.mealNutrition);
        mealNutrition.setText("Cal " + mLogs.get(position).getCalorieCount() + " | Fat " + mLogs.get(position).getFatCount() + " | Carb " + mLogs.get(position).getCarbCount() + " | Pro " + mLogs.get(position).getProteinCount());
        return view;
    }
}
