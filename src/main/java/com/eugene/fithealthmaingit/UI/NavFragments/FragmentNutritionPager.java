package com.eugene.fithealthmaingit.UI.NavFragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.Utilities.DateCompare;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Currently in production.
 * <p/>
 * Facing some errors with having a fragment within a fragment and the savestate
 */
public class FragmentNutritionPager extends Fragment {

    View v;
    ViewPager viewPager;
    private Toolbar mToolbarDaily;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.testing_nutrition_pager, container, false);
        return v;
    }

    Date mDate = new Date();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRetainInstance(true);
        viewPager = (ViewPager) v.findViewById(R.id.pager);
        InitializePagerTabs();
        mToolbarDaily = (Toolbar) v.findViewById(R.id.toolbar_daily);
        mToolbarDaily.setNavigationIcon(R.mipmap.ic_menu);
        mToolbarDaily.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.openNavigationDrawer();
            }
        });
        mToolbarDaily.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.action_today) {
                    viewPager.setCurrentItem(26);
                }

                return false;
            }
        });
        ImageView mDatePrev = (ImageView) v.findViewById(R.id.datePrevious);
        ImageView mDateNext = (ImageView) v.findViewById(R.id.dateNext);
        mDatePrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDate = DateCompare.previousDate(mDate);
                handleDateChanges(mDate);
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            }
        });
        mDateNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDate = DateCompare.nextDate(mDate);
                handleDateChanges(mDate);
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        });
        handleDateChanges(mDate);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void InitializePagerTabs() {
        viewPager.setAdapter(
            new FragmentStatePagerAdapter(getActivity().getSupportFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    // Here to configure the date.
                    int pos = position - 26;
                    return FragmentNutritionHolder.newInstance(handlePagerDate(pos));
                }

                @Override
                public void destroyItem(ViewGroup container, int position, Object object) {
                    try {
                        super.destroyItem(container, position, object);
                    } catch (IllegalStateException ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public int getCount() {
                    return 51;
                }
            }

        );

        viewPager.setCurrentItem(26);
        viewPager.addOnPageChangeListener(
            new ViewPager.OnPageChangeListener()

            {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    int pos = position - 26;
                    mDate = handlePagerDate(pos);
                    handleDateChanges(mDate);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            }

        );
    }

    /**
     * Update text and menu based on date change
     *
     * @param date current Date or updated date
     */
    private void handleDateChanges(Date date) {
        TextView mDateText = (TextView) v.findViewById(R.id.tbDate);
        if (DateCompare.areDatesEqual(new Date(), date)) { // Are Dates Equal Today
            mToolbarDaily.getMenu().clear();
            mDateText.setText("Today");
        } else if (DateCompare.areDatesEqualYesterday(new Date(), date)) {  // Are Dates Equal Yesterday
            mToolbarDaily.getMenu().clear();
            mToolbarDaily.inflateMenu(R.menu.menu_today);
            mDateText.setText("Yesterday");
        } else if (DateCompare.areDatesEqualTomorrow(new Date(), date)) {  // Are Dates Equal Yesterday
            mToolbarDaily.getMenu().clear();
            mToolbarDaily.inflateMenu(R.menu.menu_today);
            mDateText.setText("Tomorrow");
        } else {
            mToolbarDaily.getMenu().clear();
            mToolbarDaily.inflateMenu(R.menu.menu_today);
            mDateText.setText(DateFormat.format("MMM d, EE", date));
        }
    }

    public static Date handlePagerDate(int pos) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        Calendar mCalendar = new GregorianCalendar(year, month, day);
        mCalendar.add(Calendar.DATE, pos);
        return mCalendar.getTime();
    }

    private FragmentCallbacks mCallbacks;

    public interface FragmentCallbacks {
        void openNavigationDrawer();

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

