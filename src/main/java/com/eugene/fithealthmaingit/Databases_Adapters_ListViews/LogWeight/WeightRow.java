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

package com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogWeight;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eugene.fithealthmaingit.R;

import java.text.DecimalFormat;

public class WeightRow extends LinearLayout {
    Context mContext;
    WeightLog mLog;

    public WeightRow(Context context) {
        super(context);
        mContext = context;
        setup();
    }

    public WeightRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setup();
    }

    private void setup() {
        LayoutInflater inflater1 = (LayoutInflater) mContext
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater1.inflate(R.layout.list_weight_row, this);
    }

    DecimalFormat df = new DecimalFormat("0");
    DateFormat dateFormat = new DateFormat();

    public void setLog(WeightLog log) {
        mLog = log;
        TextView currentWeight = (TextView) findViewById(R.id.weightCurrent);
        currentWeight.setText(df.format(mLog.getCurrentWeight()) + " Pounds");
        TextView date = (TextView) findViewById(R.id.weightDate);
        date.setText(dateFormat.format("MMM dd, yyyy", mLog.getDate()));
    }
}