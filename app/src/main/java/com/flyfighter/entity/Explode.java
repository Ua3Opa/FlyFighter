package com.flyfighter.entity;

import android.graphics.Bitmap;

import com.flyfighter.res.ResInit;

import java.util.Random;

public class Explode {

    public static final byte[] explodePic = new byte[]{4, 4, 6, 5, 6, 6, 6, 6};

    public int type;
    public int x;
    public int y;

    public Bitmap sourceImg;
    public int picIndex;
    public int picNum;

    public Explode() {
    }

    public Explode(int type, int x, int y, Bitmap sourceImg) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.sourceImg = sourceImg;
    }

    public static Explode dealExplodeState(PlayerBullet bullet,int type) {
        Explode explode = new Explode();
        explode.x = bullet.x;
        explode.y = bullet.y;
        if (explode.type < 2) {
            explode.picIndex = 1;
        } else {
            explode.picIndex = 0;
        }
        explode.picNum = explodePic[explode.type];
        return explode;
    }

    public Bitmap getImg() {
        picIndex++;
        if (type < 2) {
            return ResInit.explodeImage[picIndex % picNum];
        } else {
            Bitmap source = ResInit.explodeImage[type + 8 - 2];
            Bitmap bitmap = Bitmap.createBitmap(source, source.getWidth() / picNum * (picIndex % picNum), 0, source.getWidth() / picNum, source.getHeight());
            return bitmap;
        }
    }
}
