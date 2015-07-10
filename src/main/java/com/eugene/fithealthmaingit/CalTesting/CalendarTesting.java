package com.eugene.fithealthmaingit.CalTesting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.LineChartView;
import com.db.chart.view.XController;
import com.db.chart.view.YController;
import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.CalSQLiteDatabase.DailyCalorieIntake;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.CalSQLiteDatabase.DailyCalorieAdapter;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.CalSQLiteDatabase.DatabaseHandler;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood.LogAdapterAll;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood.LogMeal;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogWeight.WeightLog;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogWeight.WeightLogAdapter;
import com.eugene.fithealthmaingit.MainActivity;
import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.Utilities.DateCompare;
import com.eugene.fithealthmaingit.Utilities.Equations;
import com.eugene.fithealthmaingit.Utilities.Globals;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.apache.commons.math3.util.Precision;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CalendarTesting extends AppCompatActivity implements SlidingUpPanelLayout.PanelSlideListener {
    private CaldroidFragment caldroidFragment;

    private DatabaseHandler db;
    List<DailyCalorieIntake> dailyCalorieIntakes;
    private DailyCalorieAdapter dailyCalorieAdapter;

    private void setCustomResourceForDates() {
        Calendar cal = Calendar.getInstance();
        // Min date is last 7 days
        cal.add(Calendar.DATE, 0);
        Date blueDate = cal.getTime();
        if (caldroidFragment != null) {
            caldroidFragment.setTextColorForDate(R.color.primary, blueDate);
        }
    }

    Date mainDate = new Date();

    private void setMoreData(Date getDate) {
        mainDate = getDate;
        TextView slideTitle = (TextView) findViewById(R.id.slideTitle);
        if (DateCompare.areDatesEqual(new Date(), getDate)) { // Are Dates Equal Today
            slideTitle.setText("Today");
        } else if (DateCompare.areDatesEqualYesterday(new Date(), getDate)) {  // Are Dates Equal Yesterday
            slideTitle.setText("Yesterday");
        } else if (DateCompare.areDatesEqualTomorrow(new Date(), getDate)) {  // Are Dates Equal Yesterday
            slideTitle.setText("Tomorrow");
        } else {
            slideTitle.setText(DateFormat.format("MMM d, EE", getDate));
        }

        String currentDay = (String) android.text.format.DateFormat.format("dd", getDate); //20
        if (Integer.valueOf(currentDay) > 21) {
            mLayout.setParalaxOffset(height / 3);
        } else {
            mLayout.setParalaxOffset(0);
        }
        LogAdapterAll logAdapterAll = new LogAdapterAll(this, 0, LogMeal.logsByDate(getDate));
        double intCalories = 0;
        double intFat = 0;
        double intCarbs = 0;
        double intPro = 0;
        for (LogMeal logMeal : logAdapterAll.getLogs()) {
            intCalories += logMeal.getCalorieCount();
            intFat += logMeal.getFatCount();
            intCarbs += logMeal.getCarbCount();
            intPro += logMeal.getProteinCount();
        }
        TextView cals = (TextView) findViewById(R.id.calories);
        TextView fat = (TextView) findViewById(R.id.fat);
        TextView carbs = (TextView) findViewById(R.id.carbs);
        TextView pro = (TextView) findViewById(R.id.pro);
        cals.setText(df.format(intCalories));
        fat.setText(df.format(intFat));
        carbs.setText(df.format(intCarbs));
        pro.setText(df.format(intPro));
        pg = (PieGraph) findViewById(R.id.graph);
        pg.removeSlices();
        // Fat
        int fatCount = Integer.valueOf(df.format(intFat)) * 9;
        PieSlice slice = new PieSlice();
        slice.setColor(Color.parseColor("#4DB6AC"));
        if (fatCount == 0) {
            slice.setValue(1);
        } else {
            slice.setValue(fatCount);
        }
        pg.addSlice(slice);

        // Carbs
        int carbCount = Integer.valueOf(df.format(intCarbs)) * 4;
        slice = new PieSlice();
        slice.setColor(Color.parseColor("#FFC107"));
        if (carbCount == 0) {
            slice.setValue(1);
        } else {
            slice.setValue(carbCount);
        }
        pg.addSlice(slice);

        // Pro
        int proCount = Integer.valueOf(df.format(intPro)) * 4;
        slice = new PieSlice();
        slice.setColor(Color.parseColor("#9C27B0"));
        if (proCount == 0) {
            slice.setValue(1);
        } else {
            slice.setValue(proCount);
        }
        pg.addSlice(slice);


        double percentage = fatCount + carbCount + proCount;

        double fatPer = (fatCount / percentage) * 100;
        double carbPer = (carbCount / percentage) * 100;
        double proPer = (proCount / percentage) * 100;

        boolean fatNaN = fatPer != fatPer;
        boolean carbNaN = carbPer != carbPer;
        boolean proNaN = proPer != proPer;
        if (fatNaN) {
            fatPer = 0;
        }
        if (carbNaN) {
            carbPer = 0;
        }
        if (proNaN) {
            proPer = 0;
        }
        TextView fatp = (TextView) findViewById(R.id.fatP);
        TextView carbsP = (TextView) findViewById(R.id.carbsP);
        TextView proP = (TextView) findViewById(R.id.proP);
        fatp.setText(dfT.format(fatPer) + "%");
        fatp.setTextColor(Color.parseColor("#4DB6AC"));
        carbsP.setText(dfT.format(carbPer) + "%");
        carbsP.setTextColor(Color.parseColor("#FFC107"));
        proP.setText(dfT.format(proPer) + "%");
        proP.setTextColor(Color.parseColor("#9C27B0"));
    }

    Toolbar toolbar;
    LinearLayout calView;
    List<Date> test;
    DecimalFormat df = new DecimalFormat("0");
    TextView txtTitle;
    private SlidingUpPanelLayout mLayout;
    ImageView moreLess;
    int height;
    TextView setDate;
    PieGraph pg;
    ViewPager pager;
    RelativeLayout slide_helper;
    private DecimalFormat dfT = new DecimalFormat("0.0");

    protected void init() {
        height = calView.getHeight();
    }

    Paint mLineGridPaint;
    LineSet dataSet;
    float[] simpleArray;
    String[] dates;
    int max;
    int min;
    float goalWeightLine;

    private void weightChart() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        TextView weight = (TextView) findViewById(R.id.weight);
        WeightLogAdapter weightLogAdapter = new WeightLogAdapter(this, 0, WeightLog.all());
        WeightLog weightLog = weightLogAdapter.getItem(weightLogAdapter.getCount() - 1);
        weight.setText(df.format(weightLog.getCurrentWeight()) + " lbs");
        WeightLog weightLogStart = weightLogAdapter.getItem(0);
        max = (int) Precision.round(weightLogStart.getCurrentWeight(), -1) + 10;
        if (Double.valueOf(sharedPreferences.getString(Globals.USER_WEIGHT_GOAL, "")) < weightLog.getCurrentWeight()) {
            min = (int) Precision.round(Integer.valueOf(sharedPreferences.getString(Globals.USER_WEIGHT_GOAL, "")), -1) - 10;
        } else {
            min = (int) Precision.round(weightLog.getCurrentWeight(), -1) - 10;
        }
        goalWeightLine = Float.valueOf(sharedPreferences.getString(Globals.USER_WEIGHT_GOAL, ""));


        LineChartView mLineChart = (LineChartView) findViewById(R.id.linechart);
        mLineChart.reset();
        dataSet = new LineSet();
        simpleArray = new float[weightLogAdapter.getCount()];
        dates = new String[weightLogAdapter.getCount()];
        for (int i = 0; i < weightLogAdapter.getCount(); i++) {
            WeightLog weightLogWeight = weightLogAdapter.getItem(i);
            simpleArray[i] = (float) weightLogWeight.getCurrentWeight();
            dates[i] = String.valueOf(DateFormat.format("MMM dd", weightLogWeight.getDate()));
        }
        dataSet.addPoints(dates, simpleArray);
        mLineGridPaint = new Paint();
        mLineGridPaint.setColor(this.getResources().getColor(R.color.accent));
        mLineGridPaint.setStyle(Paint.Style.FILL);
        mLineGridPaint.setAntiAlias(true);
        /**
         * Controlling the data set and setting it to the chart.
         */
        dataSet
                .setDots(true)
                .setDotsColor(this.getResources().getColor(R.color.primary))
                .setDotsRadius(Tools.fromDpToPx(3))
                .setDotsStrokeThickness(Tools.fromDpToPx(1))
                .setDotsStrokeColor(this.getResources().getColor(R.color.primary))
                .setLineColor(this.getResources().getColor(R.color.primary_dark))
                .setLineThickness(Tools.fromDpToPx(1))
                .beginAt(0).endAt(weightLogAdapter.getCount());
        mLineChart.addData(dataSet);

        mLineChart
                .setBorderSpacing(Tools.fromDpToPx(0))
                .setGrid(LineChartView.GridType.HORIZONTAL, mLineGridPaint)
                .setXAxis(false)
                .setXLabels(XController.LabelPosition.OUTSIDE)
                .setYAxis(false)
                .setYLabels(YController.LabelPosition.OUTSIDE)
                .setAxisBorderValues(min, max, 10)
                .setLabelColor(this.getResources().getColor(R.color.text_color))
                .setLabelsFormat(new DecimalFormat("##' lbs'"))
                .show();

        Paint paint = new Paint();
        paint.setStrokeWidth((float) Equations.dpToPx(this, 2));
        paint.setColor(this.getResources().getColor(R.color.green));
        mLineChart.setThresholdLine(goalWeightLine, paint);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        pager = (ViewPager) findViewById(R.id.viewPager);
        CalViewPager calViewPager = new CalViewPager();
        weightChart();
        slide_helper = (RelativeLayout) findViewById(R.id.slide_helper);
        slide_helper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
        pager.setAdapter(calViewPager);
        final ImageView pageOne = (ImageView) findViewById(R.id.pageOne);
        pageOne.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pager.setCurrentItem(0);
                return false;
            }
        });
        final ImageView pageTwo = (ImageView) findViewById(R.id.pageTwo);
        pageTwo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pager.setCurrentItem(1);
                return false;
            }
        });
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    pageOne.setColorFilter(Color.parseColor("#6D6D6D"), android.graphics.PorterDuff.Mode.MULTIPLY);
                    pageTwo.setColorFilter(Color.parseColor("#30000000"), android.graphics.PorterDuff.Mode.MULTIPLY);
                } else {
                    pageOne.setColorFilter(Color.parseColor("#30000000"), android.graphics.PorterDuff.Mode.MULTIPLY);
                    pageTwo.setColorFilter(Color.parseColor("#6D6D6D"), android.graphics.PorterDuff.Mode.MULTIPLY);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        caldroidFragment = new CaldroidSampleCustomFragment();
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.setPanelSlideListener(this);
        moreLess = (ImageView) findViewById(R.id.moreLess);
        setDate = (TextView) findViewById(R.id.setDate);
        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra(MainActivity.ACTIVITY_ONE_RESULT, mainDate);
                setResult(RESULT_OK, data); // passing the RESULT_OK parameter
                finish();
            }
        });
        moreLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.COLLAPSED)) {
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                } else {
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }
            }
        });
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_today) {
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    caldroidFragment.setBackgroundResourceForDate(R.color.white, test.get(test.size() - 1));
                    caldroidFragment.setBackgroundResourceForDate(R.color.light_trans, new Date());
                    caldroidFragment.refreshView();
                    toolbar.getMenu().clear();
                    test.add(new Date());
                    caldroidFragment.moveToDate(new Date());
                    setMoreData(new Date());
                }
                return false;
            }
        });

        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        } else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
            caldroidFragment.setArguments(args);
        }
        test = new ArrayList<>();

        HashMap<Date, Integer> meMap = new HashMap<>();
        db = new DatabaseHandler(this);
        dailyCalorieIntakes = db.getAllContacts(); // filter by string
        dailyCalorieAdapter = new DailyCalorieAdapter(this, 0, dailyCalorieIntakes);
        if (caldroidFragment != null) {
            HashMap<String, Object> extraData = caldroidFragment.getExtraData();
            for (int i = 0; i < dailyCalorieAdapter.getCount(); i++) {
                DailyCalorieIntake dailyCalorieIntake = dailyCalorieAdapter.getItem(i);
                meMap.put(DateCompare.stringTodDate(dailyCalorieIntake.getDate()), 0);
                extraData.put(dailyCalorieIntake.getDate(), df.format(dailyCalorieIntake.getPhoneNumber()));
                caldroidFragment.refreshView();
            }
        }
        caldroidFragment.setBackgroundResourceForDate(R.color.light_trans, new Date());
        caldroidFragment.refreshView();
        test.add(new Date());

        setCustomResourceForDates();
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        calView = (LinearLayout) findViewById(R.id.calendar1);
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();
        ViewTreeObserver observer = calView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                init();
                calView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        setMoreData(new Date());
        // Setup listener

        final CaldroidListener listener = new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                if (!DateCompare.dateToString(new Date()).equals(DateCompare.dateToString(date))) {
                    toolbar.getMenu().clear();
                    toolbar.inflateMenu(R.menu.menu_calendar);
                } else {
                    toolbar.getMenu().clear();
                }
                caldroidFragment.setBackgroundResourceForDate(R.color.white, test.get(test.size() - 1));
                caldroidFragment.setBackgroundResourceForDate(R.color.light_trans, date);
                caldroidFragment.refreshView();
                test.add(date);
                setMoreData(date);
            }

            @Override
            public void onChangeMonth(int month, int year) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                Calendar cal = Calendar.getInstance();
                if (cal.get(Calendar.MONTH) + 1 == month) {
                    toolbar.getMenu().clear();
                } else {
                    toolbar.getMenu().clear();
                    toolbar.inflateMenu(R.menu.menu_calendar);
                }
                if (month == 1) {
                    txtTitle.setText("January, " + year);
                } else if (month == 2) {
                    txtTitle.setText("February, " + year);
                } else if (month == 3) {
                    txtTitle.setText("March, " + year);
                } else if (month == 4) {
                    txtTitle.setText("April, " + year);
                } else if (month == 5) {
                    txtTitle.setText("May, " + year);
                } else if (month == 6) {
                    txtTitle.setText("June, " + year);
                } else if (month == 7) {
                    txtTitle.setText("July, " + year);
                } else if (month == 8) {
                    txtTitle.setText("August, " + year);
                } else if (month == 9) {
                    txtTitle.setText("September, " + year);
                } else if (month == 10) {
                    txtTitle.setText("October, " + year);
                } else if (month == 11) {
                    txtTitle.setText("November, " + year);
                } else if (month == 12) {
                    txtTitle.setText("December, " + year);
                }
            }

            @Override
            public void onLongClickDate(Date date, View view) {
            }

            @Override
            public void onCaldroidViewCreated() {
                TextView title = caldroidFragment.getMonthTitleTextView();
                title.setVisibility(View.GONE);
                caldroidFragment.setShowNavigationArrows(false);
                caldroidFragment.refreshView();
            }

        };
        caldroidFragment.setCaldroidListener(listener);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }
    }

    @Override
    public void onPanelSlide(View view, float v) {
        slide_helper.setVisibility(View.VISIBLE);
        slide_helper.setAlpha(v);
        Matrix matrix = new Matrix();
        moreLess.setScaleType(ImageView.ScaleType.MATRIX);   //required
        double angle = 180 * v;
        matrix.postRotate((float) angle, 56, 56);
        moreLess.setImageMatrix(matrix);
    }

    @Override
    public void onPanelCollapsed(View view) {
        pager.setCurrentItem(0);
        slide_helper.setVisibility(View.GONE);
    }

    @Override
    public void onPanelExpanded(View view) {

    }

    @Override
    public void onPanelAnchored(View view) {

    }

    @Override
    public void onPanelHidden(View view) {

    }
}

