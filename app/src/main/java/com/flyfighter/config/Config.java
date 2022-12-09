package com.flyfighter.config;


import com.flyfighter.BuildConfig;

public class Config {

    public static final String testUnityId = "ca-app-pub-3940256099942544/6300978111";
    public static final String myUnityId = "ca-app-pub-3940256099942544/6300978111";


    public static String getBannerUnitId() {
        return BuildConfig.BUILD_TYPE.equals("debug") ? testUnityId : myUnityId;
    }

    public static final String testRewordUnityId = "ca-app-pub-3940256099942544/5224354917";
    public static final String myRewordUnityId = "ca-app-pub-3940256099942544/5224354917";

    public static String getRewordUnitId() {
        return BuildConfig.BUILD_TYPE.equals("debug") ? testRewordUnityId : myRewordUnityId;
    }

}
