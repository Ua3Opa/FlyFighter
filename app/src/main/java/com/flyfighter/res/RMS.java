package com.flyfighter.res;

import com.alibaba.fastjson.JSON;
import com.flyfighter.room.PlayRecord;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;

public class RMS {
    public static int[][] gSaveData = new int[11][2];
    public static int[] gConfigData = new int[3];
    public static boolean loadSound;
    public static float volume;
    public static byte lastScoreIndex;
    private static List<PlayRecord> playRecords = new ArrayList<>();
    private static int aint;


    public static void readConfigData() {
        init();
        loadSound = MMKV.defaultMMKV().decodeBool("loadSound");
        volume = MMKV.defaultMMKV().decodeFloat("loadSound",0.5f);
    }


    private static void init() {
        try {
            String recordStr = MMKV.defaultMMKV().decodeString("Records", "");
            List<PlayRecord> records = JSON.parseArray(recordStr, PlayRecord.class);
            if (records == null) {
                return;
            }
            playRecords.addAll(records);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
