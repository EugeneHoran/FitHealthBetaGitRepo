package com.eugene.fithealthmaingit.UI.NavFragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eugene.fithealthmaingit.FitBit.FitBitActivityAdapter;
import com.eugene.fithealthmaingit.FitBit.FitBitActivityResult;
import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.Utilities.Globals;
import com.eugene.fithealthmaingit.Utilities.SetListHeight.SetActivityListHeight;
import com.temboo.Library.Fitbit.Activities.GetActivities;
import com.temboo.Library.Fitbit.Activities.GetActivityDailyGoals;
import com.temboo.Library.Fitbit.Devices.GetDevices;
import com.temboo.Library.Fitbit.Statistics.GetTimeSeriesByDateRange;
import com.temboo.core.TembooSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FragmentFitbit extends Fragment {

    View v;
    DecimalFormat df = new DecimalFormat("0");
    DecimalFormat dfT = new DecimalFormat("0.00");
    DecimalFormat dfTwo = new DecimalFormat("00");
    private SharedPreferences sharedPreferences;
    public static String customerKey = "d067e7c1b4d326a71add49b73f573f2f";
    public static String customerSecret = "4ea2779049ad25c145b2c83b0496a207";
    private TextView stepsGoal;
    private TextView stepsWalked;
    private TextView diatanceGoal;
    private TextView diatanceWalked;
    TextView device;
    TextView date;
    TextView lastSync;
    TextView caloriesBurned;
    ProgressBar progressSteps;
    ProgressBar progressDistance;
    ProgressBar fitbitRefreshing, fitbitLoadingActivities;
    Toolbar toolbar;
    ImageView deviceType, batteryStatus;
    int year = 0;
    int month = 0;
    int day = 0;
    String dateChange;
    TextView sedentary;
    TextView light;
    TextView fairly;
    TextView very;
    /**
     * ACTIVITIES
     */
    private ArrayList<FitBitActivityResult> mItem;
    TextView noActivities;
    ListView listActivities;
    FitBitActivityAdapter fitBitActivityAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_fit_bit, container, false);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        toolbar = (Toolbar) v.findViewById(R.id.toolbar_fitbit);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.openNavigationDrawer();
            }
        });
        fitbitRefreshing = (ProgressBar) v.findViewById(R.id.fitbitRefreshing);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_refresh) {
                    toolbar.getMenu().clear();
                    fitbitRefreshing.setVisibility(View.VISIBLE);
                    new FitbitGetActivityStats().execute();
                }
                return false;
            }
        });
        fitbitRefreshing.setVisibility(View.VISIBLE);
        stepsGoal = (TextView) v.findViewById(R.id.stepsGoal);
        stepsWalked = (TextView) v.findViewById(R.id.stepsWalked);
        diatanceGoal = (TextView) v.findViewById(R.id.diatanceGoal);
        diatanceWalked = (TextView) v.findViewById(R.id.diatanceWalked);
        device = (TextView) v.findViewById(R.id.device);
        deviceType = (ImageView) v.findViewById(R.id.deviceType);
        batteryStatus = (ImageView) v.findViewById(R.id.batteryStatus);
        lastSync = (TextView) v.findViewById(R.id.lastSync);
        caloriesBurned = (TextView) v.findViewById(R.id.caloriesBurned);
        date = (TextView) v.findViewById(R.id.date);
        date.setText("Today, " + DateFormat.format("MMM d", new Date()));
        progressSteps = (ProgressBar) v.findViewById(R.id.progressSteps);
        progressDistance = (ProgressBar) v.findViewById(R.id.progressDistance);
        fitbitLoadingActivities = (ProgressBar) v.findViewById(R.id.fitbitLoadingActivities);

        /**
         * Activities
         */
        sedentary = (TextView) v.findViewById(R.id.sedentary);
        light = (TextView) v.findViewById(R.id.light);
        fairly = (TextView) v.findViewById(R.id.fairly);
        very = (TextView) v.findViewById(R.id.very);

        mItem = new ArrayList<>();
        noActivities = (TextView) v.findViewById(R.id.noActivities);
        listActivities = (ListView) v.findViewById(R.id.listActivities);
        fitBitActivityAdapter = new FitBitActivityAdapter(getActivity(), mItem);
        listActivities.setAdapter(fitBitActivityAdapter);
        SetActivityListHeight.setListViewHeight(listActivities);

        LoadSavedData();
        new FitbitGetActivityStats().execute();
        Log.e("tken", sharedPreferences.getString("FITBIT_ACCESS_TOKEN", ""));
        Log.e("secret", sharedPreferences.getString("FITBIT_ACCESS_TOKEN_SECRET", ""));
        return v;
    }

    private void updateActivityList() {
        if (mItem.size() > 0) {
            listActivities.setVisibility(View.VISIBLE);
            noActivities.setVisibility(View.GONE);
        } else {
            listActivities.setVisibility(View.GONE);
            noActivities.setVisibility(View.VISIBLE);
        }
    }

    String distance = "0";
    String steps = "0";
    String distanceGO = "0";
    String stepsGo = "0";
    String productName = "No Device";
    String lastSyncTime = "No Data";
    String battery = "No Data";
    int calories = 0;
    String sedentaryMinutes = "0";
    String lightlyActiveMinutes = "0";
    String fairlyActiveMinutes = "0";
    String veryActiveMinutes = "0";
    String caloriesOut = "0";

    public class FitbitGetActivityStats extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {

                // Set the times
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH) + 1;
                day = cal.get(Calendar.DAY_OF_MONTH);
                dateChange = year + "-" + dfTwo.format(month) + "-" + dfTwo.format(day);

                // Initiate Temboo Session
                TembooSession session = new TembooSession("eugeneh", "FitHealth", "Gj74tL9HVPoTM84UvJaJjtMaMslhVWE7");
                /**
                 * Device connected information
                 */
                GetDevices getDevicesChoreo = new GetDevices(session);
                GetDevices.GetDevicesInputSet getDevicesInputs = getDevicesChoreo.newInputSet();
                getDevicesInputs.set_AccessToken(sharedPreferences.getString("FITBIT_ACCESS_TOKEN", ""));
                getDevicesInputs.set_AccessTokenSecret(sharedPreferences.getString("FITBIT_ACCESS_TOKEN_SECRET", ""));
                getDevicesInputs.set_ConsumerSecret(Globals.CUSTOMER_SECRET);
                getDevicesInputs.set_ConsumerKey(Globals.CUSTOMER_KEY);
                GetDevices.GetDevicesResultSet getDevicesResults = getDevicesChoreo.execute(getDevicesInputs);
                try {
                    JSONObject devices = new JSONArray(getDevicesResults.get_Response()).getJSONObject(0);
                    productName = devices.getString("deviceVersion");
                    lastSyncTime = devices.getString("lastSyncTime");
                    battery = devices.getString("battery");
                } catch (Exception e) {
                    Log.e("ERROR_DEVICE", e.toString());
                }

                /**
                 * Goals
                 */
                GetActivityDailyGoals getActivityDailyGoalsChoreo = new GetActivityDailyGoals(session);
                GetActivityDailyGoals.GetActivityDailyGoalsInputSet getActivityDailyGoalsInputs = getActivityDailyGoalsChoreo.newInputSet();

                getActivityDailyGoalsInputs.set_AccessToken(sharedPreferences.getString("FITBIT_ACCESS_TOKEN", ""));
                getActivityDailyGoalsInputs.set_AccessTokenSecret(sharedPreferences.getString("FITBIT_ACCESS_TOKEN_SECRET", ""));
                getActivityDailyGoalsInputs.set_ConsumerSecret(customerSecret);
                getActivityDailyGoalsInputs.set_ConsumerKey(customerKey);

                GetActivityDailyGoals.GetActivityDailyGoalsResultSet getActivityDailyGoalsResults = getActivityDailyGoalsChoreo.execute(getActivityDailyGoalsInputs);
                try {
                    steps = getActivityDailyGoalsResults.get_Steps();
                    double distanceKmToMiles = Double.valueOf(getActivityDailyGoalsResults.get_Distance()) * 0.62137;
                    distance = String.valueOf(distanceKmToMiles);
                } catch (Exception e) {
                    Log.e("ERROR_GOALS", e.toString());
                }
                /**
                 * Steps
                 */
                GetTimeSeriesByDateRange getTimeSeriesByDateRangeChoreo = new GetTimeSeriesByDateRange(session);
                GetTimeSeriesByDateRange.GetTimeSeriesByDateRangeInputSet getTimeSeriesByDateRangeInputs = getTimeSeriesByDateRangeChoreo.newInputSet();
                getTimeSeriesByDateRangeInputs.set_EndDate(dateChange);
                getTimeSeriesByDateRangeInputs.set_StartDate(dateChange);
                getTimeSeriesByDateRangeInputs.set_ResourcePath("activities/log/steps");

                getTimeSeriesByDateRangeInputs.set_ConsumerSecret(Globals.CUSTOMER_SECRET);
                getTimeSeriesByDateRangeInputs.set_ConsumerKey(Globals.CUSTOMER_KEY);
                getTimeSeriesByDateRangeInputs.set_AccessToken(sharedPreferences.getString("FITBIT_ACCESS_TOKEN", ""));
                getTimeSeriesByDateRangeInputs.set_AccessTokenSecret(sharedPreferences.getString("FITBIT_ACCESS_TOKEN_SECRET", ""));

                GetTimeSeriesByDateRange.GetTimeSeriesByDateRangeResultSet getTimeSeriesByDateRangeResults = getTimeSeriesByDateRangeChoreo.execute(getTimeSeriesByDateRangeInputs);
                try {
                    JSONObject logSteps = new JSONObject(getTimeSeriesByDateRangeResults.get_Response());
                    JSONArray jsonArray = logSteps.getJSONArray("activities-log-steps");
                    JSONObject stepsWalked = jsonArray.getJSONObject(0);
                    stepsGo = stepsWalked.get("value").toString();
                } catch (JSONException e) {
                    Log.e("ERROR_STEPS", e.toString());
                }

                /**
                 * Distance
                 */
                GetTimeSeriesByDateRange getTimeSeriesByDateRangeChoreoDistance = new GetTimeSeriesByDateRange(session);
                GetTimeSeriesByDateRange.GetTimeSeriesByDateRangeInputSet getTimeSeriesByDateRangeInputsDistance = getTimeSeriesByDateRangeChoreoDistance.newInputSet();
                getTimeSeriesByDateRangeInputsDistance.set_EndDate(dateChange);
                getTimeSeriesByDateRangeInputsDistance.set_StartDate(dateChange);
                getTimeSeriesByDateRangeInputsDistance.set_ResourcePath("activities/log/distance");

                getTimeSeriesByDateRangeInputsDistance.set_ConsumerSecret(Globals.CUSTOMER_SECRET);
                getTimeSeriesByDateRangeInputsDistance.set_ConsumerKey(Globals.CUSTOMER_KEY);
                getTimeSeriesByDateRangeInputsDistance.set_AccessToken(sharedPreferences.getString("FITBIT_ACCESS_TOKEN", ""));
                getTimeSeriesByDateRangeInputsDistance.set_AccessTokenSecret(sharedPreferences.getString("FITBIT_ACCESS_TOKEN_SECRET", ""));

                GetTimeSeriesByDateRange.GetTimeSeriesByDateRangeResultSet getTimeSeriesByDateRangeResultsDistance = getTimeSeriesByDateRangeChoreoDistance.execute(getTimeSeriesByDateRangeInputsDistance);
                try {
                    JSONObject logDistance = new JSONObject(getTimeSeriesByDateRangeResultsDistance.get_Response());
                    JSONArray jsonArrayDistance = logDistance.getJSONArray("activities-log-distance");
                    JSONObject distance = jsonArrayDistance.getJSONObject(0);
                    double distanceWalkedKmToMiles = Double.valueOf(distance.get("value").toString()) * 0.62137;
                    distanceGO = String.valueOf(distanceWalkedKmToMiles);
                } catch (Exception e) {
                    Log.e("ERROR_DISTANCE", e.toString());
                }

                /**
                 * Calories Burned
                 */
                GetTimeSeriesByDateRange getTimeSeriesByDateRangeChoreoCalories = new GetTimeSeriesByDateRange(session);
                GetTimeSeriesByDateRange.GetTimeSeriesByDateRangeInputSet getTimeSeriesByDateRangeInputsCalories = getTimeSeriesByDateRangeChoreoCalories.newInputSet();
                getTimeSeriesByDateRangeInputsCalories.set_EndDate(dateChange);
                getTimeSeriesByDateRangeInputsCalories.set_StartDate(dateChange);
                getTimeSeriesByDateRangeInputsCalories.set_ResourcePath("activities/log/activityCalories");
                getTimeSeriesByDateRangeInputsCalories.set_ResponseFormat("json");

                getTimeSeriesByDateRangeInputsCalories.set_ConsumerSecret(Globals.CUSTOMER_SECRET);
                getTimeSeriesByDateRangeInputsCalories.set_ConsumerKey(Globals.CUSTOMER_KEY);
                getTimeSeriesByDateRangeInputsCalories.set_AccessToken(sharedPreferences.getString("FITBIT_ACCESS_TOKEN", ""));
                getTimeSeriesByDateRangeInputsCalories.set_AccessTokenSecret(sharedPreferences.getString("FITBIT_ACCESS_TOKEN_SECRET", ""));

                GetTimeSeriesByDateRange.GetTimeSeriesByDateRangeResultSet getTimeSeriesByDateRangeResultsCalories = getTimeSeriesByDateRangeChoreo.execute(getTimeSeriesByDateRangeInputsCalories);
                try {
                    JSONObject jsonObject = new JSONObject(getTimeSeriesByDateRangeResultsCalories.get_Response());
                    JSONArray lifetime = jsonObject.getJSONArray("activities-log-activityCalories");
                    JSONObject test = lifetime.getJSONObject(0);
                    calories = test.getInt("value");
                } catch (JSONException e) {
                    Log.e("ERROR_CALORIES", e.toString());
                    e.printStackTrace();
                }
                /**
                 *Activity
                 */
                GetActivities getActivitiesChoreo = new GetActivities(session);
                GetActivities.GetActivitiesInputSet getActivitiesInputs = getActivitiesChoreo.newInputSet();
                getActivitiesInputs.set_Date(dateChange);

                getActivitiesInputs.set_ConsumerSecret(Globals.CUSTOMER_SECRET);
                getActivitiesInputs.set_ConsumerKey(Globals.CUSTOMER_KEY);
                getActivitiesInputs.set_AccessToken(sharedPreferences.getString("FITBIT_ACCESS_TOKEN", ""));
                getActivitiesInputs.set_AccessTokenSecret(sharedPreferences.getString("FITBIT_ACCESS_TOKEN_SECRET", ""));

                GetActivities.GetActivitiesResultSet getActivitiesResults = getActivitiesChoreo.execute(getActivitiesInputs);
                try {
                    JSONObject jsonResponse = new JSONObject(getActivitiesResults.get_Response());
                    JSONObject activityGoals = jsonResponse.getJSONObject("goals");
                    caloriesOut = activityGoals.getString("caloriesOut");
                    JSONArray activities = jsonResponse.getJSONArray("activities");
                    for (int i = 0; i < activities.length(); i++) {
                        JSONObject activity = activities.getJSONObject(i);
                        String activity_name = activity.getString("name");
                        String caloriesBurned = activity.getString("calories");
                        mItem.add(new FitBitActivityResult(activity_name, caloriesBurned));
                    }
                    JSONObject summary = jsonResponse.getJSONObject("summary");
                    sedentaryMinutes = summary.getString("sedentaryMinutes");
                    lightlyActiveMinutes = summary.getString("lightlyActiveMinutes");
                    fairlyActiveMinutes = summary.getString("fairlyActiveMinutes");
                    veryActiveMinutes = summary.getString("veryActiveMinutes");
                } catch (Exception e) {
                    Log.e("ERROR_ACTVITY", e.toString());
                }
            } catch (Exception e) {
                Log.e("ERROR_MAIN", e.toString());
                e.printStackTrace();
            }
            return null;
        }

        Date convertedDate;

        @Override
        protected void onPostExecute(String result) {
            stepsGoal.setText(steps);
            stepsWalked.setText(stepsGo);
            diatanceGoal.setText(dfT.format(Double.valueOf(distance)));
            diatanceWalked.setText(dfT.format(Double.valueOf(distanceGO)));
            progressSteps.setMax(Integer.valueOf(steps));
            progressSteps.setProgress(Integer.valueOf(stepsGo));

            double distanceMax = Double.valueOf(distance) * 100;
            double distanceCurrent = Double.valueOf(distanceGO) * 100;
            progressDistance.setMax(Integer.valueOf(df.format(distanceMax)));
            progressDistance.setProgress(Integer.valueOf(df.format(distanceCurrent)));

            device.setText(productName);
            String dateString = lastSyncTime;
            String str = dateString.replace('T', ' ');
            String cutString = str.substring(0, 19);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            convertedDate = new Date();

            if (productName.equals("Flex")) {
                deviceType.setImageResource(R.drawable.flex);
            } else if (productName.equals("Zip")) {
                deviceType.setImageResource(R.drawable.zip);
            } else if (productName.equals("One")) {
                deviceType.setImageResource(R.drawable.one);
            } else if (productName.equals("Charge")) {
                deviceType.setImageResource(R.drawable.charge);
            } else if (productName.equals("ChargeHR")) {
                deviceType.setImageResource(R.drawable.chargehr);
            } else if (productName.equals("Surge")) {
                deviceType.setImageResource(R.drawable.surge);
            } else {
                deviceType.setImageResource(R.drawable.flex);
            }

            if (battery.equals("Full")) {
                batteryStatus.setImageResource(R.mipmap.ic_battery_full);
            } else if (battery.equals("High")) {
                batteryStatus.setImageResource(R.mipmap.ic_battery_high);
            } else if (battery.equals("Medium")) {
                batteryStatus.setImageResource(R.mipmap.ic_battery_medium);
            } else if (battery.equals("Low")) {
                batteryStatus.setImageResource(R.mipmap.ic_battery_low);
            }

            caloriesBurned.setText(String.valueOf(calories));

            savePreferences("DEVICE_NAME", productName);
            savePreferences("DEVICE_SYNC", lastSync.getText().toString());

            savePreferences("STEPS_GOAL", steps);
            savePreferences("STEPS_ACTUAL", stepsGo);

            savePreferences("DISTANCE_GOAL", dfT.format(Double.valueOf(distance)));
            savePreferences("DISTANCE_ACTUAL", dfT.format(Double.valueOf(distanceGO)));

            savePreferences("CALORIES_BURNED", String.valueOf(calories));

            /**
             * Activity
             */


            savePreferences("SEDENTARY", sedentaryMinutes);
            savePreferences("LIGHT_ACTIVITY", lightlyActiveMinutes);
            savePreferences("FAIR_ACTIVITY", fairlyActiveMinutes);
            savePreferences("VERY_ACTIVITY", veryActiveMinutes);

            fitbitRefreshing.setVisibility(View.GONE);
            toolbar.inflateMenu(R.menu.menu_fit_bit_fragment);
            fitbitLoadingActivities.setVisibility(View.GONE);
            fitBitActivityAdapter.notifyDataSetChanged();
            updateActivityList();

            sedentary.setText(ConvertTime(Integer.valueOf(sedentaryMinutes)));
            light.setText(ConvertTime(Integer.valueOf(lightlyActiveMinutes)));
            fairly.setText(ConvertTime(Integer.valueOf(fairlyActiveMinutes)));
            very.setText(ConvertTime(Integer.valueOf(veryActiveMinutes)));
        }
    }

    private String ConvertTime(int hour) {
        int hoursS = Integer.valueOf(hour) / 60;
        int minutesS = Integer.valueOf(hour) % 60;
        return hoursS + " hours " + minutesS + " minutes";
    }

    private void LoadSavedData() {
        stepsGoal.setText(sharedPreferences.getString("STEPS_GOAL", "..."));
        stepsWalked.setText(sharedPreferences.getString("STEPS_ACTUAL", "..."));
        diatanceGoal.setText(sharedPreferences.getString("DISTANCE_GOAL", "..."));
        diatanceWalked.setText(sharedPreferences.getString("DISTANCE_ACTUAL", "..."));
        progressSteps.setMax(Integer.valueOf(sharedPreferences.getString("STEPS_GOAL", "0")));
        progressSteps.setProgress(Integer.valueOf(sharedPreferences.getString("STEPS_ACTUAL", "0")));
        double distanceMax = Double.valueOf(sharedPreferences.getString("DISTANCE_GOAL", "0")) * 100;
        double distanceCurrent = Double.valueOf(sharedPreferences.getString("DISTANCE_ACTUAL", "0")) * 100;
        progressDistance.setMax(Integer.valueOf(df.format(distanceMax)));
        progressDistance.setProgress(Integer.valueOf(df.format(distanceCurrent)));

        lastSync.setText(sharedPreferences.getString("DEVICE_SYNC", "Last Sync: "));
        device.setText(sharedPreferences.getString("DEVICE_NAME", "Device"));
        productName = sharedPreferences.getString("DEVICE_NAME", "Device");
        if (productName.equals("Flex")) {
            deviceType.setImageResource(R.drawable.flex);
        } else if (productName.equals("Zip")) {
            deviceType.setImageResource(R.drawable.zip);
        } else if (productName.equals("One")) {
            deviceType.setImageResource(R.drawable.one);
        } else if (productName.equals("Charge")) {
            deviceType.setImageResource(R.drawable.charge);
        } else if (productName.equals("Charge HR")) {
            deviceType.setImageResource(R.drawable.chargehr);
        } else if (productName.equals("Surge")) {
            deviceType.setImageResource(R.drawable.surge);
        } else {
            deviceType.setImageResource(R.drawable.flex);
        }

        caloriesBurned.setText(sharedPreferences.getString("CALORIES_BURNED", "0"));

        sedentary.setText(ConvertTime(Integer.valueOf(sharedPreferences.getString("SEDENTARY", "0"))));
        light.setText(ConvertTime(Integer.valueOf(sharedPreferences.getString("LIGHT_ACTIVITY", "0"))));
        fairly.setText(ConvertTime(Integer.valueOf(sharedPreferences.getString("FAIR_ACTIVITY", "0"))));
        very.setText(ConvertTime(Integer.valueOf(sharedPreferences.getString("VERY_ACTIVITY", "0"))));

    }

    private void savePreferences(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value).apply();
    }

    private FragmentCallbacks mCallbacks;

    public interface FragmentCallbacks {
        void openNavigationDrawer();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (FragmentCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement Fragment One.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
}
