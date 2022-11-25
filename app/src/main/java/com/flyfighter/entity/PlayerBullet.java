package com.flyfighter.entity;

import android.graphics.Bitmap;

import com.flyfighter.menu.GameCanvas;
import com.flyfighter.res.ResInit;

public class PlayerBullet extends Spirit {

    public PlayerBullet() {
    }

    /**
     * 随机type
     *
     * @param type
     * @return
     */
    public static PlayerBullet mallocBullet(int type, int x, int y, int xspd, int yspd, int imgNum) {
        PlayerBullet bullet = new PlayerBullet();
        bullet.type = type;
        bullet.picNum = imgNum;
        bullet.initSpiritBitmap();
        bullet.initSpiritSize();
        bullet.speedX = xspd;
        bullet.speedY = yspd;
        bullet.x = x - bullet.width;
        bullet.y = y;
        return bullet;
    }

    @Override
    public Bitmap getFrame() {
        super.getFrame();
        return source.get(frameIndex);
    }

    @Override
    protected void initSpiritBitmap() {
        int picIndex = 0;
        for (int j = 0; j < type; ++j) {//计算子弹图片的索引
            picIndex += GameCanvas.bulletPic[j];
        }
        Bitmap bitmap = ResInit.bulletImage[picIndex - 1];
        source.addAll(splitBitmap(bitmap, picNum));
    }

    @Override
    public void dealMoveState() {
        x += speedX;
        y += speedY;
    }
}
