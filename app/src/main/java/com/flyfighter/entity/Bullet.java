package com.flyfighter.entity;

import android.graphics.Bitmap;

import com.flyfighter.menu.GameCanvas;
import com.flyfighter.res.ResInit;

import java.util.Random;

public class Bullet extends Spirit {

    public static final int[] bulletSpeedToPlayer = new int[]{0, 8, 4, 8, 4, 4, 8, 4, 8, 4, 8, 4, 8, 0, 8, -4, 8, -4, 8, -8, 4, -8, 4, -8, 0, -12, -4, -12, -4, -4, -8, -4, -8, -4, -8, -4, -8, 0, -12, 4, -8, 4, -4, 4, -4, 4, -4, 12};

    Random random = new Random();

    public Bullet() {
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
        bullet.x = x - bullet.width / 2;
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
        recordMovePosition();
        x += speedX;
        y += speedY;
    }
}
