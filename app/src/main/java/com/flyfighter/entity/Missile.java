package com.flyfighter.entity;

import android.graphics.Bitmap;
import android.util.Size;

import com.flyfighter.res.ResInit;

public class Missile {
    private static final byte[] bulletSpeedMissile = new byte[]{0, -7, -3, -7, -5, -5, -7, -3, -7, 0, -7, 3, -5, 5, -3, 7, 0, 7, 3, 7, 5, 5, 7, 3, 7, 0, 7, -3, 5, -5, 3, -7};
    public static int[][] missileOffset = new int[][]{{-42, -5}, {42, -5}, {-40, -5}, {40, -5}};

    public int type;
    public int x;
    public int y;

    public int speedX;
    public int speedY;

    public int power;//伤害

    public int picNum;

    public int imgIndex;

    public Bitmap sourceImg;

    //无敌时间为5秒
    public Missile() {
    }

    public static Missile[] makeMissiles(int type, int x, int y) {
        Missile[] missiles = new Missile[2];
        missiles[0] = makeMissile(type, x, y, missileOffset[(type - 1) * 2]);
        missiles[1] = makeMissile(type, x, y, missileOffset[(type - 1) * 2 + 1]);
        return missiles;
    }

    public static Missile makeMissile(int type, int x, int y, int[] offset) {
        Missile missile = new Missile();
        missile.type = type;
        if (type == 1) {//双导弹
            missile.speedX = 0;
            missile.speedY = 15;
            missile.sourceImg = ResInit.bulletImage[57];
            missile.picNum = 2;
            missile.power = 18;
        } else {//追踪弹
            missile.sourceImg = ResInit.bulletImage[58];
            missile.picNum = 16;
            missile.power = 10;
        }
        Size imgSize = missile.getImgSize();
        missile.x = x + offset[0] - imgSize.getWidth() / 2;
        missile.y = y - imgSize.getHeight() / 2;
        return missile;
    }

    public Bitmap getImg() {
        //int index = (int) (System.currentTimeMillis() / 200) % 4;
        Bitmap bitmap = null;
        switch (type) {
            case 1:
                int index = imgIndex / 8;
                bitmap = Bitmap.createBitmap(sourceImg, sourceImg.getWidth() / picNum * (index % picNum), 0, sourceImg.getWidth() / picNum, sourceImg.getHeight());
                break;
            case 2:
                break;
            default:
                break;
        }
        imgIndex++;
        return bitmap;
    }


    public Bitmap getImgBitmap() {
        //int index = (int) (System.currentTimeMillis() / 200) % 4;
        Bitmap bitmap = null;
        switch (type) {
            case 1:
                bitmap = Bitmap.createBitmap(sourceImg, 0, 0, sourceImg.getWidth() / picNum, sourceImg.getHeight());
                break;
            case 2:
                break;
            default:
                break;
        }
        imgIndex++;
        return bitmap;
    }

    public Size getImgSize() {
        Size size = null;
        switch (type) {
            case 1:
                Bitmap bitmap = Bitmap.createBitmap(sourceImg, 0, 0, sourceImg.getWidth() / picNum, sourceImg.getHeight());
                size = new Size(bitmap.getWidth(), bitmap.getHeight());
                break;
            case 2:
                break;
            default:
                break;
        }
        return size;
    }

    public void dealMoveState() {
        if (type == 1) {
            y += speedY;
            speedY--;
        } else {

        }
    }
}
