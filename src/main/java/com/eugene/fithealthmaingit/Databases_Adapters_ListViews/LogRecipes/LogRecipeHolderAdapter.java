/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogRecipes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood.LogAdapterAll;
import com.eugene.fithealthmaingit.R;

import java.text.DecimalFormat;
import java.util.List;

public class LogRecipeHolderAdapter extends ArrayAdapter<LogRecipeHolder> {
    Context mContext;
    public static List<LogRecipeHolder> mLogs;

    public LogRecipeHolderAdapter(Context context, int textViewResourceId, List<LogRecipeHolder> logs) {
        super(context, textViewResourceId);
        mContext = context;
        mLogs = logs;
    }

    public void setLogs(List<LogRecipeHolder> logs) {
        mLogs = logs;
    }

    public List<LogRecipeHolder> getLogs() {
        return mLogs;
    }

    public void add(LogRecipeHolder log) {
        mLogs.add(log);
    }

    public void remove(LogRecipeHolder log) {
        LogAdapterAll.mLogs.remove(log);
    }

    public int getCount() {
        return mLogs.size();
    }

    public LogRecipeHolder getItem(int position) {
        return mLogs.get(position);
    }

    DecimalFormat df = new DecimalFormat("0");

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_recipe_row, null, true);

        TextView mealNameManual = (TextView) view.findViewById(R.id.mealNameManual);
        mealNameManual.setText(mLogs.get(position).getMealName());

        TextView servingSize = (TextView) view.findViewById(R.id.servingSize);
        servingSize.setText("Recipe");

        TextView mealNutrition = (TextView) view.findViewById(R.id.mealNutrition);
        mealNutrition.setText("Cal: " + df.format(mLogs.get(position).getCalorieCount()) + " Fat: " + df.format(mLogs.get(position).getFatCount()) + " Carb: " + df.format(mLogs.get(position).getCarbCount()) + " Pro: " + df.format(mLogs.get(position).getProteinCount()));
        return view;
    }
}
