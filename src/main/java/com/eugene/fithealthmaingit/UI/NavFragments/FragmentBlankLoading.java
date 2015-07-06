package com.eugene.fithealthmaingit.UI.NavFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.MainActivity;

public class FragmentBlankLoading extends Fragment {
    View v;
    int position = 0;
    Toolbar toolbar_blank;
    ProgressBar pbLoad, circularProgressbar;
    RelativeLayout nutrition;
    LinearLayout shadow;
    RelativeLayout main;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_blank_loading, container, false);
        Bundle extras = getArguments();
        if (extras != null) {
            position = extras.getInt(MainActivity.NAV_ITEM_ID);
        }
        main = (RelativeLayout) v.findViewById(R.id.main);
        shadow = (LinearLayout) v.findViewById(R.id.shadow);
        pbLoad = (ProgressBar) v.findViewById(R.id.pbLoad);
        toolbar_blank = (Toolbar) v.findViewById(R.id.toolbar_blank);
        nutrition = (RelativeLayout) v.findViewById(R.id.nutrition);
        circularProgressbar = (ProgressBar) v.findViewById(R.id.circularProgressbar);
        switch (position) {
            case R.id.nav_journal:
                pbLoad.setVisibility(View.VISIBLE);
                toolbar_blank.inflateMenu(R.menu.menu_main_journal);
                SpannableString s = new SpannableString("Fit Journal");
                s.setSpan(new TypefaceSpan("Roboto-Thin.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                toolbar_blank.setTitle(s);
                toolbar_blank.setSubtitle("Today");
                break;
            case R.id.nav_nutrition:
                nutrition.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_weight:
                toolbar_blank.inflateMenu(R.menu.menu_weight);
                toolbar_blank.setTitle("Your Weight");
                break;
            case R.id.nav_health:
                toolbar_blank.setTitle("Your Health");
                break;
            case R.id.nav_fitbit:
                toolbar_blank.setTitle("Fitbit");
                toolbar_blank.inflateMenu(R.menu.menu_fit_bit_fragment);
                //
                break;
            default:
                break;
        }
        return v;
    }
}
