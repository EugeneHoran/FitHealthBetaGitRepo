package com.eugene.fithealthmaingit.Databases_Adapters_ListViews.CalSQLiteDatabase;

public class DailyCalorieIntake {
    //private variables
    private int _id;
    private String _name;
    private Double _calorie_intake;
    private String _date;

    // Empty constructor
    public DailyCalorieIntake() {
    }

    // constructor for database
    public DailyCalorieIntake(int id, String name, Double _calorie_intake, String _date) {
        this._id = id;
        this._name = name;
        this._calorie_intake = _calorie_intake;
        this._date = _date;
    }

    // constructor for activity
    public DailyCalorieIntake(String name, Double _calorie_intake, String _date) {
        this._name = name;
        this._calorie_intake = _calorie_intake;
        this._date = _date;
    }

    // getting ID
    public int getID() {
        return this._id;
    }

    // setting id
    public void setID(int id) {
        this._id = id;
    }

    // getting name
    public String getName() {
        return this._name;
    }

    // setting name
    public void setName(String name) {
        this._name = name;
    }

    // getting phone number
    public Double getPhoneNumber() {
        return this._calorie_intake;
    }

    // setting phone number
    public void setCalorieIntake(Double phone_number) {
        this._calorie_intake = phone_number;
    }

    // getting date
    public String getDate() {
        return this._date;
    }

    // setting date
    public void setDate(String date) {
        this._date = date;
    }
}
