package com.eugene.fithealthmaingit.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.eugene.fithealthmaingit.R;

public class FragmentBlankLoading extends Fragment {
    int position = 0;
    Toolbar toolbar_blank;
    ProgressBar pbLoad;
    RelativeLayout nutrition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_blank_loading, container, false);
        Bundle extras = getArguments();
        if (extras != null) {
            position = extras.getInt("position");
        }
        pbLoad = (ProgressBar) v.findViewById(R.id.pbLoad);
        toolbar_blank = (Toolbar) v.findViewById(R.id.toolbar_blank);
        nutrition = (RelativeLayout) v.findViewById(R.id.nutrition);
        switch (position) {
            case 0:
                pbLoad.setVisibility(View.VISIBLE);
                toolbar_blank.inflateMenu(R.menu.menu_main_journal);
                SpannableString s = new SpannableString("Fit Journal");
                s.setSpan(new TypefaceSpan("Roboto-Thin.ttf"), 0, s.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                toolbar_blank.setTitle(s);
                toolbar_blank.setSubtitle("Today");
                break;
            case 1:
                nutrition.setVisibility(View.VISIBLE);
                break;
            case 2:
                toolbar_blank.inflateMenu(R.menu.menu_weight);
                toolbar_blank.setTitle("Your Weight");
                break;
            case 3:
                toolbar_blank.setTitle("Your Health");
                break;
            default:
                break;
        }
        return v;
    }

}
