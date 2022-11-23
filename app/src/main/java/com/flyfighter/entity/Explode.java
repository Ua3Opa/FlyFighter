package com.flyfighter.entity;

import android.graphics.Bitmap;

import com.flyfighter.res.ResInit;

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

    public static Explode dealExplodeState(PlayerBullet bullet, int type) {
        Explode explode = dealExplodeState(bullet.x, bullet.y, type);
        return explode;
    }

    public static Explode dealExplodeState(EnemyPlane enemy, int type) {
        Explode explode = dealExplodeState(enemy.x, enemy.y, type);
        return explode;
    }

    public static Explode dealExplodeState(int x, int y, int type) {
        Explode explode = new Explode();
        explode.x = x;
        explode.y = y;
        explode.type = type;
        explode.picNum = explodePic[explode.type];
        return explode;
    }

    public Bitmap getImg() {
        Bitmap bitmap;
        if (type < 2) {
            bitmap = ResInit.explodeImage[picIndex % picNum];
        } else {
            Bitmap source = ResInit.explodeImage[type + 8 - 2];
            bitmap = Bitmap.createBitmap(source, source.getWidth() / picNum * (picIndex % picNum), 0, source.getWidth() / picNum, source.getHeight());
        }
        picIndex++;
        return bitmap;
    }
}
