package com.flyfighter.entity;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

import com.flyfighter.res.ResInit;

public class Missile extends Spirit {

    private static final int[] bulletSpeedMissile = new int[]{0, -14, -6, -14, -10, -10, -14, -6, -14, 0, -14, 6, -10, 10, -6, 14, 0, 14, 6, 14, 10, 10, 14, 6, 14, 0, 14, -6, 10, -10, 6, -14};
    public static int[][] missileOffset = new int[][]{{-42, -5}, {42, -5}, {-40, -5}, {40, -5}};

    public int power;//伤害
    public int missileDirection = 0;//导弹方向,对于追踪弹
    public int from;//0: player 1 :boss

    //无敌时间为5秒
    public Missile() {
    }

    public static Missile[] makeMissiles(int type, int x, int y) {
        Missile[] missiles = new Missile[2];
        missiles[0] = makeMissile(type, x, y, missileOffset[(type - 1) * 2], 0);
        missiles[1] = makeMissile(type, x, y, missileOffset[(type - 1) * 2 + 1], 0);
        return missiles;
    }

    public static Missile[] makeMissiles(int type, int x, int y, int[][] missileOffset, int from) {
        Missile[] missiles = new Missile[2];
        missiles[0] = makeMissile(type, x, y, missileOffset[0], from);
        missiles[1] = makeMissile(type, x, y, missileOffset[1], from);
        return missiles;
    }

    public static Missile makeMissile(int type, int x, int y, int[] offset, int from) {
        Missile missile = new Missile();
        missile.type = type;
        missile.from = from;
        if (type == 1) {//双导弹
            missile.speedX = 0;
            missile.speedY = from == 1 ? 8 : 15;
            missile.picNum = 2;
            missile.power = 18;
        } else {//追踪弹
            missile.picNum = 16;
            missile.power = 10;
            missile.missileDirection = 0;//默认向上
            missile.speedX = bulletSpeedMissile[missile.missileDirection * 2];
            missile.speedY = bulletSpeedMissile[missile.missileDirection * 2 + 1];
        }
        missile.initSpiritBitmap();
        missile.initSpiritSize();
        missile.x = x + offset[0] - missile.width / 2;
        missile.y = y - missile.height / 2;
        return missile;
    }

    public Bitmap getFrame() {
        super.getFrame();
        //int index = (int) (System.currentTimeMillis() / 200) % 4;
        Bitmap bitmap = null;
        switch (type) {
            case 1:
                bitmap = source.get(frameIndex % 2);
                break;
            case 2:
                bitmap = source.get(missileDirection);
                break;
            default:
                break;
        }
        return bitmap;
    }

    /**
     * 最近的一个敌人的位置
     *
     * @param enemy
     */

    public void dealMoveState(EnemyPlane enemy) {
        recordMovePosition();
        if (from == 1) {//速度翻转
            y += speedY;
            speedY++;
        } else {
            if (type == 1) {
                y += speedY;
                speedY--;
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

        int eX = enemy.getCenterX();
        int eY = enemy.getCenterY();

        double distance1 = Math.pow(nextX - eX, 2) + Math.pow(nextY - eY, 2);

        if (missileDirection + 1 >= 16) {
            temp = 0;
        } else {
            temp = missileDirection + 1;
        }

        int next2X = x + bulletSpeedMissile[temp * 2];
        int next2Y = y + bulletSpeedMissile[temp * 2 + 1];

        double distance2 = Math.pow(next2X - eX, 2) + Math.pow(next2Y - eY, 2);

        if (missileDirection - 1 < 0) {
            temp = 15;
        } else {
            temp = missileDirection - 1;
        }

        int next3X = x + bulletSpeedMissile[temp * 2];
        int next3Y = y + bulletSpeedMissile[temp * 2 + 1];

        double distance3 = Math.pow(next3X - eX, 2) + Math.pow(next3Y - eY, 2);
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

    @Override
    protected void initSpiritBitmap() {
        if (from == 1) {
            Bitmap bm = ResInit.bulletImage[57];
            Matrix matrix = new Matrix();
            matrix.setRotate(180);

            Bitmap newBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, false);
            source.addAll(splitBitmap(newBm, picNum));
        } else {
            if (type == 1) {//双导弹
                source.addAll(splitBitmap(ResInit.bulletImage[57], picNum));
            } else {//追踪弹
                source.addAll(splitBitmap(ResInit.bulletImage[58], picNum));
            }
        }
    }

}
