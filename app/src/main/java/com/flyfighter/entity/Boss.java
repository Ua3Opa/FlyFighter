package com.flyfighter.entity;

import android.graphics.Bitmap;

import com.flyfighter.res.ResInit;
import com.flyfighter.view.MainWindow;

import java.util.Random;

public class Boss extends EnemyPlane {

    public int bulletDirect1;
    public int bulletDirect2;
    public int bulletDirect3;

    public int moveDownRange = 250;
    public int moveUpRange = 90;
    public int moveAction;

    public boolean initDirect = true;
    public Random random = new Random();
    public long lastShootTime;
    public long shootDuration;
    public int bulletSequence;
    public int bulletNum;
    public boolean bossAppear = false;

    public Boss() {
    }

    public static Boss makeBossByMission(int mMission, int gameDifficulty) {
        int type = 45 + mMission;
        Boss enemy = new Boss();
        enemy.setValue(enemyData[type][0], enemyData[type][1],
                enemyData[type][2], enemyData[type][3], enemyData[type][4],
                enemyData[type][5], enemyData[type][6], enemyData[type][7],
                enemyData[type][8], enemyData[type][9], enemyData[type][10],
                enemyData[type][11], enemyData[type][12], enemyData[type][13],
                enemyData[type][14], enemyData[type][15], enemyData[type][16]);

        enemy.type = type;
        enemy.moveAction = enemy.getRand(3);
        enemy.picNum = 3;
        enemy.initSpiritBitmap();
        enemy.initSpiritSize();

        if (0 == gameDifficulty) {//hard模式
            enemy.health = (int) (enemy.health * 0.75);
        } else {
            enemy.health = (int) (enemy.health * 1.25);
        }
        return enemy;
    }

    boolean appear = false;


    @Override
    public void dealMoveState() {
        if (!appear) {
            if (x == 0 && y == 0) {
                if (moveAction == 0) {
                    x = -width;
                    y = getRand(moveDownRange);
                } else if (moveAction == 1) {
                    x = MainWindow.windowWidth / 2 - width / 2;
                    y = -height;
                } else {
                    x = MainWindow.windowWidth;
                    y = getRand(moveDownRange);
                }
            }
            if (moveAction == 0) {
                x += speedX;
            } else if (moveAction == 1) {
                y += speedY;
            } else {
                x -= speedX;
            }
            if ((x > 0 && x < MainWindow.windowWidth - width) && y >= 0) {
                appear = true;
                moveAction = 1;
            }
            return;
        }
        recordMovePosition();
        initBossMoveAction();
        // ↑ ↓ ← → ↖ ↗ ↘ ↙ ↕
        switch (moveAction) {
            case 0://↑
                y -= speedY;
                break;
            case 1://↓
                y += speedY;
                break;
            case 2://←
                x -= speedX;
                break;
            case 3://→
                x += speedX;
                break;
            case 4://↖
                x -= speedX;
                y -= speedY;
                break;
            case 5://↗
                x += speedX;
                y -= speedY;
                break;
            case 6://↙
                x -= speedX;
                y += speedY;
                break;
            case 7://↘
                x += speedX;
                y += speedY;
                break;
        }
    }

