package com.flyfighter.entity;

import android.graphics.Bitmap;
import android.util.Size;

import com.flyfighter.res.ResInit;

public class Missile {

    private static final byte[] bulletSpeedMissile = new byte[]{0, -10, -4, -10, -7, -7, -10, -4, -10, 0, -10, 4, -7, 7, -4, 10, 0, 10, 4, 10, 7, 7, 10, 4, 10, 0, 10, -4, 7, -7, 4, -10};
    public static int[][] missileOffset = new int[][]{{-42, -5}, {42, -5}, {-40, -5}, {40, -5}};

    public int type;
    public int x;
    public int y;

    public int speedX;
    public int speedY;

    public int power;//伤害
    public int missileDirection = 0;//导弹方向,对于追踪弹

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
            missile.missileDirection = 0;//默认向上
            missile.speedX = bulletSpeedMissile[missile.missileDirection * 2];
            missile.speedY = bulletSpeedMissile[missile.missileDirection * 2 + 1];
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
                bitmap = Bitmap.createBitmap(sourceImg, sourceImg.getWidth() / picNum * missileDirection, 0, sourceImg.getWidth() / picNum, sourceImg.getHeight());
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
            case 2:
                bitmap = Bitmap.createBitmap(sourceImg, 0, 0, sourceImg.getWidth() / picNum, sourceImg.getHeight());
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
            case 2:
                Bitmap bitmap = Bitmap.createBitmap(sourceImg, 0, 0, sourceImg.getWidth() / picNum, sourceImg.getHeight());
                size = new Size(bitmap.getWidth(), bitmap.getHeight());
                break;
            default:
                break;
        }
        return size;
    }

    /**
     * 随机一个敌人的位置
     *
     * @param enemy
     */

    public void dealMoveState(EnemyPlane enemy) {
        if (type == 1) {
            y += speedY;
            speedY++;
        } else {//追踪弹
            x += speedX;
            y += speedY;

            if (enemy == null) {
                return;
            }
            missileDirection = getMissileDirection(enemy);
            speedX = bulletSpeedMissile[missileDirection * 2];
            speedY = bulletSpeedMissile[missileDirection * 2 + 1];
        }
    }

    private int getMissileDirection(EnemyPlane enemy) {
        missileDirection = 0;

        int temp;
        for (temp = 0; temp < 16; temp++) {
            if (speedX == bulletSpeedMissile[temp * 2] && speedY == bulletSpeedMissile[temp * 2 + 1]) {
                missileDirection = temp;
                break;
            }
        }
        missileDirection = temp >= 16 ? 0 : missileDirection;

        //原始位置
        int nextX = x + bulletSpeedMissile[missileDirection * 2];
        int nextY = y + bulletSpeedMissile[missileDirection * 2 + 1];

        int distance1 = (int) Math.sqrt(Math.pow(nextX - enemy.x, 2) + Math.pow(nextY - enemy.y, 2));
        if (missileDirection + 1 >= 16) {
            temp = 0;
        } else {
            temp = missileDirection + 1;
        }

        int next2X = x + bulletSpeedMissile[temp * 2];
        int next2Y = y + bulletSpeedMissile[temp * 2 + 1];

        int distance2 = (int) Math.sqrt(Math.pow(next2X - enemy.x, 2) + Math.pow(next2Y - enemy.y, 2));

        if (missileDirection - 1 < 0) {
            temp = 15;
        } else {
            temp = missileDirection - 1;
        }

        int next3X = x + bulletSpeedMissile[temp * 2];
        int next3Y = y + bulletSpeedMissile[temp * 2 + 1];

        int distance3 = (int) Math.sqrt(Math.pow(next3X - enemy.x, 2) + Math.pow(next3Y - enemy.y, 2));
        temp = missileDirection;


        if (distance1 > distance2) {
            distance1 = distance2;
            temp = missileDirection + 1;
            if (temp > 15) {
                temp = 0;
            }
        }

        if (distance1 > distance3) {
            temp = missileDirection - 1;
            if (temp < 0) {
                temp = 15;
            }
        }
        return temp;
    }
}
