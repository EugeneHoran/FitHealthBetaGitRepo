package com.eugene.fithealthmaingit.HomeScreenWidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood.LogAdapterAll;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood.LogMeal;
import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.MainActivity;
import com.eugene.fithealthmaingit.Utilities.Globals;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * Implementation of App Widget functionality.
 */
public class FitHealthWidget extends AppWidgetProvider {
    public static String ACTION_SEARCH = "search_fragment";
    public static String ACTION_ADD = "add_fragment";
    Context mContext;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
        mContext = context;
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        Intent i = new Intent(context, MainActivity.class);
        i.setAction(FitHealthWidget.ACTION_SEARCH);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);
        Intent add = new Intent(context, MainActivity.class);
        add.setAction(FitHealthWidget.ACTION_ADD);
        PendingIntent pendingIntentAdd = PendingIntent.getActivity(context, 0, add, 0);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        Intent openApp = new Intent(context, MainActivity.class);
        PendingIntent piOPen = PendingIntent.getActivity(context, 0, openApp, 0);

        views.setOnClickPendingIntent(R.id.search, pendingIntent);
        views.setOnClickPendingIntent(R.id.add, pendingIntentAdd);
        views.setOnClickPendingIntent(R.id.openApp, piOPen);


        DecimalFormat df = new DecimalFormat("0");
        LogAdapterAll mLogAdapterAll = new LogAdapterAll(context, 0, LogMeal.logsByDate(new Date()));
        // Nutrition Consumed
        double mAllCaloriesConsumed = 0;
        double mAllFatConsumed = 0;
        double mAllCarbsConsumed = 0;
        double mAllProteinConsumed = 0;
        for (LogMeal logMeal : mLogAdapterAll.getLogs()) {
            mAllCaloriesConsumed += logMeal.getCalorieCount();
            mAllFatConsumed += logMeal.getFatCount();
            mAllCarbsConsumed += logMeal.getCarbCount();
            mAllProteinConsumed += logMeal.getProteinCount();
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        double mCalorieGoal = Double.valueOf(sharedPreferences.getString(Globals.USER_CALORIES_TO_REACH_GOAL, ""));
        double mFatGoal = Double.valueOf(sharedPreferences.getString(Globals.USER_DAILY_FAT, ""));
        double mCarbGoal = Double.valueOf(sharedPreferences.getString(Globals.USER_DAILY_CARBOHYDRATES, ""));
        double mProteinGoal = Double.valueOf(sharedPreferences.getString(Globals.USER_DAILY_PROTEIN, ""));

        views.setProgressBar(R.id.pbCal, Integer.valueOf(df.format(mCalorieGoal)), Integer.valueOf(df.format(mAllCaloriesConsumed)), false);
        views.setProgressBar(R.id.pbFat, Integer.valueOf(df.format(mFatGoal)), Integer.valueOf(df.format(mAllFatConsumed)), false);
        views.setProgressBar(R.id.pbCarb, Integer.valueOf(df.format(mCarbGoal)), Integer.valueOf(df.format(mAllCarbsConsumed)), false);
        views.setProgressBar(R.id.pbPro, Integer.valueOf(df.format(mProteinGoal)), Integer.valueOf(df.format(mAllProteinConsumed)), false);

        views.setTextViewText(R.id.txtRemainderCal, df.format(mCalorieGoal - mAllCaloriesConsumed));
        views.setTextViewText(R.id.txtRemainderFat, df.format(mFatGoal - mAllFatConsumed));
        views.setTextViewText(R.id.txtRemainderCarb, df.format(mCarbGoal - mAllCarbsConsumed));
        views.setTextViewText(R.id.txtRemainderPro, df.format(mProteinGoal - mAllProteinConsumed));

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}

