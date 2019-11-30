package com.example.myidol;

import android.app.Application;
import android.util.Log;

import com.gw.swipeback.tools.WxSwipeBackActivityManager;

public class MainAplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("sontit","oncreate main application");
        WxSwipeBackActivityManager.getInstance().init(this);
    }
}
