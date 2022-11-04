package com.flyfighter;

import android.app.Application;

import com.tencent.mmkv.MMKV;

public class FlyFighterApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MMKV.initialize(this);
    }
}
