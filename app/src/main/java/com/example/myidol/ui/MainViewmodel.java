package com.example.myidol.ui;

import android.app.Application;

import androidx.annotation.NonNull;

import com.example.myidol.base.BaseViewmodel;

public class MainViewmodel extends BaseViewmodel {
    String test = "Sơn DZ";
    public MainViewmodel(@NonNull Application application) {
        super(application);
    }
    public  String getTest(){
        return test;
    }
}
