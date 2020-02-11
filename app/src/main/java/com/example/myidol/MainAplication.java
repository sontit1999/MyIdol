package com.example.myidol;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.gw.swipeback.tools.WxSwipeBackActivityManager;

public class MainAplication extends Application {
    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("sontit","oncreate main application");
        WxSwipeBackActivityManager.getInstance().init(this);
    }
}
