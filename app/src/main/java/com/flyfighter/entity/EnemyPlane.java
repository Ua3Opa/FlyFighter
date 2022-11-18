package com.flyfighter.entity;

import android.graphics.Bitmap;

import com.flyfighter.res.ResInit;

import java.util.Random;

public class EnemyPlane {

    public static final int[][] enemyData = new int[][]{{1, 4, 1, 0, 0, 20, 0, 48, 0, 0, 0, 0, 156, 38, 2, 4, 3}, {2, 2, 0, 0, -12, 0, 400, 60, 0, 0, 0, 0, 168, 41, 2, 4, 3}, {3, 2, 0, 0, 12, 0, 400, 48, 0, 0, 0, 0, 158, 40, 2, 4, 3}, {4, 4, 1, 0, 0, 20, 0, 80, 0, 0, 0, 0, 166, 38, 2, 5, 3}, {5, 2, 0, 0, -8, 0, 400, 100, 0, 0, 0, 0, 168, 35, 2, 5, 3}, {6, 2, 0, 0, 8, 0, 400, 80, 0, 0, 0, 0, 156, 35, 2, 5, 3}, {7, 3, 1, 0, 0, 20, 0, 112, 1, 1, 0, 0, 146, 41, 2, 6, 4}, {8, 1, 0, 0, -12, 0, 400, 88, 0, 0, 0, 0, 138, 36, 2, 6, 4}, {9, 1, 0, 0, 12, 0, 400, 88, 0, 0, 0, 0, 156, 37, 2, 6, 4}, {10, 4, 1, 0, 0, 20, 0, 100, 1, 1, 0, 0, 166, 35, 2, 7, 4}, {11, 2, 0, 0, -8, 0, 400, 132, 0, 0, 0, 0, 168, 37, 2, 7, 4}, {12, 2, 0, 0, 8, 0, 400, 100, 0, 0, 0, 0, 158, 39, 2, 7, 4}, {13, 2, 1, 0, 0, 20, 0, 80, 1, 1, 0, 0, 148, 40, 2, 8, 3}, {14, 2, 0, 0, -12, 0, 400, 60, 0, 0, 0, 0, 150, 41, 2, 8, 3}, {15, 2, 0, 0, 12, 0, 400, 60, 0, 0, 0, 0, 136, 37, 2, 8, 3}, {16, 3, 1, 0, 0, 20, 0, 104, 0, 0, 0, 0, 158, 36, 2, 6, 3}, {17, 1, 0, 0, -12, 0, 400, 100, 0, 0, 0, 0, 166, 35, 2, 6, 4}, {18, 1, 0, 0, 12, 0, 400, 100, 0, 0, 0, 0, 168, 42, 2, 6, 4}, {19, 3, 1, -1, -8, 20, 0, 88, 0, 0, 0, 0, 198, 40, 3, 14, 5}, {20, 3, 1, -1, 8, 20, 0, 72, 0, 0, 0, 0, 188, 42, 3, 14, 5}, {21, 3, 1, 0, 0, 12, 0, 400, 1, 1, 0, 0, 282, 45, 3, 15, 4}, {22, 1, 0, 0, 0, 0, 560, 600, 0, 0, 0, 0, 382, 55, 4, 16, 7}, {23, 1, 1, 0, 0, 8, 180, 640, -1, 1, 0, 0, 383, 52, 4, 17, 8}, {24, 2, 1, 0, 0, 12, 0, 480, 1, 1, 0, 0, 369, 54, 3, 11, 6}, {25, 4, 0, 0, 0, 0, 600, 800, 0, 0, 0, 0, 595, 56, 4, 10, 7}, {26, 2, 1, 0, 0, 12, 0, 800, 1, 1, 0, 0, 478, 52, 3, 14, 4}, {27, 4, 0, 0, 0, 0, 560, 880, 0, 0, 0, 0, 498, 58, 4, 17, 7}, {28, 4, 1, 0, 0, 12, 0, 720, 1, 1, 0, 0, 462, 52, 4, 16, 8}, {29, 4, 1, 0, 0, 12, 0, 320, 1, 1, 0, 0, 290, 46, 3, 6, 5}, {30, 2, 1, 0, 0, 12, 0, 800, 1, 1, 0, 0, 488, 48, 4, 12, 7}, {31, 3, 1, 0, 0, 12, 0, 1040, 1, 1, 0, 0, 580, 62, 5, 13, 8}, {32, 1, 1, 0, 0, 8, 200, 1000, -1, 1, 0, 0, 552, 52, 5, 9, 7}, {33, 3, 1, 0, 0, 12, 0, 1000, 1, 1, 0, 0, 593, 54, 5, 16, 8}, {34, 1, 1, 0, 0, 8, 192, 1200, -1, 1, 0, 0, 758, 56, 5, 17, 7}, {35, 4, 0, 0, 0, 0, 520, 800, 0, 0, 0, 0, 486, 48, 4, 11, 8}, {36, 4, 1, 0, 0, 8, 176, 640, -1, 1, 0, 0, 396, 52, 5, 10, 7}, {37, 3, 1, 0, 0, 12, 0, 1120, 1, 1, 0, 0, 786, 61, 5, 16, 8}, {38, 4, 1, 0, 0, 12, 0, 320, 1, 1, 0, 0, 286, 44, 4, 7, 7}, {39, 4, 1, 0, 0, 12, 0, 320, 1, 1, 0, 0, 298, 46, 4, 15, 7}, {40, 3, 1, 0, 0, 12, 0, 800, 1, 1, 0, 0, 656, 43, 5, 17, 8}, {41, 4, 1, 0, 0, 12, 0, 800, 1, 1, 0, 0, 631, 53, 5, 12, 7}, {42, 4, 1, 0, 0, 12, 0, 600, 1, 1, 0, 0, 428, 61, 5, 13, 8}, {43, 4, 1, 0, 0, 8, 0, 480, 1, 1, 0, 0, 366, 46, 4, 7, 5}, {44, 3, 1, 0, 0, 8, 0, 400, 1, 1, 0, 0, 356, 37, 3, 6, 6}, {45, 4, 1, 0, 0, 24, 0, 60, 0, 0, 0, 0, 388, 42, 3, 11, 5}, {46, 3, 1, 0, 0, 24, 0, 60, 0, 0, 0, 0, 366, 41, 3, 15, 6}, {201, 1, 50, 0, 8, 8, 240, 20000, 0, 170, 0, 0, 6688, 40, 0, 0, 8}, {202, 1, 50, 0, 8, 8, 240, 20000, 0, 170, 0, 0, 6686, 40, 0, 0, 8}, {203, 1, 50, 0, 8, 8, 240, 20000, 0, 170, 0, 0, 6886, 40, 0, 0, 8}, {204, 1, 50, 0, 8, 8, 240, 28000, 0, 170, 0, 0, 8888, 40, 0, 0, 8}, {205, 1, 50, 0, 8, 8, 240, 36000, 0, 170, 0, 0, 9999, 40, 0, 0, 8}};
    public static final int[] enemyPic = new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 12, 6, 3, 3, 3, 3, 3};

    public int type;
    public int colors;
    public int x;
    public int y;
    public int speedX;
    public int speedY;
    public int offset;
    public int health;
    public int aliveTime;
    public int pauseDelay;
    public int picId;
    public int item;
    public int reward;
    public int fireDelay;
    public int bulletMax;
    public int bulletType;
    public int explode;

    public int width;
    public int height;
    Bitmap sourceImg;


    Random random = new Random();

    public EnemyPlane() {
        this.type = 0;
        this.colors = 0;
        this.x = 0;
        this.y = 0;
        this.speedX = 0;
        this.speedY = 0;
        this.offset = 0;
        this.health = 0;
        this.aliveTime = 0;
        this.pauseDelay = 0;
        this.picId = 0;
        this.item = 0;
        this.reward = 0;
        this.fireDelay = 0;
        this.bulletMax = 0;
        this.bulletType = 0;
        this.explode = 0;
    }


    public EnemyPlane(int type, int colors, int x, int y, int speedX, int speedY, int offset, int health, int aliveTime, int pauseDelay, int picId, int item, int reward, int fireDelay, int bulletMax, int bulletType, int explode) {
        this.type = type;
        this.colors = colors;
        this.x = x;
        this.y = y;
        this.speedX = speedX;
        this.speedY = speedY;
        this.offset = offset;
        this.health = health;
        this.aliveTime = aliveTime;
        this.pauseDelay = pauseDelay;
        this.picId = picId;
        this.item = item;
        this.reward = reward;
        this.fireDelay = fireDelay;
        this.bulletMax = bulletMax;
        this.bulletType = bulletType;
        this.explode = explode;
    }

    /**
     * 随机type
     *
     * @param type
     * @return
     */
    public static EnemyPlane mallocEnemy(int mission, int type) {
        EnemyPlane enemy = new EnemyPlane();
        enemy.setValue(enemyData[type][0], enemyData[type][1],
                enemyData[type][2], enemyData[type][3], enemyData[type][4],
                enemyData[type][5], enemyData[type][6], enemyData[type][7],
                enemyData[type][8], enemyData[type][9], enemyData[type][10],
                enemyData[type][11], enemyData[type][12], enemyData[type][13],
                enemyData[type][14], enemyData[type][15], enemyData[type][16]);
        calculateEnemySize(enemy);
        makeRandomAction(enemy);
        return enemy;
    }

    private static void calculateEnemySize(EnemyPlane enemy) {
        Bitmap source = ResInit.enemyImage[enemy.type - 1][enemy.colors];
        enemy.sourceImg = Bitmap.createBitmap(source, source.getWidth() / enemyPic[enemy.type - 1] * enemy.picId, 0, source.getWidth() / enemyPic[enemy.type - 1], source.getHeight());
        enemy.width = enemy.sourceImg.getWidth();
        enemy.height = enemy.sourceImg.getHeight();
    }

    private static void makeRandomAction(EnemyPlane enemy) {
        if (enemy.x == 1) {

        } else {

        }
    }


    public void setValue(int type, int colors, int x, int y, int speedX, int speedY, int offset, int health, int aliveTime, int pauseDelay, int picId, int item, int reward, int fireDelay, int bulletMax, int bulletType, int explode) {
        this.type = type;
        this.colors = colors;
        this.x = x;
        this.y = y;
        this.speedX = speedX;
        this.speedY = speedY;
        this.offset = offset;
        this.health = health;
        this.aliveTime = aliveTime;
        this.pauseDelay = pauseDelay;
        this.picId = picId;
        this.item = item;
        this.reward = reward;
        this.fireDelay = fireDelay;
        this.bulletMax = bulletMax;
        this.bulletType = bulletType;
        this.explode = explode;
    }


    public void setValue(int value) {
        this.type = value;
        this.colors = value;
        this.x = value;
        this.y = value;
        this.speedX = value;
        this.speedY = value;
        this.offset = value;
        this.health = value;
        this.aliveTime = value;
        this.pauseDelay = value;
        this.picId = value;
        this.item = value;
        this.reward = value;
        this.fireDelay = value;
        this.bulletMax = value;
        this.bulletType = value;
        this.explode = value;
    }

    private int getRand(int i) {
        if (i == 0) {
            return 0;
        }
        int r = this.random.nextInt();
        r = (r >> 24) + (r >> 16) + (r >> 8) + r & 0xFF;
        return Math.abs(r % i);
    }

}