    private void initBossMoveAction() {
        int direction;
        // ↑ ↓ ← → ↖ ↗ ↘ ↙ ↕
        if (initDirect) {
            if (y > -(speedY * 2)) {
                direction = getRand(4);
                if (direction == 0) {
                    moveAction = 2;//←
                } else if (direction == 1) {
                    moveAction = 3;//→
                }
                if (direction == 2) {
                    moveAction = 6;//↙
                } else {
                    moveAction = 7;//↙
                }
            }
            initDirect = false;
        } else if (moveAction == 1 && y > moveDownRange) {//↓ 到达底部位置,变向
            direction = getRand(3);
            if (x + width / 3 < 0) {
                if (direction == 0) {
                    moveAction = 0;//↑
                } else if (direction == 1) {
                    moveAction = 3;//→
                } else {
                    moveAction = 5;//↗
                }
            } else if (x + width / 3 * 2 > MainWindow.windowWidth) {
                if (direction == 0) {
                    moveAction = 0;//↑
                } else if (direction == 1) {
                    moveAction = 2;//←
                } else {
                    moveAction = 4;//↖
                }
            }
        } else if (moveAction == 0 && y < moveUpRange) {
            direction = getRand(3);
            if (x + width / 3 < 0) {
                if (direction == 0) {
                    moveAction = 1;//↓
                } else if (direction == 1) {
                    moveAction = 3;//→
                } else {
                    moveAction = 7;//↘
                }
            } else if (x + width / 3 * 2 > MainWindow.windowWidth) {
                if (direction == 0) {
                    moveAction = 1;//↓
                } else if (direction == 1) {
                    moveAction = 2;//←
                } else {
                    moveAction = 6;//↙
                }
            }
        } else if (moveAction == 2 && x + width / 3 < 0) {//左移超过边界
            direction = getRand(3);
            if (y < moveUpRange) {
                if (direction == 0) {
                    moveAction = 1;//↓
                } else if (direction == 1) {
                    moveAction = 3;//→
                } else {
                    moveAction = 7;//↘
                }
            } else if (y > moveUpRange) {
                if (direction == 0) {
                    moveAction = 0;//↑
                } else if (direction == 1) {
                    moveAction = 3;//→
                } else {
                    moveAction = 5;//↗
                }
            }
        } else if (moveAction == 3 && x + width / 3 * 2 > MainWindow.windowWidth) {//右移超过边界
            direction = getRand(3);
            if (y < moveUpRange) {
                if (direction == 0) {
                    moveAction = 1;//↓
                } else if (direction == 1) {
                    moveAction = 2;//←
                } else {
                    moveAction = 6;//↙
                }
            } else if (y > moveUpRange) {
                if (direction == 0) {
                    moveAction = 0;//↑
                } else if (direction == 1) {
                    moveAction = 2;//←
                } else {
                    moveAction = 4;//↖
                }
            }
        } else if (moveAction != 4 || y >= moveUpRange && x + width / 3 >= 0) {
            if (moveAction == 6 && (y > moveDownRange || x + width / 3 < 0)) {
                direction = getRand(2);
                if (y > moveDownRange) {
                    if (direction != 0) {
                        moveAction = 2;//←
                    } else {
                        moveAction = 4;//↖
                    }
                } else if (direction != 0) {
                    moveAction = 1;//↓
                } else {
                    moveAction = 7;//↘
                }
            } else if (moveAction != 5 || y >= moveUpRange && x + width / 3 * 2 <= MainWindow.windowWidth) {
                if (moveAction == 7 && (y > moveDownRange || x > MainWindow.windowWidth - width / 3 * 2)) {
                    direction = getRand(2);
                    if (y > moveDownRange) {
                        if (direction != 0) {
                            moveAction = 3;//→
                        } else {
                            moveAction = 5;//↗
                        }
                    } else if (direction != 0) {
                        moveAction = 1;//↓
                    } else {
                        moveAction = 6;//↙
                    }
                }
            } else {
                direction = getRand(2);
                if (y < moveUpRange) {
                    if (direction != 0) {
                        moveAction = 3;//→
                    } else {
                        moveAction = 7;//↘
                    }
                } else if (direction != 0) {
                    moveAction = 0;//↑
                } else {
                    moveAction = 4;//↖
                }
            }
        } else {
            direction = getRand(2);
            if (y < moveUpRange) {
                if (direction != 0) {
                    moveAction = 2;//←
                } else {
                    moveAction = 6;//↙
                }
            } else if (direction != 0) {//↑
                moveAction = 0;//↑
            } else {
                moveAction = 5;//↗
            }
        }
    }

    @Override
    protected void initSpiritBitmap() {
        Bitmap source = ResInit.enemyImage[type][0];
        this.source.addAll(splitBitmap(source, 3));
    }

    @Override
    public Bitmap getFrame() {
        if (System.currentTimeMillis() - lastFrameTime >= animDuration) {
            frameIndex++;
        }
        int picIndex;
        if (hit) {
            picIndex = 2;
            hit = false;
        } else {
            picIndex = frameIndex % (2);
        }
        return source.get(picIndex);
    }

    public int getRand(int i) {
        if (i == 0) {
            return 0;
        }
        int r = random.nextInt();
        r = (r >> 24) + (r >> 16) + (r >> 8) + r & 0xFF;
        return Math.abs(r % i);
    }
}
