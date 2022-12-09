package com.flyfighter.holder;

import androidx.lifecycle.MutableLiveData;

import com.flyfighter.enums.RunState;

public class MainDataHolder {

    public static int continueNum;
    public static boolean getReward;
    public static MutableLiveData<Boolean> mainAdState = new MutableLiveData<>(true);
    public static MutableLiveData<Boolean> mainRewordAdState = new MutableLiveData<>(false);

    public static volatile MutableLiveData<RunState> runState = new MutableLiveData<>(RunState.Running);
}
