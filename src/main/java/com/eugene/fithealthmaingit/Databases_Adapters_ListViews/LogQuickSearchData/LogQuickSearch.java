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

package com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogQuickSearchData;

import android.content.Context;

import com.eugene.fithealthmaingit.AppActivity;
import com.orm.androrm.CharField;
import com.orm.androrm.DateField;
import com.orm.androrm.Filter;
import com.orm.androrm.Model;
import com.orm.androrm.QuerySet;

import java.util.Date;
import java.util.List;

/**
 * This class is the database for storing the users entry information. The quick search logs
 * is a database object from the androrm package
 */
public class LogQuickSearch extends Model {

    protected CharField meal_name;
    protected DateField date;

    public LogQuickSearch() {
        super(true);
        meal_name = new CharField();
        date = new DateField();
    }

    public static LogQuickSearch create(String mealName, Date mDate) {
        LogQuickSearch LogQuickSearch = new LogQuickSearch();
        LogQuickSearch.setName(mealName);
        LogQuickSearch.setDate(mDate);
        LogQuickSearch.save();
        return LogQuickSearch;
    }


    public String getName() {
        return meal_name.get();
    }

    public void setName(String count) {
        meal_name.set(count);
    }

    public void setDate(Date d) {
        date.set(d);
    }

    public Date getDate() {
        return date.get();
    }

    private static String formatProjectForQuery(String name) {
        String name1 = name;
        return name1;
    }

    public static List<LogQuickSearch> logSortByProjectType(String Key_) {
        String query_string = formatProjectForQuery(Key_);
        Filter filter = new Filter();
        filter.contains("meal_name", query_string);
        return LogQuickSearch.objects().filter(filter).orderBy("meal_name").toList();
    }

    public boolean save() {
        int id = LogQuickSearch.objects(context(), LogQuickSearch.class).all().count() + 1;
        return this.save(context(), id);
    }

    public boolean edit() {
        return this.save(context());
    }

    public boolean delete() {
        return this.delete(context());
    }

    public static List<LogQuickSearch> all() {

        return LogQuickSearch.objects().all().orderBy("-date").toList();
    }

    public static List<LogQuickSearch> FilterByName(String name) {
        Filter filter = new Filter();
        filter.contains("meal_name", name);
        return LogQuickSearch.objects().filter(filter).orderBy("meal_name").toList();
    }

    public static QuerySet<LogQuickSearch> objects() {

        return LogQuickSearch.objects(context(), LogQuickSearch.class);
    }

    /**
     * Get application context
     *
     * @return
     */
    private static Context context() {

        return AppActivity.context();
    }
}
