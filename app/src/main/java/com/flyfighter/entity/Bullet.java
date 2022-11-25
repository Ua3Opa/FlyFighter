package com.flyfighter.entity;

import android.graphics.Bitmap;

import com.flyfighter.menu.GameCanvas;
import com.flyfighter.res.ResInit;

import java.util.Random;

public class Bullet extends Spirit {

    public static final int[] bulletSpeedToPlayer = new int[]{0, 9, 4, 9, 4, 4, 9, 4, 9, 4, 9, 4, 9, 0, 9, -4, 9, -4, 9, -9, 4, -9, 4, -9, 0, -13, -4, -13, -4, -4, -9, -4, -9, -4, -9, -4, -9, 0, -13, 4, -9, 4, -4, 4, -4, 4, -4, 13};

    Random random = new Random();

    public Bullet() {
    }


    public Bullet(int type, int x, int y, int speedX, int speedY) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.speedX = speedX;
        this.speedY = speedY;
    }

    /**
     * 随机type
     *
     * @param type
     * @return
     */
    public static Bullet mallocBullet(int type, int x, int y, int xspd, int yspd, int picNum, Bitmap sourceImg) {
        Bullet bullet = new Bullet();
        bullet.type = type;
        bullet.picNum = picNum;
        bullet.initSpiritBitmap();
        bullet.initSpiritSize();
        bullet.speedX = xspd;
        bullet.speedY = yspd;
        bullet.x = x - bullet.width;
        bullet.y = y;
        return bullet;
    }

    public Bitmap getFrame() {
        super.getFrame();
        return source.get(frameIndex % picNum);
    }

    @Override
    protected void initSpiritBitmap() {
        int picIndex = 0;
        for (int j = 0; j < type; ++j) {//计算子弹图片的索引
            picIndex += GameCanvas.bulletPic[j];
        }
        for (int i = 0; i < GameCanvas.bulletPic[type]; i++) {
            Bitmap bitmap = ResInit.bulletImage[picIndex + i];
            source.add(bitmap);
        }
    }

    @Override
    public void dealMoveState() {

    }
}
