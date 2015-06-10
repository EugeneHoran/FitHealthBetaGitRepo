package com.eugene.fithealthmaingit.UI.Adapters.ChooseAddMealPagerAdapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.eugene.fithealthmaingit.R;

public class MyPagerAdapterAdd extends PagerAdapter {

    @Override
    public CharSequence getPageTitle(int position) { // Tab text
        if (position == 0) {
            return "Meals";
        }
        if (position == 1) {
            return "Favorites";
        }
        if (position == 2) {
            return "Recent";
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
                resId = R.id.recent;
                break;
        }
        return viewGroup.findViewById(resId);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }
}