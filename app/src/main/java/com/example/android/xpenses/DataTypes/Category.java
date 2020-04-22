package com.example.android.xpenses.DataTypes;

import java.util.ArrayList;

public class Category {

    private String name;
    private ArrayList<SubCategory> subCategoryList = new ArrayList<>();

    public Category(String name, ArrayList<SubCategory> subCategoryList){
        this.name = name;
        this.subCategoryList = subCategoryList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSubCategoryList(ArrayList<SubCategory> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }

    public String getName() {
        return name;
    }

    public ArrayList<SubCategory> getSubCategoryList() {
        return subCategoryList;
    }
}
