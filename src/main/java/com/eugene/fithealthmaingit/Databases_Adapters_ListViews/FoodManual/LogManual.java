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

package com.eugene.fithealthmaingit.Databases_Adapters_ListViews.FoodManual;

import android.content.Context;

import com.eugene.fithealthmaingit.AppActivity;
import com.orm.androrm.CharField;
import com.orm.androrm.DateField;
import com.orm.androrm.DoubleField;
import com.orm.androrm.Filter;
import com.orm.androrm.Model;
import com.orm.androrm.QuerySet;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * This class is the database for storing the users entry information. The manual logs
 * is a database object from the androrm package
 */
public class LogManual extends Model {
    protected CharField mealId;
    protected CharField mealChoice;
    protected CharField mealOrder;
    protected CharField mealName;
    protected DoubleField calories;
    protected DoubleField fat;
    protected DoubleField saturatedFat;
    protected DoubleField cholesterol;
    protected DoubleField sodium;
    protected DoubleField carbs;
    protected DoubleField fiber;
    protected DoubleField sugars;
    protected DoubleField protein;
    protected DoubleField vitA;
    protected DoubleField vitC;
    protected DoubleField calcium;
    protected DoubleField iron;
    protected DoubleField servingSize;
    protected CharField mealServing;
    protected DateField date;
    protected CharField mFavorite;
    protected CharField brand;
    protected CharField manualEntry;

    public LogManual() {
        super(true);
        mealId = new CharField();
        mealChoice = new CharField();
        mealOrder = new CharField();
        mealName = new CharField(20);
        calories = new DoubleField();
        fat = new DoubleField();
        saturatedFat = new DoubleField();
        cholesterol = new DoubleField();
        sodium = new DoubleField();
        carbs = new DoubleField();
        fiber = new DoubleField();
        sugars = new DoubleField();
        protein = new DoubleField();
        vitA = new DoubleField();
        vitC = new DoubleField();
        calcium = new DoubleField();
        iron = new DoubleField();
        servingSize = new DoubleField();
        mealServing = new CharField();
        date = new DateField();
        mFavorite = new CharField();
        brand = new CharField();
        manualEntry = new CharField();
    }

    private static String formatProjectForQuery(String name) {
        return name;
    }

    public static List<LogManual> logSortByMealChoice(String b, Date d) {
        String query_date = formatDateForQuery(d);
        String query_meal = formatProjectForQuery(b);
        Filter filter = new Filter();
        filter.is("mealChoice", query_meal);
        filter.contains("date", query_date);
        return LogManual.objects().filter(filter).orderBy("-date").toList();
    }


    public static List<LogManual> logSortByManual(String b) {
        String query_meal = formatProjectForQuery(b);
        Filter filter = new Filter();
        filter.is("manualEntry", query_meal);
        return LogManual.objects().filter(filter).toList();
    }

    public static List<LogManual> logSortByFavorite(String b) {
        Filter filter = new Filter();
        filter.is("mFavorite", b);
        return LogManual.objects().filter(filter).toList();
    }

    public String getManualEntry() {
        return manualEntry.get();
    }

    public void setManualEntry(String meal) {
        manualEntry.set(meal);
    }

    public String getMealId() {
        return mealId.get();
    }

    public void setMealId(String meal) {
        mealId.set(meal);
    }

    public String getBrand() {
        return brand.get();
    }

    public void setBrand(String meal) {
        brand.set(meal);
    }

    public String getFavorite() {
        return mFavorite.get();
    }


    public void setFavorite(String meal) {
        mFavorite.set(meal);
    }

    public String getMealServing() {
        return mealServing.get();
    }

    public void setMealServing(String meal) {
        mealServing.set(meal);
    }

    public String getMealChoice() {
        return mealChoice.get();
    }

    public void setMealChoice(String meal) {
        mealChoice.set(meal);
    }

    public String getOrder() {
        return mealOrder.get();
    }

