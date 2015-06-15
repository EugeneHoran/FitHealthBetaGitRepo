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

package com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogQuickSearchData;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public class LogQuickSearchAdapter extends ArrayAdapter<LogQuickSearch> {
    Context mContext;
    public static List<LogQuickSearch> mLogs;

    public LogQuickSearchAdapter(Context context, int textViewResourceId, List<LogQuickSearch> logs) {
        super(context, textViewResourceId);
        mContext = context;
        mLogs = logs;
    }

    public void setLogs(List<LogQuickSearch> logs) {
        mLogs = logs;
    }

    public List<LogQuickSearch> getLogs() {
        return mLogs;
    }

    public void add(LogQuickSearch log) {
        mLogs.add(log);
    }

    public void remove(LogQuickSearch log) {
        LogQuickSearchAdapter.mLogs.remove(log);
    }

    public int getCount() {
        return mLogs.size();
    }

    public LogQuickSearch getItem(int position) {
        return mLogs.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LogQuickSearchRow view = (LogQuickSearchRow) convertView;
        if (view == null) {
            view = new LogQuickSearchRow(mContext);
        }
        LogQuickSearch log = getItem(position);
        view.setLog(log);
        return view;
    }
}
