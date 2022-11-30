package com.flyfighter.entity;

import android.graphics.Bitmap;

import com.flyfighter.res.ResInit;
import com.flyfighter.view.MainWindow;

import java.util.Random;

public class EnemyPlane extends Spirit {
    //随机数以100为作为行为变更的分割线,
    public static final int offsetMaxRange = 100;

    public static final int[][] enemyData = new int[][]{{1, 4, 1, 0, 0, 20, 0, 12, 0, 0, 0, 0, 156, 38, 2, 4, 3},
            {2, 2, 0, 0, -12, 0, 100, 15, 0, 0, 0, 0, 168, 41, 2, 4, 3},
            {3, 2, 0, 0, 12, 0, 100, 12, 0, 0, 0, 0, 158, 40, 2, 4, 3},
            {4, 4, 1, 0, 0, 20, 0, 20, 0, 0, 0, 0, 166, 38, 2, 5, 3}, {5, 2, 0, 0, -8, 0, 100, 25, 0, 0, 0, 0, 168, 35, 2, 5, 3},
            {6, 2, 0, 0, 8, 0, 100, 20, 0, 0, 0, 0, 156, 35, 2, 5, 3},
            {7, 3, 1, 0, 0, 20, 0, 28, 1, 1, 0, 0, 146, 41, 2, 6, 4},
            {8, 1, 0, 0, -12, 0, 100, 22, 0, 0, 0, 0, 138, 36, 2, 6, 4},
            {9, 1, 0, 0, 12, 0, 100, 22, 0, 0, 0, 0, 156, 37, 2, 6, 4},
            {10, 4, 1, 0, 0, 20, 0, 25, 1, 1, 0, 0, 166, 35, 2, 7, 4},
            {11, 2, 0, 0, -8, 0, 100, 33, 0, 0, 0, 0, 168, 37, 2, 7, 4},
            {12, 2, 0, 0, 8, 0, 100, 25, 0, 0, 0, 0, 158, 39, 2, 7, 4}, {13, 2, 1, 0, 0, 20, 0, 20, 1, 1, 0, 0, 148, 40, 2, 8, 3}, {14, 2, 0, 0, -12, 0, 100, 15, 0, 0, 0, 0, 150, 41, 2, 8, 3}, {15, 2, 0, 0, 12, 0, 100, 15, 0, 0, 0, 0, 136, 37, 2, 8, 3}, {16, 3, 1, 0, 0, 20, 0, 26, 0, 0, 0, 0, 158, 36, 2, 6, 3}, {17, 1, 0, 0, -12, 0, 100, 25, 0, 0, 0, 0, 166, 35, 2, 6, 4}, {18, 1, 0, 0, 12, 0, 100, 25, 0, 0, 0, 0, 168, 42, 2, 6, 4}, {19, 3, 1, -1, -8, 20, 0, 22, 0, 0, 0, 0, 198, 40, 3, 14, 5}, {20, 3, 1, -1, 8, 20, 0, 18, 0, 0, 0, 0, 188, 42, 3, 14, 5}, {21, 3, 1, 0, 0, 12, 0, 100, 1, 1, 0, 0, 282, 45, 3, 15, 4}, {22, 1, 0, 0, 0, 0, 140, 150, 0, 0, 0, 0, 382, 55, 4, 16, 7}, {23, 1, 1, 0, 0, 8, 45, 160, -1, 1, 0, 0, 383, 52, 4, 17, 8}, {24, 2, 1, 0, 0, 12, 0, 120, 1, 1, 0, 0, 369, 54, 3, 11, 6}, {25, 4, 0, 0, 0, 0, 150, 200, 0, 0, 0, 0, 595, 56, 4, 10, 7}, {26, 2, 1, 0, 0, 12, 0, 200, 1, 1, 0, 0, 478, 52, 3, 14, 4}, {27, 4, 0, 0, 0, 0, 140, 220, 0, 0, 0, 0, 498, 58, 4, 17, 7}, {28, 4, 1, 0, 0, 12, 0, 180, 1, 1, 0, 0, 462, 52, 4, 16, 8}, {29, 4, 1, 0, 0, 12, 0, 80, 1, 1, 0, 0, 290, 46, 3, 6, 5}, {30, 2, 1, 0, 0, 12, 0, 200, 1, 1, 0, 0, 488, 48, 4, 12, 7}, {31, 3, 1, 0, 0, 12, 0, 260, 1, 1, 0, 0, 580, 62, 5, 13, 8}, {32, 1, 1, 0, 0, 8, 50, 250, -1, 1, 0, 0, 552, 52, 5, 9, 7}, {33, 3, 1, 0, 0, 12, 0, 250, 1, 1, 0, 0, 593, 54, 5, 16, 8}, {34, 1, 1, 0, 0, 8, 48, 300, -1, 1, 0, 0, 758, 56, 5, 17, 7}, {35, 4, 0, 0, 0, 0, 130, 200, 0, 0, 0, 0, 486, 48, 4, 11, 8}, {36, 4, 1, 0, 0, 8, 44, 160, -1, 1, 0, 0, 396, 52, 5, 10, 7}, {37, 3, 1, 0, 0, 12, 0, 280, 1, 1, 0, 0, 786, 61, 5, 16, 8}, {38, 4, 1, 0, 0, 12, 0, 80, 1, 1, 0, 0, 286, 44, 4, 7, 7}, {39, 4, 1, 0, 0, 12, 0, 80, 1, 1, 0, 0, 298, 46, 4, 15, 7}, {40, 3, 1, 0, 0, 12, 0, 200, 1, 1, 0, 0, 656, 43, 5, 17, 8}, {41, 4, 1, 0, 0, 12, 0, 200, 1, 1, 0, 0, 631, 53, 5, 12, 7}, {42, 4, 1, 0, 0, 12, 0, 150, 1, 1, 0, 0, 428, 61, 5, 13, 8}, {43, 4, 1, 0, 0, 8, 0, 120, 1, 1, 0, 0, 366, 46, 4, 7, 5}, {44, 3, 1, 0, 0, 8, 0, 100, 1, 1, 0, 0, 356, 37, 3, 6, 6}, {45, 4, 1, 0, 0, 24, 0, 15, 0, 0, 0, 0, 388, 42, 3, 11, 5}, {46, 3, 1, 0, 0, 24, 0, 15, 0, 0, 0, 0, 366, 41, 3, 15, 6},
            {201, 1, 22, 0, 4, 4, 60, 500, 0, 170, 0, 0, 6688, 40, 0, 0, 8},
            {202, 1, 25, 0, 4, 4, 60, 500, 0, 170, 0, 0, 6686, 40, 0, 0, 8},
            {203, 1, 25, 0, 6, 6, 60, 500, 0, 170, 0, 0, 6886, 40, 0, 0, 8},
            {204, 1, 28, 0, 6, 6, 60, 700, 0, 170, 0, 0, 8888, 40, 0, 0, 8},
            {205, 1, 30, 0, 8, 8, 60, 900, 0, 170, 0, 0, 9999, 40, 0, 0, 8}};
    public static final int[] enemyPic = new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 12, 6, 3, 3, 3, 3, 3};

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

    public long shootTime;//对象创建时间


    Random random = new Random();

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

        enemy.initSpiritBitmap();
        enemy.makeRandomAction();//这里是通过初始化的默认enemyData进行初始化行为
        enemy.makeRandomColors();//这里是通过初始化的默认enemyData进行初始化行为

        enemy.makeRandomSpeedAndPosition();

        if (1 == gameDifficulty) {//hard模式
            enemy.health = enemy.health + enemy.health >> 2;
            enemy.fireDelay = enemy.fireDelay - 5;
            enemy.bulletMax = enemy.bulletMax + 1;
            enemy.reward = enemy.reward + enemy.reward >> 1;
        }

        return enemy;
    }

    private void makeRandomColors() {
        colors = getRand(enemyData[type - 1][1]);
    }

    private void makeRandomSpeedAndPosition() {
        patrolX = getRand(MainWindow.windowWidth / 2);
        patrolY = getRand(MainWindow.windowHeight / 6) + MainWindow.windowHeight / 6;
    }

    @Override
    protected void initSpiritBitmap() {
        Bitmap source = ResInit.enemyImage[type - 1][colors - 1];
        //Log.d("TAG", "calculateEnemySize: " + (source == null) + "    " + type + "  " + colors);
        this.source.add(Bitmap.createBitmap(source, source.getWidth() / enemyPic[type - 1] * picId, 0, source.getWidth() / enemyPic[type - 1], source.getHeight()));
        width = this.source.get(0).getWidth();
        height = this.source.get(0).getHeight();
    }

    private void makeRandomAction() {
        if (x == 1) {//初始化的x
            if (y == -1) {
                y = getRand(0 - height);
                x = getRand(MainWindow.windowWidth - width);
                reward += getRand(reward);
                return;
            }
            speedY = speedY + getRand(2);
            speedX = speedX + (2 - getRand(5));

            y = 0 - height;
            x = new Random().nextInt(MainWindow.windowWidth - width);
            if (getRand(2) == 1) {
                switch (getRand(4)) {
                    case 0:
                    case 1:
                        pauseDelay = 0;
                        break;
                    case 2://过了巡逻时间 ,自动出场
                        pauseDelay = (getRand(10) + 5) * 1000;
                        break;
                    default://不停巡逻的这种
                        pauseDelay = (1000 * 1000);
                        break;
                }
            }
        } else {
            if (speedX == 0) {
                speedX = getRand(2) == 1 ? 2 : -2;
            }
            if (speedX > 0) {
                x = (0 - width);
            } else {
                x = MainWindow.windowWidth;
            }

            y = getRand(140);
            if (speedY == 0) {
                int rand = getRand(3);
                if (rand == 1) {
                    speedY = 1;
                } else if (rand == 2) {
                    speedY = -1;
                }
            }
            if (speedY < 0) {
                int disY = (MainWindow.windowWidth / 2 / Math.abs(speedX) * Math.abs(speedY));
                if (y < disY) {
                    y = disY + getRand(50);
                }
            } else if (speedY > 0) {
                y = getRand(270);
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

    @Override
    public void dealMoveState() {
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
                if (x <= 0 || x >= MainWindow.windowWidth - width) {
                    speedX = -speedX;
                } else if (Math.abs(x - patrolX) <= 10) {
                    speedX = -speedX;
                }
            }
        }
    }

    @Override
    public Bitmap getFrame() {
        return firstFrame();
    }


}
