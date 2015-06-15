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

package com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.eugene.fithealthmaingit.R;

import java.util.List;

public class LogAdapterDinner extends ArrayAdapter<LogMeal> {
    private static LogAdapterDinner instance;
    Context mContext;
    public static List<LogMeal> mLogs;

    public LogAdapterDinner(Context context, int textViewResourceId, List<LogMeal> logs) {
        super(context, textViewResourceId);
        mContext = context;
        mLogs = logs;
    }

    public void setLogs(List<LogMeal> logs) {
        mLogs = logs;
    }

    public List<LogMeal> getLogs() {
        return mLogs;
    }

    public void add(LogMeal log) {
        mLogs.add(log);
    }

    public void remove(LogMeal log) {
        LogAdapterDinner.mLogs.remove(log);
    }

    public int getCount() {
        return mLogs.size();
    }

    public LogMeal getItem(int position) {
        return mLogs.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        MealRow view = (MealRow) convertView;
        if (view == null) {
            view = new MealRow(mContext);
        }
        LogMeal log = getItem(position);
        view.setLog(log);
        if (position == 0) {
            LinearLayout topLine = (LinearLayout) view.findViewById(R.id.topLine);
            topLine.setVisibility(View.INVISIBLE);
        } else {
            LinearLayout topLine = (LinearLayout) view.findViewById(R.id.topLine);
            topLine.setVisibility(View.VISIBLE);
        }
        if (position + 1 == getCount()) {
            LinearLayout bottomLine = (LinearLayout) view.findViewById(R.id.bottomLine);
            bottomLine.setVisibility(View.INVISIBLE);
        } else {
            LinearLayout bottomLine = (LinearLayout) view.findViewById(R.id.bottomLine);
            bottomLine.setVisibility(View.VISIBLE);
        }
        return view;
    }
}
