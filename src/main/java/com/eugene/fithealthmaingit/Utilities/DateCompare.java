package com.eugene.fithealthmaingit.Utilities;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class DateCompare {

    public static int lastX = 0;

    /*
    Returns Date - 1
     */
    public static Date previousDate(Date d) {
        Date previous = d;
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        Calendar mCalendar = new GregorianCalendar(year, month, day);
        mCalendar.add(Calendar.DATE, lastX-- - 1);
        previous = mCalendar.getTime();
        return previous;
    }

    /*
    Returns Date + 1
    */
    public static Date nextDate(Date d) {
        Date next = d;
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        Calendar mCalendar = new GregorianCalendar(year, month, day);
        mCalendar.add(Calendar.DATE, lastX++ + 1);
        next = mCalendar.getTime();
        return next;
    }

    /*
    Checks to see if two dates are equal
    Returns True if dates are equal
     */
    public static boolean areDatesEqual(Date a, Date b) {
        Calendar cal1 = Calendar.getInstance();  // put in current date; Date mDate = new Date();
        Calendar cal2 = Calendar.getInstance();  // put in the date to compare
        cal1.setTime(a);
        cal2.setTime(b);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
            && cal1.get(Calendar.DAY_OF_YEAR) == cal2
            .get(Calendar.DAY_OF_YEAR);
    }

    /*
    Checks Checks to see if a given date
    Returns True if dates are equal
    */
    public static boolean areDatesEqualYesterday(Date a, Date b) {
        Calendar cal1 = Calendar.getInstance(); // put in current date; Date mDate = new Date();
        Calendar cal2 = Calendar.getInstance(); // put in the date to compare
        cal1.setTime(a);
        cal2.setTime(b);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
            && cal1.get(Calendar.DAY_OF_YEAR) - 1 == cal2
            .get(Calendar.DAY_OF_YEAR);
    }

    /*
    Checks Checks to see if a given date
    Returns True if dates are equal
    */
    public static boolean areDatesEqualTomorrow(Date a, Date b) {
        Calendar cal1 = Calendar.getInstance(); // put in current date; Date mDate = new Date();
        Calendar cal2 = Calendar.getInstance(); // put in the date to compare
        cal1.setTime(a);
        cal2.setTime(b);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
            && cal1.get(Calendar.DAY_OF_YEAR) + 1 == cal2
            .get(Calendar.DAY_OF_YEAR);
    }
}
