package com.eugene.fithealthmaingit.CalTesting;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.eugene.fithealthmaingit.R;

public class CalViewPager extends PagerAdapter {

    @Override
    public CharSequence getPageTitle(int position) { // Tab text
        if (position == 0) {
            return "Home";
        }
        if (position == 1) {
            return "Music";
        }
        return getPageTitle(position);
    }

    /*
    Returns provided id's view (children) to populate pages (ViewGroup, parent) inside of ViewPager
     */
    @Override
    public Object instantiateItem(ViewGroup viewGroup, int position) {
        int resId = 0;
        switch (position) {
            case 0:
                resId = R.id.page1;
                break;
            case 1:
                resId = R.id.page2;
                break;
        }
        return viewGroup.findViewById(resId);
    }

    @Override

    public int getCount() {
        return 2;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }
}