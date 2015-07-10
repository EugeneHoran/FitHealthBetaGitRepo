package com.eugene.fithealthmaingit.Databases_Adapters_ListViews.CalSQLiteDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    /**
     * All Static variables
     */
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "contactsManager";

    // Contacts table name
    private static final String TABLE_CONTACTS = "contacts";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_CALORIES = "phone_number";
    private static final String KEY_DATE = "date";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_CALORIES + " DOUBLE,"
                + KEY_DATE + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new dailyCalorieIntake
    public void addContact(DailyCalorieIntake dailyCalorieIntake) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, dailyCalorieIntake.getName()); // DailyCalorieIntake Name
        values.put(KEY_CALORIES, dailyCalorieIntake.getPhoneNumber()); // DailyCalorieIntake Phone
        values.put(KEY_DATE, dailyCalorieIntake.getDate()); // Date
        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    // TODO:Not being used
    DailyCalorieIntake getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CONTACTS, new String[]{KEY_ID,
                        KEY_NAME, KEY_CALORIES, KEY_DATE}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        // return contact
        return new DailyCalorieIntake(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getDouble(2), cursor.getString(3));
    }

    // Get Contacts list by date
    public List<DailyCalorieIntake> getContactsByDate(String mDate) {
        List<DailyCalorieIntake> dailyCalorieIntakeList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE " + KEY_DATE + " LIKE " + "'%" + mDate + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DailyCalorieIntake dailyCalorieIntake = new DailyCalorieIntake();
                dailyCalorieIntake.setID(Integer.parseInt(cursor.getString(0)));
                dailyCalorieIntake.setName(cursor.getString(1));
                dailyCalorieIntake.setCalorieIntake(cursor.getDouble(2));
                dailyCalorieIntake.setDate(cursor.getString(3));
                // Adding dailyCalorieIntake to list
                dailyCalorieIntakeList.add(dailyCalorieIntake);
            } while (cursor.moveToNext());
        }
        // return contact list
        return dailyCalorieIntakeList;
    }

    // Getting All Contacts
    // TODO:Not being used
    public List<DailyCalorieIntake> getAllContacts() {
        List<DailyCalorieIntake> dailyCalorieIntakeList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DailyCalorieIntake dailyCalorieIntake = new DailyCalorieIntake();
                dailyCalorieIntake.setID(Integer.parseInt(cursor.getString(0)));
                dailyCalorieIntake.setName(cursor.getString(1));
                dailyCalorieIntake.setCalorieIntake(cursor.getDouble(2));
                dailyCalorieIntake.setDate(cursor.getString(3));
                // Adding dailyCalorieIntake to list
                dailyCalorieIntakeList.add(dailyCalorieIntake);
            } while (cursor.moveToNext());
        }

        // return contact list
        return dailyCalorieIntakeList;
    }

    // Updating single dailyCalorieIntake
    // TODO:Not being used
    public int updateCalories(DailyCalorieIntake dailyCalorieIntake) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, dailyCalorieIntake.getName());
        values.put(KEY_CALORIES, dailyCalorieIntake.getPhoneNumber());
        values.put(KEY_DATE, dailyCalorieIntake.getDate());
        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(dailyCalorieIntake.getID())});
    }

    // Deleting single dailyCalorieIntake
    public void deleteContact(DailyCalorieIntake dailyCalorieIntake) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[]{String.valueOf(dailyCalorieIntake.getID())});
        db.close();
    }
}