package com.flyfighter.entity;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public abstract class Spirit {
    public int animDuration = 200;

    public int type;
    public int x;
    public int y;
    public int speedX;
    public int speedY;

    public int width;
    public int height;

    public int picNum;

    public int direction;
    public long createTime = System.currentTimeMillis();
    public long lastFrameTime;
    public int frameIndex;

    public List<Bitmap> source = new ArrayList<>();

    protected abstract void initSpiritBitmap();
    public abstract void dealMoveState();


    public Bitmap getFrame() {
        if (System.currentTimeMillis() - lastFrameTime >= animDuration) {
            frameIndex++;
        }
        return null;
    }

    protected void initSpiritSize(){
        width = source.get(0).getWidth();
        height = source.get(0).getHeight();
    }

    public static List<Bitmap> splitBitmap(Bitmap source, int picNum) {
        List<Bitmap> bitmaps = new ArrayList<>();
        for (int i = 0; i < picNum; i++) {
            Bitmap bitmap = Bitmap.createBitmap(source, source.getWidth() / picNum * i, 0, source.getWidth() / picNum, source.getHeight());
            bitmaps.add(bitmap);
        }
        return bitmaps;
    }

    public static List<Bitmap> createBitmap(Bitmap source, int x, int y, int width, int height) {
        List<Bitmap> bitmaps = new ArrayList<>();
        Bitmap bitmap = Bitmap.createBitmap(source, x, y, width, height);
        bitmaps.add(bitmap);
        return bitmaps;
    }

    public Bitmap firstFrame() {
        return source.get(0);
    }
}
