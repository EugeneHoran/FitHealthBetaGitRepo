package com.eugene.fithealthmaingit.Utilities;

import android.animation.Animator;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;


public class InitiateSearch {

    public static void handleToolBar(final Context context, final CardView search, final View view, final ListView listView, final EditText editText, final View line_divider) {
        final Animation fade_in = AnimationUtils.loadAnimation(context.getApplicationContext(), android.R.anim.fade_in);
        final Animation fade_out = AnimationUtils.loadAnimation(context.getApplicationContext(), android.R.anim.fade_out);
        if (search.getVisibility() == View.VISIBLE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                final Animator animatorHide = ViewAnimationUtils.createCircularReveal(search,
                    search.getWidth() - (int) convertDpToPixel(24, context),
                    (int) convertDpToPixel(23, context),
                    (float) Math.hypot(search.getWidth(), search.getHeight()),
                    0);
                animatorHide.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        view.startAnimation(fade_out);
                        view.setVisibility(View.INVISIBLE);
                        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        search.setVisibility(View.GONE);
                        listView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                animatorHide.setDuration(200);
                animatorHide.start();
            } else {
                ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
                view.startAnimation(fade_out);
                view.setVisibility(View.INVISIBLE);
                search.setVisibility(View.GONE);
            }
            editText.setText("");

            search.setEnabled(false);
        } else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                final Animator animator = ViewAnimationUtils.createCircularReveal(search,
                    search.getWidth() - (int) convertDpToPixel(24, context),
                    (int) convertDpToPixel(23, context),
                    0,
                    (float) Math.hypot(search.getWidth(), search.getHeight()));
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        listView.setVisibility(View.VISIBLE);
                        view.setVisibility(View.VISIBLE);
                        view.startAnimation(fade_in);
                        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                search.setVisibility(View.VISIBLE);
                if (search.getVisibility() == View.VISIBLE) {
                    animator.setDuration(300);
                    animator.start();
                    search.setEnabled(true);
                }
            } else {
                search.setVisibility(View.VISIBLE);
                search.setEnabled(true);
                view.setVisibility(View.VISIBLE);
                view.startAnimation(fade_in);
                listView.setVisibility(View.VISIBLE);
                ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
    }
}


