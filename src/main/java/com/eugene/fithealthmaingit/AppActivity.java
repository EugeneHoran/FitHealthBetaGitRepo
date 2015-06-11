package com.eugene.fithealthmaingit;

import android.app.Application;
import android.content.Context;

import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.FoodManual.LogManual;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood.LogMeal;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogQuickSearchData.LogQuickSearch;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogWeight.WeightLog;
import com.orm.androrm.DatabaseAdapter;
import com.orm.androrm.Model;

import java.util.ArrayList;
import java.util.List;

public class AppActivity extends Application {
    private static AppActivity appContext;

    public void onCreate() {
        super.onCreate();
        appContext = this;
        initializeDatabase();
    }

    public static Context context() {
        return appContext;
    }

    private void initializeDatabase() {
        List<Class<? extends Model>> models = new ArrayList<>(0);
        models.add(LogQuickSearch.class);
        models.add(LogMeal.class);
        models.add(LogManual.class);
        models.add(WeightLog.class);
        String dbName = this.getResources().getString(R.string.database_name);
        DatabaseAdapter.setDatabaseName(dbName);
        DatabaseAdapter adapter = new DatabaseAdapter(appContext);
        adapter.setModels(models);
    }
}

