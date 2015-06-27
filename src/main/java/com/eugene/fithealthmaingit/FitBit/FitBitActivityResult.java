package com.eugene.fithealthmaingit.FitBit;

public class FitBitActivityResult {
    private String NAME;
    private String CALORIES;

    public FitBitActivityResult(String activity_name, String activity_calories) {
        super();
        this.NAME = activity_name;
        this.CALORIES = activity_calories;
    }


    public String getName() {
        return NAME;
    }

    public String getCalories() {
        return CALORIES;
    }
}
