package com.flyfighter.menu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.flyfighter.entity.Bullet;
import com.flyfighter.entity.EnemyPlane;
import com.flyfighter.entity.PlayerPlane;
import com.flyfighter.entity.RectArea;
import com.flyfighter.interf.Controller;
import com.flyfighter.res.RMS;
import com.flyfighter.res.ResInit;
import com.flyfighter.res.SoundHelper;
import com.flyfighter.utils.UiUtils;
import com.flyfighter.view.MainWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameCanvas extends SurfaceView implements SurfaceHolder.Callback, Runnable, Controller {

    private Context context;
    private MediaPlayer[] mediaPlayers = new MediaPlayer[13];

    private int playerType;
    private int difficulty;
    private PlayerPlane mPlayer;
    private List<EnemyPlane> enemys = new ArrayList<>();
    private RectArea gSrcRect;
    private Random random;

    private final int maxEnemy = 5;
    private boolean isThreadAlive = false;
    private boolean gIsLoadGame = true;
    private boolean gIsSaved;
    private int gContinueNum;
    private boolean gIsGameFinished;
    private boolean gIsPlayerFire;
    private int mMission;
    private int gGameScore;
    private int gMissileType;
    private int gCurrentMissileMax;
    private int gOtherTypeBomb;
    private int gGameSecretNum;
    private int gGamePause;
    private boolean gIsMissionComplete;
    private int gTempDelay;
    private int gBombDelay;
    private boolean mIsEnableEnemy;
    private boolean gIsGameOver;
    private boolean gIsBombing;
    private boolean gIsBossAppear;
    private boolean gBossInitDirect;
    private int gBossIndex;
    private int gPlayerInit;
    private int gScreenMove;
    private int gBackgroundOffset;
    private int gEnemyCount;
    private int gBulletCount;
    private int gExplodeCount;
    private int gItemCount;
    private int gAppearEnemyCount;
    private int gMissileCount;
    private int gBossBulletSequenceMax;
    private int gBossBulletNum;
    private int gBossShootDelay;
    private int gFireDelay;
    private int gApearEnemyType;
    private boolean mIsPlayerDestroyed;
    private int gBossBulletDirect1;
    private int gBossBulletDirect2;
    private int gBossBulletDirect3;
    private int mCorrespondMission;
    private int gBossMoveDownRange;
    private int gBossMoveUpRange;
    private int mGameDifficulty;
    private int gBossMoveAction;
    private int gBackgroundHeight;

    private static int it_type = 0;
    private static int it_x = 1;
    private static int it_y = 2;
    private static int it_picid = 3;
    private static int it_speedx = 4;
    private static int it_speedy = 5;

    private static final int[] backGroundHeight = new int[5];

    public List<Bullet> bullets = new ArrayList<>();

    public static final int[][] stageEnemy = new int[][]{
            {1, 2, 4, 38, 44, 19, 39, 22, 45, 26, 46, 25, 43, 36, 40},
            {5, 2, 4, 24, 29, 27, 46, 21, 23, 39, 45, 30, 28, 33, 35},
            {6, 4, 3, 43, 20, 39, 23, 31, 45, 34, 38, 46, 37, 32, 41},
            {19, 45, 20, 46, 19, 45, 20, 46, 19, 45, 20, 46, 19, 45, 20},
            {46, 44, 43, 22, 20, 39, 23, 31, 45, 34, 38, 46, 37, 32, 41}
    };

    private static final int[] enemySize = new int[]{126, 103, 108, 126, 108, 126, 135, 117, 117, 135, 117, 135, 171, 126, 126, 171, 126, 171, 189, 126, 126, 189, 126, 189, 153, 94, 99, 153, 99, 153, 144, 121, 153, 162, 153, 162, 171, 198, 171, 198, 333, 139, 261, 180, 261, 180, 252, 139, 423, 130, 333, 162, 405, 157, 369, 144, 243, 175, 423, 126, 378, 144, 279, 189, 279, 189, 315, 162, 189, 265, 189, 265, 333, 153, 279, 144, 252, 139, 378, 157, 315, 180, 306, 202, 243, 126, 207, 108, 171, 135, 171, 238, 576, 288, 504, 360, 927, 270, 684, 279, 765, 238};

    private static final int[] gGetItemsList = new int[]
            {5, 5, 5, 6, 6, 6, 7, 7, 8, 1, 2, 3, 4, 12, 9, 9, 10, 10, 11, 11};

    public static final int[] bulletPic = new int[]{2, 3, 3, 1, 4, 2, 2, 2, 4, 4, 4, 4, 4, 4, 4, 2, 2, 2, 2, 2, 2};


    private static final int[] explodePic = new int[]{4, 4, 6, 5, 6, 6, 6, 6};
    private static final int[] explodeSize = new int[]{14, 12, 14, 12, 40, 40, 50, 40, 42, 32, 42, 32, 42, 42, 42, 42};
    private static final int[] enemyPic = new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 12, 6, 3, 3, 3, 3, 3};

    private boolean[] gKey_State = new boolean[]{false, false, false, false, true, true, true};

    private static final int[][][] bossBullet = new int[][][]{
            {{13, 3, 17, 35}, {3, 2, 11, 35}, {13, 4, 20, 45},
                    {21, 6, 21, 33}, {2, 4, 10, 28}, {13, 6, 6, 36},
                    {3, 3, 19, 44}, {16, 6, 16, 28}},
            {{3, 2, 18, 36}, {22, 6, 21, 25}, {13, 4, 17, 34}, {2, 4, 20, 46},
                    {16, 6, 12, 38}, {21, 6, 21, 28}, {3, 3, 19, 44}, {12, 5, 17, 26}},
            {{13, 3, 19, 37}, {3, 2, 13, 33}, {16, 6, 10, 45}, {22, 6, 21, 28},
                    {3, 3, 16, 48}, {13, 4, 20, 34}, {16, 6, 9, 46}, {3, 2, 13, 28}},
            {{3, 2, 16, 30}, {13, 3, 20, 30}, {3, 3, 11, 40}, {22, 6, 21, 20}, {16, 6, 10, 35}, {13, 4, 6, 30},
                    {3, 3, 19, 45}, {12, 5, 17, 20}},
            {{3, 4, 16, 20}, {13, 6, 20, 25}, {3, 4, 11, 30}, {23, 6, 21, 20}, {16, 6, 10, 35}, {13, 5, 6, 25},
                    {3, 5, 19, 20}, {12, 6, 17, 25}}};

    private static final int[][] playerBulletData = new int[][]{{0, 9, -2, 0, -23}, {1, 3, 0, 0, -20}, {1, 15, 0, 0, -20}, {1, 3, 0, -1, -20}, {0, 9, -7, 0, -25}, {1, 15, 0, 1, -20}, {1, 0, 0, -1, -20}, {0, 4, 0, 0, -25}, {0, 14, 0, 0, -25}, {1, 18, 0, 1, -20}, {0, 1, 0, -2, -23}, {1, 5, -2, -1, -24}, {0, 9, 0, 0, -28}, {1, 13, -2, 1, -24}, {0, 17, 0, 2, -23}, {1, 10, 0, 0, -23}, {1, 4, 0, 0, -23}, {1, 15, 0, 0, -23}, {0, 4, 0, -1, -23}, {1, 10, 0, 0, -30}, {2, 15, 0, 1, -23}, {0, 1, 0, -1, -23}, {1, 5, 0, 0, -30}, {1, 14, 0, 0, -30}, {2, 18, 0, 1, -23}, {0, 1, 0, -1, -23}, {0, 3, 0, 0, -27}, {1, 10, 2, 0, -33}, {2, 16, 0, 0, -27}, {2, 17, 0, 1, -23}, {1, 14, -5, 0, -23}, {2, 6, 0, 0, -23}, {2, 22, 0, 0, -23}, {2, 4, 1, 0, -23}, {1, 14, -5, 0, -30}, {2, 24, 1, 0, -23}, {2, 2, 1, 0, -23}, {1, 10, -5, 0, -30}, {1, 18, -5, 0, -30}, {2, 26, 1, 0, -23}, {2, -2, -2, 0, -23}, {1, 4, -5, 0, -30}, {1, 14, -2, 0, -30}, {1, 24, -5, 0, -30}, {2, 31, -2, 0, -23}};

    private static final int[] bulletSize = new int[]{7, 22, 9, 20, 2, 20, 8, 8, 10, 8, 8, 8, 8, 8, 8, 8, 14, 13, 14, 13, 10, 9, 10, 9, 10, 9, 8, 8, 8, 8, 12, 12, 12, 12, 16, 16, 16, 16, 16, 16, 8, 320};

    private static final int[] bulletSpeedSL = new int[]{-2, 1, -2, 2, -1, 2, -1, 3, 0, 3, 1, 3, 1, 2, 2, 2, 2, 1};

    private static final int[][] playerBulletFloorPower = new int[][]{{6, 6, 5, 4, 4}, {13, 11, 8, 7, 6}, {11, 6, 6, 6, 5}};

    private static final int[] enemyBulletToFront = new int[]{0, 18, -4, 18, 4, 18, -4, 18, 18, 4, 18, -4, 18, 18, 4, 18, -9, 13, -4, 18, 18, 4, 18, 9, 13, -9, 13, -4, 18, 18, 4, 18, 9, 13};

    private static final int[] playerSize = new int[]{26, 32, 30, 28, 30, 28};

    private static final int[] bulletSpeedMissile = new int[]{0, -7, -3, -7, -5, -5, -7, -3, -7, 0, -7, 3, -5, 5, -3, 7, 0, 7, 3, 7, 5, 5, 7, 3, 7, 0, 7, -3, 5, -5, 3, -7};

    private static final int[][] playerBulletFloorNum = new int[][]{{1, 2, 3, 4, 5, 0}, {1, 2, 3, 4, 5, 15}, {1, 2, 3, 4, 5, 30}};


    private EnemyPlane[] gEnemy = new EnemyPlane[5];

    private int[][] mBullets = new int[100][6];

    private int[][] gMissile = new int[4][7];

    private int[][] gExplode = new int[30][4];

    private int[][] gItem = new int[6][6];

    private boolean[] gShoot_SL = new boolean[5];
    private int[] gShootNum_SL = new int[5];
    private static byte ex_type = 0;
    private Paint mPaint = new Paint();
    private final SurfaceHolder surfaceHolder;

    public GameCanvas(Context context, int playerType) {
        super(context, null);
        this.context = context;

        mPaint.setAntiAlias(true);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        this.playerType = playerType;
        this.difficulty = RMS.difficulty;

        this.gSrcRect = new RectArea();
        this.random = new Random();

        gameInit();
        stageInit();
    }

    private void gameInit() {
        ResInit.loadPlayer(context, playerType);
        SoundHelper.loadGameSounds(context, mediaPlayers);

        this.mPlayer = PlayerPlane.createPlayerPlane(playerType);

        this.gIsSaved = false;
        this.gContinueNum = 3;
        this.gIsGameFinished = false;
        this.gIsPlayerFire = false;
        this.mMission = 1;
        this.gGameScore = 0;
        this.gMissileType = 0;
        this.gCurrentMissileMax = 0;
        this.gOtherTypeBomb = 0;
        this.gGameSecretNum = -1;
    }

    private void stageInit() {
        this.gGamePause = 0;
        this.gIsMissionComplete = false;
        this.gTempDelay = 0;
        this.gBombDelay = 0;
        this.mIsEnableEnemy = false;
        this.gIsGameOver = false;
        this.gIsBombing = false;
        this.gIsBossAppear = false;
        this.gBossInitDirect = true;
        this.gBossIndex = -1;
        this.gPlayerInit = 1;
        this.gScreenMove = 1;
        this.gBackgroundOffset = 0;
        this.gEnemyCount = 0;
        this.gBulletCount = 0;
        this.gExplodeCount = 0;
        this.gItemCount = 0;
        this.gAppearEnemyCount = 0;
        this.gMissileCount = 0;
        this.gBossBulletSequenceMax = 0;
        this.gBossBulletNum = 0;
        this.gBossShootDelay = 0;
        this.gFireDelay = 0;
        this.gApearEnemyType = 1;
        this.mIsPlayerDestroyed = false;

        this.gBossBulletDirect1 = getRand(3);
        this.gBossBulletDirect2 = getRand(3);
        this.gBossBulletDirect3 = getRand(3);

        if (this.mMission == 1) {
            this.mCorrespondMission = playerType + 1;
        } else if (this.mMission == 2) {
            if (this.playerType == 0) {
                this.mCorrespondMission = 2;
            } else if (this.playerType == 1) {
                this.mCorrespondMission = 3;
            } else {
                this.mCorrespondMission = 1;
            }
        } else if (this.mMission == 3) {
            this.mCorrespondMission = 4;
        } else if (this.mMission == 4) {
            if (this.playerType == 0) {
                this.mCorrespondMission = 3;
            } else if (this.playerType == 1) {
                this.mCorrespondMission = 1;
            } else {
                this.mCorrespondMission = 2;
            }
        } else {
            this.mCorrespondMission = this.mMission;
        }

        if (this.mMission > 1) {
            this.gIsLoadGame = true;
            ResInit.loadStagePic(context, this.mCorrespondMission - 1);
        }
        ResInit.loadStagePic(context, this.mCorrespondMission);

        this.gBackgroundHeight = ResInit.BackgroundImage[this.mCorrespondMission - 1].getHeight();

        this.gBossMoveDownRange = 180;
        this.gBossMoveUpRange = 90;

        for (int i = 0; i < 5; i++) {
            EnemyPlane enemyPlane = new EnemyPlane();
            enemyPlane.setValue(0);
            this.gEnemy[i] = enemyPlane;
        }

        for (int jj = 0; jj < 100; jj++) {
            for (int j = 0; j < 6; j++) {
                this.mBullets[jj][j] = 0;
            }
        }

        for (int jj = 0; jj < 4; jj++) {
            for (int j = 0; j < 7; j++) {
                this.gMissile[jj][j] = 0;
            }
        }

        for (int jj = 0; jj < 30; jj++) {
            for (int j = 0; j < 4; j++) {
                this.gExplode[jj][j] = 0;
            }
        }

        for (int jj = 0; jj < 6; jj++) {
            for (int j = 0; j < 6; j++) {
                this.gItem[jj][j] = 0;
            }
        }
        for (int ii = 0; ii < 5; ii++) {
            this.gShoot_SL[ii] = false;
        }
        for (int ii = 0; ii < 5; ii++) {
            this.gShootNum_SL[ii] = 0;
        }
    }

    private void driveObjects() {
        gScreenMove++;
        makeEnemy();
        //makeBoss();
        dealEnemyState();

        //dealBossState();
        dealPlayerState();

        dealBullet();
        //dealItems();
        //dealBomb();
    }

    private void dealBullet() {
        for (int i = bullets.size() - 1; i >= 0; i--) {
            bullets.get(i).x += bullets.get(i).speedX;
            bullets.get(i).y += bullets.get(i).speedY;
            if (outScreen(bullets.get(i).x, bullets.get(i).y, bullets.get(i).sourceImg)) {
                bullets.remove(bullets.get(i));
            }
        }
    }

    private void dealEnemyState() {
        for (int i = enemys.size() - 1; i >= 0; i--) {
            if (enemys.get(i).health <= 0) {
            } else {
                enemys.get(i).dealMoveState();

                if (mallocBullet()) {//
                    makeEnemyBullet(enemys.get(i), mPlayer);
                }
                if (outScreen(enemys.get(i).x, enemys.get(i).y, enemys.get(i).sourceImg)) {
                    enemys.remove(enemys.get(i));
                }
            }
        }

    }

    public void makeEnemyBullet(EnemyPlane enemy, PlayerPlane player) {
        int delay = (int) (enemy.fireDelay * 1.0f / 25 * 1000);//按原来数据的25帧,换算成时间 ms
        if (System.currentTimeMillis() - enemy.shootTime <= delay) {
            return;
        }
        enemy.shootTime = System.currentTimeMillis();
        Log.d("TAG", "makeEnemyBullet: " + (System.currentTimeMillis() - enemy.createTime) + "    " + delay);
        int bulletNum = enemy.bulletMax;
        int rand = getRand(3);
        if (bulletNum < 3 || rand != 0) {
            if (this.getRand(8 - mMission - mGameDifficulty) > 0) {
                bulletNum = (1 + this.getRand(enemy.bulletMax));
            }
            int ranVal = this.getRand(4 - mGameDifficulty);
            Bullet bullet = makeEnemyBullet(enemy);
            if (ranVal == 0) {
                makeBulletToPlayer(enemy, bullet, player, bulletNum);
            } else {
                makeBulletToFront(player, bulletNum);
            }
        }
    }

    private void makeBulletToPlayer(EnemyPlane enemy, Bullet bullet, PlayerPlane player, int bulletNum) {
        this.bulletGoToPlayer(bullet, player);//初始化子弹xy轴速度
        if (bulletNum > 1) {
            int direction;
            //↑ ↓ ← → ↖ ↗ ↘ ↙ ↕
            if (bullet.speedX > 0) {
                if (bullet.speedY > 0) {
                    direction = 1;// →  ↓  ↘
                } else if (bullet.speedY < 0) {
                    direction = 3;// →  ↑  ↗
                } else {
                    direction = 2;// →     →
                }
            } else if (bullet.speedX < 0) {
                if (bullet.speedY > 0) {
                    direction = 7;// ←  ↓  ↙
                } else if (bullet.speedY < 0) {
                    direction = 5;// ←  ↑  ↖
                } else {
                    direction = 6;// ←     ←
                }
            } else if (bullet.speedY > 0) {
                direction = 0;//    ↓  ↓
            } else {
                direction = 4;//    ↑  ↑
            }

            if (bulletNum >= 2) {
                bullet.speedX = Bullet.bulletSpeedToPlayer[direction * 6];
                bullet.speedY = Bullet.bulletSpeedToPlayer[direction * 6 + 1];

                Bullet nb = makeEnemyBullet(enemy);
                nb.speedX = Bullet.bulletSpeedToPlayer[direction * 6 + 2];
                nb.speedY = Bullet.bulletSpeedToPlayer[direction * 6 + 3];
            }
            if (bulletNum >= 3) {
                if (direction == 0) {
                    direction = 8;
                }
                Bullet nb = makeEnemyBullet(enemy);
                nb.speedX = Bullet.bulletSpeedToPlayer[direction * 6 - 2];
                nb.speedY = Bullet.bulletSpeedToPlayer[direction * 6 - 1];
            }
            if (bulletNum >= 4) {
                if (direction == 8) {
                    direction = 0;
                }
                Bullet nb = makeEnemyBullet(enemy);
                nb.speedX = Bullet.bulletSpeedToPlayer[direction * 6 + 4];
                nb.speedY = Bullet.bulletSpeedToPlayer[direction * 6 + 5];
            }
            if (bulletNum >= 5) {
                if (direction == 0) {
                    direction = 8;
                }
                Bullet nb = makeEnemyBullet(enemy);
                nb.speedX = Bullet.bulletSpeedToPlayer[direction * 6 - 4];
                nb.speedY = Bullet.bulletSpeedToPlayer[direction * 6 - 3];
            }

            if (bulletNum >= 6) {
                Bullet nb = makeEnemyBullet(enemy);
                this.bulletGoToPlayer(nb, player);
            }
        }
    }

    private void bulletGoToPlayer(Bullet bullet, PlayerPlane player) {
        int x = bullet.x - player.x;
        int y = bullet.y - player.y;

        boolean flag_x = true;
        boolean flag_y = true;
        if (x < 0) {
            x = -x;
            flag_x = false;
        }

        if (y < 0) {
            y = -y;
            flag_y = false;
        }
        if (x > 10 || y > 10) {
            if (x > y) {
                // sy = 100  scale time
                // sx = 100
                // time = x / sy
                // sy = y / time;
                bullet.speedX = bullet.speedY;
                int scale = (x / Math.abs(bullet.speedY));
                if (scale <= 0)
                    scale = 1;
                bullet.speedY = (y / scale);
            } else {
                int scale = (y / Math.abs(bullet.speedY));
                if (scale <= 0)
                    scale = 1;
                bullet.speedX = (x / scale);
            }
            if (flag_x)
                bullet.speedX = -bullet.speedX;
            if (flag_y) {
                bullet.speedY = -bullet.speedY;
            }
        }
    }

    private Bullet makeEnemyBullet(EnemyPlane enemy) {
        int shootX = enemy.x + enemy.sourceImg.getWidth() / 2;
        int shootY = enemy.y + enemy.sourceImg.getHeight() - 5;
        int bulletTypeNum = GameCanvas.bulletPic[enemy.bulletType - 1];

        int picIndex = 0;
        for (int j = 0; j < enemy.bulletType; ++j) {//计算子弹图片的索引
            picIndex += GameCanvas.bulletPic[j];
        }

        Bitmap bitmap = ResInit.bulletImage[picIndex - 1];

        Bullet bullet = Bullet.mallocBullet(enemy.bulletType, shootX, shootY, 0, 5, bulletTypeNum, bitmap);
        bullets.add(bullet);
        return bullet;
    }

    private void makeBulletToFront(PlayerPlane player, int bulletNum) {

    }

    private void makeEnemy() {
        if (enemys.size() >= maxEnemy || !mIsEnableEnemy) {
            return;
        }
        int randomType;
        int type;
        int ranVal;
        if (this.mMission <= 4) {
            randomType = this.getRand(this.gApearEnemyType);
            type = stageEnemy[this.mCorrespondMission - 1][randomType] - 1;
            if (type < 6) {
                ranVal = this.getRand(5);
                if (ranVal < 3) {
                    type = (type * 3);
                } else if (ranVal == 3) {
                    type = (type * 3 + 1);
                } else {
                    type = (type * 3 + 2);
                }
            }
        } else {
            type = (18 + this.getRand(28));
        }
        enemys.add(EnemyPlane.mallocEnemy(mMission, type, mGameDifficulty));
        if ((this.gApearEnemyType < 15 && this.mMission != 3) || (this.gApearEnemyType < 10 && this.mMission == 3)) {
            this.gApearEnemyType = this.gApearEnemyType + 1;
        } else {
            //playSound(3);
            this.gIsBossAppear = true;
            //this.gIsEnableEnemy = false;
        }
    }

    private void dealPlayerState() {
        if (gIsGameOver) {
            return;
        }
        if (mIsPlayerDestroyed) {
            mIsPlayerDestroyed = false;
        } else {
            if (mPlayer.state == -1) {//进场
                mPlayer.y -= 5;
                if (mPlayer.y <= MainWindow.windowHeight - 300) {
                    mPlayer.state = 0;
                }
            } else {//正常状态
                mPlayer.handleMove();
            }
        }
    }

    private void makeEnemyInit(int id) {
        int type;
        if (this.mMission <= 4) {
            int ranValTemp = getRand(this.gApearEnemyType);
            type = (stageEnemy[this.mCorrespondMission - 1][ranValTemp] - 1);
            if (type < 6) {
                int ranVal = getRand(5);
                if (ranVal < 3) {
                    type = (type * 3);
                } else if (ranVal == 3) {
                    type = (type * 3 + 1);
                } else {
                    type = (type * 3 + 2);
                }
            }
        } else {
            type = (18 + getRand(28));
        }

        int wide = enemySize[((this.gEnemy[id]).type - 1) * 2];
        int high = enemySize[((this.gEnemy[id]).type - 1) * 2 + 1];
        if ((this.gEnemy[id]).x == 1) {
            if ((this.gEnemy[id]).y == -1) {
                (this.gEnemy[id]).y = (0 - high);
                (this.gEnemy[id]).x = getRand(MainWindow.windowWidth - wide);
                (this.gEnemy[id]).reward = ((this.gEnemy[id]).reward + getRand((this.gEnemy[id]).reward));
                makeGoods(id);
                return;
            }

            (this.gEnemy[id]).speedY = ((this.gEnemy[id]).speedY + getRand(2));
            (this.gEnemy[id]).speedX = ((this.gEnemy[id]).speedX + 2 - getRand(5));
            (this.gEnemy[id]).y = (0 - high);
            (this.gEnemy[id]).x = getRand(MainWindow.windowWidth - wide);
            if ((this.gEnemy[id]).pauseDelay > 0) {
                (this.gEnemy[id]).pauseDelay = getRand(900);
            }
            if (getRand(2) == 1 && (this.gEnemy[id]).aliveTime >= 0) {
                int ranVal = getRand(4);
                if (ranVal == 0) {
                    this.gEnemy[id].aliveTime = 0;
                    this.gEnemy[id].pauseDelay = 0;
                    this.gEnemy[id].offset = 0;
                } else if (ranVal == 1) {
                    (this.gEnemy[id]).aliveTime = 0;
                    (this.gEnemy[id]).pauseDelay = 0;
                    (this.gEnemy[id]).offset = (getRand(250) + 125);
                    if ((this.gEnemy[id]).x < 0 || (this.gEnemy[id]).x > MainWindow.windowWidth - wide) {
                        (this.gEnemy[id]).offset = 0;
                    }
                } else if (ranVal == 2) {
                    //if ((this.gEnemy[id]).aliveTime > 0) {
                    (this.gEnemy[id]).aliveTime = (getRand(320) + 240);
                    //}

                    (this.gEnemy[id]).pauseDelay = getRand(320);
                    (this.gEnemy[id]).offset = (getRand(250) + 125);
                    if ((this.gEnemy[id]).x < 0 || (this.gEnemy[id]).x > MainWindow.windowWidth - wide) {
                        (this.gEnemy[id]).offset = 0;
                    }
                } else {
                    //if ((this.gEnemy[id]).aliveTime > 0) {
                    (this.gEnemy[id]).aliveTime = -1;
//                    }
                    (this.gEnemy[id]).pauseDelay = getRand(320);
                    (this.gEnemy[id]).offset = getRand(250) + 125;
                    if ((this.gEnemy[id]).x < 0 || (this.gEnemy[id]).x > MainWindow.windowWidth - wide) {
                        (this.gEnemy[id]).offset = 0;
                    }
                }
            }
        } else {
            if ((this.gEnemy[id]).speedX == 0) {
                if (getRand(2) == 1) {
                    (this.gEnemy[id]).speedX = -2;
                } else {
                    (this.gEnemy[id]).speedX = 2;
                }
            }
            if ((this.gEnemy[id]).speedX > 0) {
                (this.gEnemy[id]).x = (0 - wide);
            } else {
                (this.gEnemy[id]).x = MainWindow.windowWidth;
            }

            (this.gEnemy[id]).y = getRand(140);
            if ((this.gEnemy[id]).speedY == 0) {
                int s = getRand(3);
                if (s == 1) {
                    (this.gEnemy[id]).speedY = 2;
                } else if (s == 2) {
                    (this.gEnemy[id]).speedY = -2;
                }
            }
            int ranVal = getRand(2);
            if ((this.gEnemy[id]).offset > 300 && ranVal == 1) {
                (this.gEnemy[id]).offset = 300;
            }
            if ((this.gEnemy[id]).offset > 300) {
                int ranValTemp = ((this.gEnemy[id]).offset - 100) * Math.abs((this.gEnemy[id]).speedY);
                if ((this.gEnemy[id]).y < ranValTemp) {
                    (this.gEnemy[id]).y = (ranValTemp + getRand(150));
                }
            } else if ((this.gEnemy[id]).speedY < 0) {
                int ranValTemp = (120 / Math.abs((this.gEnemy[id]).speedX) * Math.abs((this.gEnemy[id]).speedY));
                if ((this.gEnemy[id]).y < ranValTemp) {
                    (this.gEnemy[id]).y = (ranValTemp + getRand(150));
                }
            } else if ((this.gEnemy[id]).speedY > 0) {
                (this.gEnemy[id]).y = getRand(360);
            }
        }
        (this.gEnemy[id]).reward = ((this.gEnemy[id]).reward + getRand((this.gEnemy[id]).reward));
        makeGoods(id);

    }

    private void bulletGoToPlayer(int i) {
        int x = (this.mBullets[i][2] - this.mPlayer.x + (playerSize[this.playerType * 2] >> 1));
        int y = (this.mBullets[i][3] - this.mPlayer.y + (playerSize[this.playerType * 2 + 1] >> 1));

        boolean flag_x = true;
        boolean flag_y = true;
        if (x < 0) {
            x = -x;
            flag_x = false;
        }
        if (y < 0) {
            y = -y;
            flag_y = false;
        }
        if (x > 10 || y > 10) {
            if (x > y) {
                // sy = 100  scale time
                // sx = 100
                // time = x / sy
                // sy = y / time;
                this.mBullets[i][4] = this.mBullets[i][5];
                int scale = (x / Math.abs(this.mBullets[i][5]));
                if (scale <= 0)
                    scale = 1;
                this.mBullets[i][5] = (y / scale);
            } else {
                int scale = (y / Math.abs(this.mBullets[i][5]));
                if (scale <= 0)
                    scale = 1;
                this.mBullets[i][4] = (x / scale);
            }
            if (flag_x)
                this.mBullets[i][4] = -this.mBullets[i][4];
            if (flag_y) {
                this.mBullets[i][5] = -this.mBullets[i][5];
            }
        }
    }

    private int mallocBlastPT() {
        if (this.gExplodeCount >= 30)
            return -1;
        for (int i = 0; i < 30; i = i + 1) {
            if (this.gExplode[i][ex_type] == 0) {
                this.gExplodeCount = this.gExplodeCount + 1;
                return i;
            }
        }
        return -1;
    }

    private boolean mallocBullet() {
        if (bullets.size() >= 100)
            return false;
        return true;
    }

    private void makeEnemyBulletInit(int i, int type, int x, int y, int xspd, int yspd) {
        this.mBullets[i][0] = type;
        this.mBullets[i][2] = x;
        this.mBullets[i][3] = y;
        this.mBullets[i][4] = xspd;
        this.mBullets[i][5] = yspd;
        this.mBullets[i][1] = getRand(bulletPic[this.mBullets[i][0] - 1]);
    }

    /**
     * 生成奖励
     *
     * @param i
     */
    private void makeGoods(int i) {
        if ((this.gEnemy[i]).type <= 18) {
            if (getRand(10) == 0) {
                (this.gEnemy[i]).item = 5;
            } else if (this.mPlayer.power == 1 && getRand(10) == 1) {
                (this.gEnemy[i]).item = 7;
            }
        } else {
            if (this.mPlayer.power < 3 && getRand(4) == 0) {
                (this.gEnemy[i]).item = 7;
                return;
            }
            if (getRand(6) == 0) {
                (this.gEnemy[i]).item = gGetItemsList[getRand(20)];
                if (12 == (this.gEnemy[i]).item) {
                    (this.gEnemy[i]).item = (this.gEnemy[i]).item + this.playerType;
                }
            }
        }

    }

    private boolean checkIfHit(int sx, int sy, int sw, int sh, int dx, int dy, int dw, int dh) {
        int mw = 0, mh = 0;
        sw = (sw - sw / 6);
        sh = (sh - sh / 6);
        dw = (dw - dw / 6);
        dh = (dh - dh / 6);
        if (dw > sw) {
            dx = (dx + dw / 10);
        } else {
            sx = (sx + sw / 10);
        }
        int cx = (sx - dx);
        int cy = (sy - dy);
        if (cx == 0 && cy == 0)
            return true;
        if (cx >= 0 && cy >= 0) {
            mw = dw;
            mh = dh;
        } else if (cx >= 0 && cy <= 0) {
            mw = dw;
            mh = sh;
        } else if (cx <= 0 && cy >= 0) {
            mw = sw;
            mh = dh;
        } else if (cx <= 0 && cy <= 0) {
            mw = sw;
            mh = sh;
        }

        if (Math.abs(cx) < mw && Math.abs(cy) < mh)
            return true;
        return false;
    }

    /**
     * 随机分配一个敌人对象
     *
     * @return
     */
    private int mallocEnemy() {
        if (this.gEnemyCount >= 5)
            return -1;

        for (int i = 0; i < 5; i++) {
            if ((this.gEnemy[i]).type == 0) {
                this.gEnemyCount = this.gEnemyCount + 1;
                return i;
            }
        }
        return -1;
    }

    private int getRand(int i) {
        if (i == 0) {
            return 0;
        }
        int r = this.random.nextInt();
        r = (r >> 24) + (r >> 16) + (r >> 8) + r & 0xFF;
        return Math.abs(r % i);
    }

    private void drawBitmapXY(Canvas canvas, Bitmap bitmap, int x, int y) {
        canvas.drawBitmap(bitmap, x, y, mPaint);
    }

    private void drawBitmapCenter(Canvas canvas, Bitmap bitmap) {
        int posX = (MainWindow.windowWidth - bitmap.getWidth()) / 2;
        int posY = (MainWindow.windowHeight - bitmap.getHeight()) / 2;
        canvas.drawBitmap(bitmap, posX, posY, mPaint);
    }

    private void drawBitMapLT(Canvas canvas, Bitmap bitmap) {
        int posX = (MainWindow.windowWidth - bitmap.getWidth()) / 2;
        int posY = (MainWindow.windowHeight - bitmap.getHeight()) / 2;
        canvas.drawBitmap(bitmap, posX, posY, mPaint);
    }

    private void drawBitmapCenterHorizontal(Canvas canvas, Bitmap bitmapL, Bitmap bitmapR, int offset) {
        int lWidth = bitmapL.getWidth();
        int lHeight = bitmapL.getHeight();
        int rWidth = bitmapR.getWidth();
        int rHeight = bitmapR.getHeight();
        int totalWidth = lWidth + rWidth + offset;

        int leftX = (MainWindow.windowWidth - totalWidth) / 2;
        int leftY = (MainWindow.windowHeight - lHeight) / 2;
        int rightX = MainWindow.windowWidth - leftX - rWidth;
        int rightY = (MainWindow.windowHeight - rHeight) / 2;
        canvas.drawBitmap(bitmapL, leftX, leftY, mPaint);
        canvas.drawBitmap(bitmapR, rightX, rightY, mPaint);
    }

    private void drawBitmapCenterVertical(Canvas canvas, Bitmap bitmapL, Bitmap bitmapR, int offset) {
        int lWidth = bitmapL.getWidth();
        int lHeight = bitmapL.getHeight();
        int rWidth = bitmapR.getWidth();
        int rHeight = bitmapR.getHeight();
        int totalHeight = lHeight + rHeight + offset;

        int leftX = (MainWindow.windowWidth - lWidth) / 2;
        int leftY = (MainWindow.windowHeight - totalHeight) / 2;
        int rightX = (MainWindow.windowWidth - rWidth) / 2;
        int rightY = MainWindow.windowHeight - leftY - rHeight;
        canvas.drawBitmap(bitmapL, leftX, leftY, mPaint);
        canvas.drawBitmap(bitmapR, rightX, rightY, mPaint);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        isThreadAlive = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
    }

    @Override
    public void run() {
        while (this.isThreadAlive) {
            Canvas lockCanvas = null;
            try {
                lockCanvas = surfaceHolder.lockCanvas();
                //清空画布
                lockCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
                if (this.gGamePause == 0) {
                    driveObjects();
                } else {//暂停状态
                    drawBitmapCenter(lockCanvas, ResInit.pauseImage[this.gGamePause - 1]);
                }
                if (this.gIsLoadGame) {
                    drawLoading(lockCanvas);
                } else {
                    drawObjects(lockCanvas);
                    surfaceHolder.unlockCanvasAndPost(lockCanvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void drawObjects(Canvas canvas) throws Exception {
        showBackScreen(canvas);
        //showBoss(canvas);
        showEnemy(canvas);
        //showBomb(canvas);
        showPlayer(canvas);
        //showItems(canvas);
        showBullet(canvas);
        //showExplode(canvas);
        showActiveLife(canvas);
        showActiveBomb(canvas);
        showActiveScore(canvas);
        showMissionInfo(canvas);
        showGameOver(canvas);
    }

    private void showBullet(Canvas canvas) {
        for (Bullet bullet : bullets) {
            if (bullet.type >= 21) {
                drawBitmapXY(canvas, ResInit.bulletImage[59], bullet.x, bullet.y);
            } else {
                drawBitmapXY(canvas, bullet.getImg(), bullet.x, bullet.y);
            }
        }
    }

    private int getMissilePicIndex(int i) {
        int misBulletDirection = 0;
        int xspd = this.gMissile[i][4];
        int yspd = this.gMissile[i][5];

        for (int temp = 0; temp < 16; ++temp) {
            if (xspd == bulletSpeedMissile[temp * 2] && yspd == bulletSpeedMissile[temp * 2 + 1]) {
                misBulletDirection = temp;
                break;
            }
        }
        return misBulletDirection;
    }

    private void showEnemy(Canvas canvas) {
        for (EnemyPlane enemy : enemys) {
            drawBitmapXY(canvas, enemy.sourceImg, enemy.x, enemy.y);
        }
    }

    private void showGameOver(Canvas canvas) {
        if (this.gIsGameFinished) {
            this.isThreadAlive = false;
            ((MainWindow) (getParent())).showRanking();
        }
    }

    private void showMissionInfo(Canvas canvas) {
        if (this.gScreenMove < 310 && this.gScreenMove >= 45) {
            if (this.gScreenMove < 250) {
                if (this.mMission == 5) {
                    drawBitmapCenter(canvas, ResInit.otherImage[10]);
                } else {
                    drawBitmapCenterHorizontal(canvas, ResInit.otherImage[9], ResInit.numberImageValue[0][this.mCorrespondMission], 100);
                }
            } else {
                this.mIsEnableEnemy = true;
            }
        }

        if (this.gIsMissionComplete) {
            this.gTempDelay = (this.gTempDelay + 1);
            if (this.gTempDelay > 90 && !this.gIsGameFinished) {
                if (this.mMission >= 5) {
                    drawBitmapCenterVertical(canvas, ResInit.otherImage[5], ResInit.otherImage[3], 50);
                } else {
                    drawBitmapCenterVertical(canvas, ResInit.otherImage[9], ResInit.otherImage[3], 50);
                }
            }
        }

        //按60帧算的话这里应该是6秒左右
        if (this.gTempDelay > 400) {
            if (this.mMission >= 5) {
                drawBitmapCenter(canvas, ResInit.otherImage[0]);
                if (this.gTempDelay > 800) {
                    this.gIsGameFinished = true;
                }
                return;
            }

            this.mMission = (this.mMission + 1);
            this.gScreenMove = 0;
            this.gTempDelay = 0;
            this.gIsMissionComplete = false;
            stageInit();
        }
    }

    private void showActiveScore(Canvas canvas) {
        int textSize = UiUtils.dp2px(context, 20);
        mPaint.setTextSize(textSize);
        mPaint.setColor(Color.BLUE);
        float textWidth = mPaint.measureText(int2string(this.gGameScore));
        canvas.drawText(int2string(this.gGameScore), MainWindow.windowWidth - textWidth, textSize + 30, mPaint);
    }

    private void showActiveBomb(Canvas canvas) {
        if (this.gIsGameOver) {
            return;
        }
        for (int i = 0; i < mPlayer.bomb; i = (i + 1)) {
            int x = (27 + i * 45);
            drawBitmapXY(canvas, ResInit.bombIcon[mPlayer.bombTypes[i]], x, 120);
        }
    }

    private void showActiveLife(Canvas canvas) {
        for (int i = 0; i < this.mPlayer.life - 1; i = (i + 1)) {
            int x = (27 + i * 45);
            drawBitmapXY(canvas, ResInit.lifeIcon[this.playerType], x, 50);
        }
    }

    private void showPlayer(Canvas canvas) {
        if (this.gIsGameOver) {
            return;
        }
        if (mPlayer.state == -1 && (this.gScreenMove & 3) != 0) {
            drawBitmapXY(canvas, mPlayer.stateImg[5], mPlayer.x, mPlayer.y);
        } else {
            drawBitmapXY(canvas, mPlayer.stateImg[0], mPlayer.x, mPlayer.y);
        }

        if (this.gIsPlayerFire && (this.gScreenMove & 5) != 0) {
            gIsPlayerFire = false;
            drawBitmapXY(canvas, mPlayer.stateImg[4], mPlayer.x, mPlayer.y);
        }
    }

    private void showBackScreen(Canvas canvas) {
        this.gSrcRect.x = 0;
        this.gSrcRect.w = MainWindow.windowWidth;
        int Rect_y = -(this.gBackgroundHeight - MainWindow.windowHeight - this.gBackgroundOffset);
        if (Rect_y >= 0) {
            drawBitmapXY(canvas, ResInit.BackgroundImage[this.mCorrespondMission - 1], 0, -(this.gBackgroundHeight - Rect_y));
        }
        drawBitmapXY(canvas, ResInit.BackgroundImage[this.mCorrespondMission - 1], 0, Rect_y);

        if (Rect_y >= MainWindow.windowHeight) {
            this.gBackgroundOffset = 0;
        }
        if (this.gIsBossAppear) {
            this.gBackgroundOffset = (this.gBackgroundOffset + 5);
        } else {
            this.gBackgroundOffset = (this.gBackgroundOffset + 3);
        }
    }

    private void drawLoading(Canvas lockCanvas) throws Exception {
        drawBitMapLT(lockCanvas, ResInit.otherImage[1]);
        drawBitmapCenter(lockCanvas, ResInit.otherImage[2]);
        surfaceHolder.unlockCanvasAndPost(lockCanvas);
        Thread.sleep(1000);
        this.gIsLoadGame = false;
    }

    private String int2string(int value) {
        String str = String.valueOf(value);
        int len = str.length();
        if (len < 7)
            for (int i = 0; i < 7 - len; i++) {
                str = "0" + str;
            }
        return str;
    }

    @Override
    public void updatePosition(double xPercent, double yPercent) {
        if (mPlayer == null || gIsMissionComplete) {
            return;
        }
        mPlayer.handleSpeed(xPercent, yPercent);
    }


    public boolean inScreen(int posX, int posY, Bitmap source) {
        if ((posX >= 0 && posX <= MainWindow.windowWidth - source.getWidth()) && posY >= 0 && posY <= MainWindow.windowHeight - source.getHeight()) {
            return true;
        }
        return false;
    }

    public boolean outScreen(int posX, int posY, Bitmap source) {
        if (posX < -source.getWidth() || posX > MainWindow.windowWidth) {
            return true;
        }
        if (posY < -source.getHeight() || posY > MainWindow.windowHeight) {
            return true;
        }
        return false;
    }


}
