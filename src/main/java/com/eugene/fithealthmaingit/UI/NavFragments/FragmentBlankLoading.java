package com.eugene.fithealthmaingit.UI.NavFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.eugene.fithealthmaingit.Custom.TextViewFont;
import com.eugene.fithealthmaingit.MainActivity;
import com.eugene.fithealthmaingit.R;

public class FragmentBlankLoading extends Fragment {
    View v;
    int position = 0;
    Toolbar toolbar_blank;
    ProgressBar pbLoad, circularProgressbar;
    RelativeLayout nutrition;
    LinearLayout shadow;
    RelativeLayout main;
    TextViewFont txtDate;
    ImageView arrowDown;
    LinearLayout changeDate;

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

        changeDate = (LinearLayout) v.findViewById(R.id.changeDate);
        txtDate = (TextViewFont) v.findViewById(R.id.txtDate);
        arrowDown = (ImageView) v.findViewById(R.id.arrowDown);

        circularProgressbar = (ProgressBar) v.findViewById(R.id.circularProgressbar);
        switch (position) {
            case R.id.nav_journal:
                pbLoad.setVisibility(View.VISIBLE);
                toolbar_blank.inflateMenu(R.menu.menu_main_journal);
                changeDate.setVisibility(View.VISIBLE);
                arrowDown.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_nutrition:
                nutrition.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_weight:
                changeDate.setVisibility(View.VISIBLE);
                toolbar_blank.inflateMenu(R.menu.menu_weight);
                txtDate.setText("Weight");
                break;
            case R.id.nav_health:
                changeDate.setVisibility(View.VISIBLE);
                txtDate.setText("Health");
                break;
            case R.id.nav_fitbit:
                changeDate.setVisibility(View.VISIBLE);
                txtDate.setText("Fitbit");
                toolbar_blank.inflateMenu(R.menu.menu_fit_bit_fragment);
                //
                break;
            default:
                break;
        }
        return v;
    }
}
