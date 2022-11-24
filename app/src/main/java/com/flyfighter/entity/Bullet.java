package com.flyfighter.entity;

import android.graphics.Bitmap;

import com.flyfighter.menu.GameCanvas;
import com.flyfighter.res.ResInit;

import java.util.Random;

public class Bullet {

    public static final int[] bulletSpeedToPlayer = new int[]{0, 9, 4, 9, 4, 4, 9, 4, 9, 4, 9, 4, 9, 0, 9, -4, 9, -4, 9, -9, 4, -9, 4, -9, 0, -13, -4, -13, -4, -4, -9, -4, -9, -4, -9, -4, -9, 0, -13, 4, -9, 4, -4, 4, -4, 4, -4, 13};
    public int type;
    public int colors;
    public int x;
    public int y;
    public int speedX;
    public int speedY;
    public int imgIndex;
    public int imgNum;
    public int width;
    public int height;
    public Bitmap sourceImg;


    Random random = new Random();

    public Bullet() {
        this.type = 0;
        this.colors = 0;
        this.x = 0;
        this.y = 0;
        this.speedX = 0;
        this.speedY = 0;
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
    public static Bullet mallocBullet(int type, int x, int y, int xspd, int yspd, int imgNum, Bitmap sourceImg) {
        Bullet bullet = new Bullet();
        bullet.type = type;
        bullet.speedX = xspd;
        bullet.speedY = yspd;
        bullet.imgNum = imgNum;
        bullet.sourceImg = sourceImg;

        bullet.x = x - sourceImg.getWidth() / imgNum;
        bullet.y = y;

        return bullet;
    }

    private int getRand(int i) {
        if (i == 0) {
            return 0;
        }
        int r = this.random.nextInt();
        r = (r >> 24) + (r >> 16) + (r >> 8) + r & 0xFF;
        return Math.abs(r % i);
    }


    public Bitmap getImg() {
//        Bitmap bitmap = Bitmap.createBitmap(sourceImg, sourceImg.getWidth() / imgNum * (imgIndex % imgNum), 0, sourceImg.getWidth() / imgNum, sourceImg.getHeight());
//        imgIndex++;

        int picIndex = 0;
        for (int j = 0; j < type; ++j) {//计算子弹图片的索引
            picIndex += GameCanvas.bulletPic[j];
        }

        imgIndex++;
        int index = imgIndex / 8;
        //Log.d("TAG", "getImg: "+this.hashCode()+"   " +(picIndex - 1 + index % imgNum));
        Bitmap bitmap = ResInit.bulletImage[picIndex + index % imgNum];
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        return bitmap;
    }
}
