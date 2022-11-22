package com.flyfighter.entity;

import android.graphics.Bitmap;

import java.util.Random;

public class ExploredImg {

    public int type;
    public int x;
    public int y;

    public Bitmap sourceImg;


    public ExploredImg(int type, int x, int y, Bitmap sourceImg) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.sourceImg = sourceImg;
    }

    public Bitmap getImg() {
        return sourceImg;
    }
}
