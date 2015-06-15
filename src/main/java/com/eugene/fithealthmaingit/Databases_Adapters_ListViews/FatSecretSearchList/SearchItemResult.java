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

package com.eugene.fithealthmaingit.Databases_Adapters_ListViews.FatSecretSearchList;


public class SearchItemResult {
    private String FOOD_NAME;
    private String FOOD_DESCRIPTION;
    private String FOOD_BRAND;
    private String FOOD_ID;

    public SearchItemResult(String food_name, String food_description, String food_brand, String food_id) {
        super();
        this.FOOD_NAME = food_name;
        this.FOOD_DESCRIPTION = food_description;
        this.FOOD_BRAND = food_brand;
        this.FOOD_ID = food_id;
    }

    public String getID() {
        return FOOD_ID;
    }

    public String getBrand() {
        return FOOD_BRAND;
    }

    public String getTitle() {
        return FOOD_NAME;
    }

    public String getFoodDescription() {
        return FOOD_DESCRIPTION;
    }
}


