package com.eugene.fithealthmaingit.Utilities;

import android.content.Context;


public class Equations {

    public static int dpToPx(Context context, float dp) {
        // Took from http://stackoverflow.com/questions/8309354/formula-px-to-dp-dp-to-px-android
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((dp * scale) + 0.5f);
    }

    /**
     * Calculates persons Body Mass Index
     * English Units: BMI = Weight (lb) / (Height (in) x Height (in)) x 703
     */
    public static double personBMI(double personWeight, double personFeet, double personInches) {
        double feetToInches = personFeet * 12;
        return ((personWeight) / ((feetToInches + personInches) * (feetToInches + personInches))) * 703;
    }

    /**
     * Calculates persons BMR
     * Men: BMR=66.47+ (13.75 x W) + (5.0 x H) - (6.75 x A)
     * Women: BMR=665.09 + (9.56 x W) + (1.84 x H) - (4.67 x A)
     */
    private static double personBMR;

    public static double personBMR(String sex, double personWeight, double personFeet, double personInches, double personAge) {
        double feetToInches = personFeet * 12;
        if (sex.equals("male")) {
            personBMR = (66 + (6.25 * personWeight)
                + (12.7 * (feetToInches + personInches))
                - (6.8 * personAge));
        }
        if (sex.equals("female")) {
            personBMR = (655 + (4.35 * personWeight)
                + (4.7 * (feetToInches + personInches))
                - (4.7 * personAge));
        }
        return personBMR;
    }

    private static double activityLevel;

    public static double personActivityLevel(int position) {
        switch (position) {
            case 0:
                activityLevel = 1.2;
                break;
            case 1:
                activityLevel = 1.375;
                break;
            case 2:
                activityLevel = 1.55;
                break;
            case 3:
                activityLevel = 1.725;
                break;
            case 4:
                activityLevel = 1.9;
                break;
        }
        return activityLevel;
    }


    public static double weightLossPerWeek(int position, double weightGoalFinal) {
        double weightLossPerWeek = 0;
        switch (position) {
            case 0:
                weightLossPerWeek = -1000;
                break;
            case 1:
                weightLossPerWeek = -750;
                break;
            case 2:
                weightLossPerWeek = -500;
                break;
            case 3:
                weightLossPerWeek = -250;
                break;
            case 4:
                weightLossPerWeek = 0;
                break;
            case 5:
                weightLossPerWeek = 250;
                break;
            case 6:
                weightLossPerWeek = 500;
                break;
            case 7:
                weightLossPerWeek = 750;
                break;
            case 8:
                weightLossPerWeek = 1000;
                break;
        }
        return weightGoalFinal - weightLossPerWeek;
    }

    public static double caloriesToGiveUpk(int position) {
        double weightLossPerWeek = 0;
        switch (position) {
            case 0:
                weightLossPerWeek = -1000;
                break;
            case 1:
                weightLossPerWeek = -750;
                break;
            case 2:
                weightLossPerWeek = -500;
                break;
            case 3:
                weightLossPerWeek = -250;
                break;
            case 4:
                weightLossPerWeek = 0;
                break;
            case 5:
                weightLossPerWeek = 250;
                break;
            case 6:
                weightLossPerWeek = 500;
                break;
            case 7:
                weightLossPerWeek = 750;
                break;
            case 8:
                weightLossPerWeek = 1000;
                break;
        }
        return weightLossPerWeek;
    }
}
