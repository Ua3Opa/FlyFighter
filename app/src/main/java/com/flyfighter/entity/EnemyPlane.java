package com.flyfighter.entity;

import android.graphics.Bitmap;

import com.flyfighter.res.ResInit;
import com.flyfighter.view.MainWindow;

import java.util.Random;

public class EnemyPlane extends Spirit {
    //随机数以100为作为行为变更的分割线,
    public static final int offsetMaxRange = 100;

    public static final int[][] enemyData = new int[][]{
            //3,4位 00是横向的,需要根据速度去判断屏幕左右侧出现
            //3,4位 10是竖直向下的,
            //3,4位 1, -1 ,坐下或者右下
            {1, 4, 1, 0, 0, 12, 0, 12, 0, 0, 0, 0, 156, 1266, 2, 4, 3},
            {2, 2, 0, 0, -7, 0, 100, 15, 0, 0, 0, 0, 168, 1366, 2, 4, 3},
            {3, 2, 0, 0, 7, 0, 100, 12, 0, 0, 0, 0, 158, 1333, 2, 4, 3},
            {4, 4, 1, 0, 0, 12, 0, 20, 0, 0, 0, 0, 166, 1266, 2, 5, 3},
            {5, 2, 0, 0, -5, 0, 100, 25, 0, 0, 0, 0, 168, 1166, 2, 5, 3},
            {6, 2, 0, 0, 5, 0, 100, 20, 0, 0, 0, 0, 156, 1166, 2, 5, 3},
            {7, 3, 1, 0, 0, 12, 0, 28, 1, 1, 0, 0, 146, 1366, 2, 6, 4},
            {8, 1, 0, 0, -7, 0, 100, 22, 0, 0, 0, 0, 138, 1200, 2, 6, 4},
            {9, 1, 0, 0, 7, 0, 100, 22, 0, 0, 0, 0, 156, 1233, 2, 6, 4},
            {10, 4, 1, 0, 0, 12, 0, 25, 1, 1, 0, 0, 166, 1166, 2, 7, 4},
            {11, 2, 0, 0, -5, 0, 100, 33, 0, 0, 0, 0, 168, 1233, 2, 7, 4},
            {12, 2, 0, 0, 5, 0, 100, 25, 0, 0, 0, 0, 158, 1300, 2, 7, 4},
            {13, 2, 1, 0, 0, 12, 0, 20, 1, 1, 0, 0, 148, 1333, 2, 8, 3},
            {14, 2, 0, 0, -7, 0, 100, 15, 0, 0, 0, 0, 150, 1366, 2, 8, 3},
            {15, 2, 0, 0, 7, 0, 100, 15, 0, 0, 0, 0, 136, 1233, 2, 8, 3},
            {16, 3, 1, 0, 0, 12, 0, 26, 0, 0, 0, 0, 158, 1200, 2, 6, 3},
            {17, 1, 0, 0, -7, 0, 100, 25, 0, 0, 0, 0, 166, 1166, 2, 6, 4},
            {18, 1, 0, 0, 7, 0, 100, 25, 0, 0, 0, 0, 168, 1400, 2, 6, 4},
            {19, 3, 1, -1, -5, 12, 0, 22, 0, 0, 0, 0, 198, 1333, 3, 14, 5},
            {20, 3, 1, -1, 5, 12, 0, 18, 0, 0, 0, 0, 188, 1400, 3, 14, 5},
            {21, 3, 1, 0, 0, 7, 0, 100, 1, 1, 0, 0, 282, 1500, 3, 15, 4},
            {22, 1, 0, 0, 0, 0, 140, 150, 0, 0, 0, 0, 382, 1833, 4, 16, 7},
            {23, 1, 1, 0, 0, 5, 45, 160, -1, 1, 0, 0, 383, 1733, 4, 17, 8},
            {24, 2, 1, 0, 0, 7, 0, 120, 1, 1, 0, 0, 369, 1800, 3, 11, 6},
            {25, 4, 0, 0, 0, 0, 150, 200, 0, 0, 0, 0, 595, 1866, 4, 10, 7},
            {26, 2, 1, 0, 0, 7, 0, 200, 1, 1, 0, 0, 478, 1733, 3, 14, 4},
            {27, 4, 0, 0, 0, 0, 140, 220, 0, 0, 0, 0, 498, 1933, 4, 17, 7},
            {28, 4, 1, 0, 0, 7, 0, 180, 1, 1, 0, 0, 462, 1733, 4, 16, 8},
            {29, 4, 1, 0, 0, 7, 0, 80, 1, 1, 0, 0, 290, 1533, 3, 6, 5},
            {30, 2, 1, 0, 0, 7, 0, 200, 1, 1, 0, 0, 488, 1600, 4, 12, 7},
            {31, 3, 1, 0, 0, 7, 0, 260, 1, 1, 0, 0, 580, 2066, 5, 13, 8},
            {32, 1, 1, 0, 0, 5, 50, 250, -1, 1, 0, 0, 552, 1733, 5, 9, 7},
            {33, 3, 1, 0, 0, 7, 0, 250, 1, 1, 0, 0, 593, 1800, 5, 16, 8},
            {34, 1, 1, 0, 0, 5, 48, 300, -1, 1, 0, 0, 758, 1866, 5, 17, 7},
            {35, 4, 0, 0, 0, 0, 130, 200, 0, 0, 0, 0, 486, 1600, 4, 11, 8},
            {36, 4, 1, 0, 0, 5, 44, 160, -1, 1, 0, 0, 396, 1733, 5, 10, 7},
            {37, 3, 1, 0, 0, 7, 0, 280, 1, 1, 0, 0, 786, 2033, 5, 16, 8},
            {38, 4, 1, 0, 0, 7, 0, 80, 1, 1, 0, 0, 286, 1466, 4, 7, 7},
            {39, 4, 1, 0, 0, 7, 0, 80, 1, 1, 0, 0, 298, 1533, 4, 15, 7},
            {40, 3, 1, 0, 0, 7, 0, 200, 1, 1, 0, 0, 656, 1433, 5, 17, 8},
            {41, 4, 1, 0, 0, 7, 0, 200, 1, 1, 0, 0, 631, 1766, 5, 12, 7},
            {42, 4, 1, 0, 0, 7, 0, 150, 1, 1, 0, 0, 428, 2033, 5, 13, 8},
            {43, 4, 1, 0, 0, 5, 0, 120, 1, 1, 0, 0, 366, 1533, 4, 7, 5},
            {44, 3, 1, 0, 0, 5, 0, 100, 1, 1, 0, 0, 356, 1233, 3, 6, 6},
            {45, 4, 1, 0, 0, 15, 0, 15, 0, 0, 0, 0, 388, 1400, 3, 11, 5},
            //45
            {46, 3, 1, 0, 0, 15, 0, 15, 0, 0, 0, 0, 366, 1366, 3, 15, 6},
            //46
            {201, 1, 0, 0, 5, 5, 60, 3000, 0, 170, 0, 0, 6688, 1333, 0, 0, 8},
            {202, 1, 0, 0, 5, 5, 60, 4000, 0, 170, 0, 0, 6686, 1333, 0, 0, 8},
            {203, 1, 0, 0, 5, 5, 60, 5000, 0, 170, 0, 0, 6886, 1333, 0, 0, 8},
            {204, 1, 0, 0, 5, 5, 60, 6000, 0, 170, 0, 0, 8888, 1333, 0, 0, 8},
            {205, 1, 0, 0, 5, 5, 60, 8000, 0, 170, 0, 0, 9999, 1333, 0, 0, 8}};
    public static final int[] enemyPic = new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 12, 6, 3, 3, 3, 3, 3};

    public static float[] offsetY = new float[]{
            0.8f, 0.8f, 0.8f, 0.8f, 0.8f,
            0.8f, 0.8f, 0.8f, 0.8f, 0.8f,
            0.8f, 0.8f, 0.8f, 0.8f, 0.8f,
            0.8f, 0.8f, 0.8f, 0.8f, 0.8f,
            0.8f, 0.8f, 0.7f, 0.8f, 0.8f,
            0.8f, 0.8f, 0.8f, 0.8f, 0.7f,
            0.8f, 0.8f, 0.8f, 0.8f, 0.8f,
            0.8f, 0.8f, 0.8f, 0.8f, 0.8f,

            0.8f, 0.8f, 0.8f, 0.8f, 0.8f,
            0.8f,
            0.8f, 0.9f, 0.8f, 0.8f, 0.8f,//boss
    };

    public int colors;
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

    public int patrolY;//最大的巡逻Y范围
    public int patrolX;//最大的巡逻X范围
    public long patrolTime;//开始巡逻的时间

    public long shootTime;//发射时间,默认为创建时间

    public boolean hit;

    public EnemyPlane() {
    }

    /**
     * 随机type
     *
     * @param type
     * @return
     */
    public static EnemyPlane mallocEnemy(int type, int gameDifficulty) {
        EnemyPlane enemy = new EnemyPlane();
        enemy.setValue(enemyData[type][0], enemyData[type][1],
                enemyData[type][2], enemyData[type][3], enemyData[type][4],
                enemyData[type][5], enemyData[type][6], enemyData[type][7],
                enemyData[type][8], enemyData[type][9], enemyData[type][10],
                enemyData[type][11], enemyData[type][12], enemyData[type][13],
                enemyData[type][14], enemyData[type][15], enemyData[type][16]);
        if (type == 45 || type == 46) {
            enemy.animDuration *= 3;
        }
        enemy.initSpiritBitmap();
        enemy.initSpiritSize();
        enemy.makeRandomAction();
        enemy.makeRandomSpeedAndPosition();

        enemy.shootTime = System.currentTimeMillis() - enemy.getRand(enemy.fireDelay) - enemy.fireDelay / 2;
        enemy.fireDelay = (int) (enemy.fireDelay * 1.4);

        if (1 == gameDifficulty) {//hard模式
            enemy.health = enemy.health + enemy.health >> 2;
            enemy.fireDelay = enemy.fireDelay - 5;
            enemy.bulletMax = enemy.bulletMax + 1;
            enemy.reward = enemy.reward + enemy.reward >> 1;
        }

        return enemy;
    }

    private void makeRandomSpeedAndPosition() {
        patrolX = getRand(MainWindow.windowWidth / 4);
        patrolY = getRand(MainWindow.windowHeight / 6) + MainWindow.windowHeight / 6;
    }


    private void makeRandomAction() {
        //3,4位 00是横向的,需要根据速度去判断屏幕左右侧出现
        //3,4位 10是竖直向下的,
        //3,4位 1, -1 ,向或者右下
        if (x == 1) {//初始化的x
            if (y == -1) {//屏幕正上方
                int rand = getRand(2);
                if (speedX < 0) {//右侧出现
                    if (rand == 0) {//顶部往左下
                        y = -height;
                        x = (int) (getRand((int) (MainWindow.windowWidth * 0.25)) + 0.75 * MainWindow.windowWidth);
                    } else {//右侧往左下
                        y = getRand((int) (MainWindow.windowHeight * 0.20));
                        x = MainWindow.windowWidth;
                    }
                } else {
                    if (rand == 0) {//顶部往右下
                        y = -height;
                        x = getRand((int) (MainWindow.windowWidth * 0.25));
                    } else {//左侧侧往右下
                        y = getRand((int) (MainWindow.windowHeight * 0.20));
                        x = 0;
                    }
                }
                reward += getRand(reward);
                return;
            }
            //10是竖直向下的,
            int dirX = getRand(2) == 0 ? -1 : 1;
            speedX = dirX * getRand(5) + 2;
            speedY = speedY + getRand(3);

            y = 0 - height;
            x = getRand(MainWindow.windowWidth - width);
            if (type == 45 || type == 46) {
                speedY += getRand(6);
                return;
            }
            if (getRand(2) == 1) {
                switch (getRand(4)) {
                    case 0:
                    case 1:
                        pauseDelay = 0;
                        break;
                    case 2://过了巡逻时间 ,自动出场
                        pauseDelay = (getRand(8) + 5) * 1000;
                        break;
                    default://不停巡逻的这种
                        pauseDelay = (1000 * 1000);
                        break;
                }
            }
        } else {//x =0
            // 00是横向的
            if (speedX == 0) {
                int direction = getRand(2) == 1 ? 1 : -1;
                speedX = direction * (getRand(3) + 1);
            }

            if (speedX > 0) {
                x = -width;
            } else {
                x = MainWindow.windowWidth;
            }

            y = getRand(MainWindow.windowHeight / 8);
            if (speedY == 0) {
                int randomY = getRand(3) - 2;
                speedY = randomY * (getRand(2) + 1);
            }
            if (speedY < 0) {
                int disY = (MainWindow.windowWidth / 2 / Math.abs(speedX) * Math.abs(speedY));
                if (y < disY) {
                    y = disY + getRand(MainWindow.windowHeight / 8);
                }
            } else if (speedY > 0) {
                y = getRand(MainWindow.windowHeight / 8);
            }
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

    private int getRand(int i) {
        return new Random().nextInt(i);
    }

    @Override
    public void dealMoveState() {
        recordMovePosition();
        if (pauseDelay == 0) {
            x = (x + speedX);
            y = (y + speedY);
        } else {
            x = x + speedX;
            if (y <= patrolY) {
                y = y + speedY;
            } else {
                if (patrolTime == 0) {
                    patrolTime = System.currentTimeMillis();
                } else if (System.currentTimeMillis() - patrolTime > pauseDelay) {
                    y = y + speedY;
                }
                if ((x <= 0 && speedX < 0) || (x >= MainWindow.windowWidth - width && speedX > 0)) {
                    speedX = -speedX;
                } else if (x <= patrolX || x >= MainWindow.windowWidth - patrolX) {
                    speedX = -speedX;
                }
            }
        }
    }

    @Override
    protected void initSpiritBitmap() {
        Bitmap source = ResInit.enemyImage[type - 1][getRand(colors)];
        this.source.addAll(splitBitmap(source, enemyPic[type - 1]));
    }

    @Override
    public Bitmap getFrame() {
        super.getFrame();
        int picIndex;

        if (type == 45 || type == 46) {
            picIndex = frameIndex % (enemyPic[type - 1] - 1);
        } else {
            if (hit) {
                picIndex = enemyPic[type - 1] - 1;
                hit = false;
            } else {
                picIndex = frameIndex % (enemyPic[type - 1] - 1);
            }
        }
        return source.get(picIndex);
    }

    @Override
    public int[][] buildRect() {
        int[][] rect = new int[4][2];
        rect[0] = new int[]{x, y};//t
        rect[1] = new int[]{x + width, y};//r
        rect[2] = new int[]{x + width, (int) (y + height * offsetY[type])};//b
        rect[3] = new int[]{x, (int) (y + height * offsetY[type])};//lb
        return rect;
    }
}
