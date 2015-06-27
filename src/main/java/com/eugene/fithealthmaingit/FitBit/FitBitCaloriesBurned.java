package com.eugene.fithealthmaingit.FitBit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.eugene.fithealthmaingit.MainActivityController;
import com.temboo.Library.Fitbit.Statistics.GetTimeSeriesByDateRange;
import com.temboo.core.TembooSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

public class FitBitCaloriesBurned extends AsyncTask<String, Void, String> {

    private SharedPreferences sharedPreferences;
    private Context mCon;
    int calories = 0;
    int year = 0;
    int month = 0;
    int day = 0;
    DecimalFormat df = new DecimalFormat("00");
    String dateChange;

    public FitBitCaloriesBurned(Context con, Date date) {
        mCon = con;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        day = cal.get(Calendar.DAY_OF_MONTH);
        dateChange = year + "-" + df.format(month) + "-" + df.format(day);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(con);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ((MainActivityController) mCon).FitBitLoading();
    }

    @Override
    protected String doInBackground(String... params) {

        try {

            /**
             * Getting activity data
             */
            TembooSession session = new TembooSession("eugeneh", "FitHealth", "Gj74tL9HVPoTM84UvJaJjtMaMslhVWE7");
            GetTimeSeriesByDateRange getTimeSeriesByDateRangeChoreo = new GetTimeSeriesByDateRange(session);
            GetTimeSeriesByDateRange.GetTimeSeriesByDateRangeInputSet getTimeSeriesByDateRangeInputs = getTimeSeriesByDateRangeChoreo.newInputSet();
            getTimeSeriesByDateRangeInputs.set_EndDate(dateChange);
            getTimeSeriesByDateRangeInputs.set_StartDate(dateChange);
            getTimeSeriesByDateRangeInputs.set_ConsumerSecret("4ea2779049ad25c145b2c83b0496a207");
            getTimeSeriesByDateRangeInputs.set_ResourcePath("activities/log/activityCalories");
            getTimeSeriesByDateRangeInputs.set_ConsumerKey("d067e7c1b4d326a71add49b73f573f2f");
            getTimeSeriesByDateRangeInputs.set_AccessToken(sharedPreferences.getString("FITBIT_ACCESS_TOKEN", ""));
            getTimeSeriesByDateRangeInputs.set_ResponseFormat("json");
            getTimeSeriesByDateRangeInputs.set_AccessTokenSecret(sharedPreferences.getString("FITBIT_ACCESS_TOKEN_SECRET", ""));
            GetTimeSeriesByDateRange.GetTimeSeriesByDateRangeResultSet getTimeSeriesByDateRangeResults = getTimeSeriesByDateRangeChoreo.execute(getTimeSeriesByDateRangeInputs);
            try {
                JSONObject jsonObject = new JSONObject(getTimeSeriesByDateRangeResults.get_Response());
                JSONArray lifetime = jsonObject.getJSONArray("activities-log-activityCalories");
                JSONObject test = lifetime.getJSONObject(0);
                calories = test.getInt("value");
                Log.e("DATE", test.getString("dateTime"));
            } catch (JSONException e) {
                Log.e("ERROR", e.toString());
                e.printStackTrace();
            }
        } catch (Exception e) {
            Log.e("Test", e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        ((MainActivityController) mCon).FitBitCaloriesBurned(String.valueOf(calories));
    }
}
