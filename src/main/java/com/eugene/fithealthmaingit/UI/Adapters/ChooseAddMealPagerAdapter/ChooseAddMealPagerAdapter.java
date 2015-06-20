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

package com.eugene.fithealthmaingit.UI.Adapters.ChooseAddMealPagerAdapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.eugene.fithealthmaingit.R;

public class ChooseAddMealPagerAdapter extends PagerAdapter {

    @Override
    public CharSequence getPageTitle(int position) { // Tab text
        if (position == 0) {
            return "Created Meals";
        }
        if (position == 1) {
            return "Favorites";
        }
        if (position == 2) {
            return "Recipe";
        }
        if (position == 3) {
            return "Recent Searches";
        }
        return getPageTitle(position);
    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, int position) {
        int resId = 0;
        switch (position) {
            case 0:
                resId = R.id.manual;
                break;
            case 1:
                resId = R.id.favorites;
                break;
            case 2:
                resId = R.id.recipes;
                break;
            case 3:
                resId = R.id.recent;
                break;
        }
        return viewGroup.findViewById(resId);
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }
}