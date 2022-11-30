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
    public static int difficulty;
    public static byte lastScoreIndex;
    public static List<PlayRecord> playRecords = new ArrayList<>();
    private static int aint;


    public static void readConfigData() {
        init();
        loadSound = MMKV.defaultMMKV().decodeBool("loadSound", true);
        volume = MMKV.defaultMMKV().decodeFloat("loadSound", 0.3f);
        difficulty = MMKV.defaultMMKV().decodeInt("difficulty", 1);
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
