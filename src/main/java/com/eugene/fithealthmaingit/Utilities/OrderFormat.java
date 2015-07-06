package com.eugene.fithealthmaingit.Utilities;

public class OrderFormat {
    public static String setMealFormat(String meanType) {
        String meal = "";
        switch (meanType) {
            case "Snack":
                meal = "a";
                break;
            case "Breakfast":
                meal = "b";
                break;
            case "Lunch":
                meal = "c";
                break;
            case "Dinner":
                meal = "d";
                break;
            default:
                break;
        }
        return meal;
    }
}
