package com.flyfighter.entity;

import android.graphics.Bitmap;

import com.flyfighter.res.ResInit;

import java.util.List;

public class Explode extends Spirit {

    public static final byte[] explodePic = new byte[]{4, 4, 6, 5, 6, 6, 6, 6};

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
        explode.initSpiritBitmap();
        return explode;
    }

    public Bitmap getFrame() {
        super.getFrame();
        return source.get(frameIndex % picNum);
    }

    @Override
    protected void initSpiritBitmap() {
        if (type < 2) {
            for (int i = 0; i < picNum; i++) {
                source.add(ResInit.explodeImage[type * picNum + i]);
            }
        } else {
            List<Bitmap> splitBitmap = splitBitmap(ResInit.explodeImage[type + 8 - 2], picNum);
            source.addAll(splitBitmap);
        }
    }

    @Override
    public void dealMoveState() {

    }
}
