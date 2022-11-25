package com.flyfighter.entity;

import android.graphics.Bitmap;

import com.flyfighter.menu.GameCanvas;
import com.flyfighter.res.ResInit;

public class PlayerBullet extends Spirit {

    int index;

    public PlayerBullet() {
    }

    /**
     * 随机type
     *
     * @param type
     * @return
     */
    public static PlayerBullet mallocBullet(int type, int x, int y, int xspd, int yspd, int index) {
        PlayerBullet bullet = new PlayerBullet();
        bullet.type = type;
        bullet.index = index;
        bullet.initSpiritBitmap();
        bullet.initSpiritSize();
        bullet.speedX = xspd;
        bullet.speedY = yspd;
        bullet.x = x - bullet.width / 2;
        bullet.y = y;
        return bullet;
    }

    @Override
    public Bitmap getFrame() {
        return source.get(0);
    }

    @Override
    protected void initSpiritBitmap() {
        int picIndex = 0;
        for (int j = 0; j < type - 1; ++j) {//计算子弹图片的索引
            picIndex += GameCanvas.bulletPic[j];
        }
        source.add(ResInit.bulletImage[picIndex + index]);
    }

    @Override
    public void dealMoveState() {
        x += speedX;
        y += speedY;
    }
}