    public void setOrder(String meal) {
        mealOrder.set(meal);
    }

    public String getMealName() {
        return mealName.get();
    }

    public void setMealName(String meal) {
        mealName.set(meal);
    }

    public double getCalorieCount() {
        return calories.get();
    }

    public void setCalorieCount(double count) {
        calories.set(count);
    }

    public double getFatCount() {
        return fat.get();
    }

    public void setFatCount(double count1) {
        fat.set(count1);
    }

    public double getSaturatedFat() {
        return saturatedFat.get();
    }

    public void setSaturatedFat(double count) {
        saturatedFat.set(count);
    }

    public double getCholesterol() {
        return saturatedFat.get();
    }

    public void setCholesterol(double count) {
        cholesterol.set(count);
    }

    public double getSodium() {
        return sodium.get();
    }

    public void setSodium(double count) {
        sodium.set(count);
    }

    public double getCarbCount() {
        return carbs.get();
    }

    public void setCarbCount(double count2) {
        carbs.set(count2);
    }

    public double getFiber() {
        return fiber.get();
    }

    public void setFiber(double count) {
        fiber.set(count);
    }

    public double getSugars() {
        return sugars.get();
    }

    public void setSugars(double count) {
        sugars.set(count);
    }

    public double getProteinCount() {
        return protein.get();
    }

    public void setProteinCount(double count) {
        protein.set(count);
    }

    public double getVitA() {
        return vitA.get();
    }

    public void setVitA(double count) {
        vitA.set(count);
    }

    public double getVitC() {
        return vitC.get();
    }

    public void setVitC(double count) {
        vitC.set(count);
    }

    public double getCalcium() {
        return calcium.get();
    }

    public void setCalcium(double count) {
        calcium.set(count);
    }

    public double getIron() {
        return iron.get();
    }

    public void setIron(double count) {
        iron.set(count);
    }

    public double getServingSize() {
        return servingSize.get().doubleValue();
    }

    public void setServingSize(double count) {
        servingSize.set(count);
    }

    public Date getDate() {
        return date.get();
    }


    public void setDate(Date d) {
        date.set(d);
    }

    public boolean save() {
        int min = 65;
        int max = 2000000;
        Random r = new Random();
        int i1 = r.nextInt(max - min + 1) + min;
        return this.save(context(), i1);
    }

    public boolean edit() {
        return this.save(context());
    }

    public boolean delete() {
        return this.delete(context());
    }

    public static List<LogManual> logsById(String s) {
        Filter filter = new Filter();
        filter.contains("mealId", s);
        return LogManual.objects().filter(filter).orderBy("mealId").toList();
    }

    /**
     * Returns a list of all logs in a given date
     *
     * @param date
     * @return
     */
    public static List<LogManual> logsByDate(Date date) {
        String query_string = formatDateForQuery(date);
        Filter filter = new Filter();
        filter.contains("date", query_string);
        return LogManual.objects().filter(filter).orderBy("date").toList();
    }

    public static List<LogManual> logsMealName(String mealType) {
        Filter filter = new Filter();
        filter.contains("mealName", mealType);
        return LogManual.objects().filter(filter).toList();
    }

    private static String formatDateForQuery(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String dayString = String.valueOf(day);
        String monthString = String.valueOf(month);
        if (day < 10) {
            dayString = "0" + dayString;
        }
        if (month < 10) {
            monthString = "0" + monthString;
        }
        return year + "-" + monthString + "-" + dayString;
    }

    /**
     * Returns a list of all Logs sorted in descending order of date
     *
     * @return
     */
    public static List<LogManual> all() {
        return LogManual.objects().all().orderBy("date").toList();
    }

    /**
     * Builds up query for logs database
     *
     * @return
     */
    public static QuerySet<LogManual> objects() {
        return LogManual.objects(context(), LogManual.class);
    }

    private static Context context() {
        return AppActivity.context();
    }
}

