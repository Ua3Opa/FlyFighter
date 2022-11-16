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

import com.flyfighter.entity.EnemyPlane;
import com.flyfighter.entity.PlayerPlane;
import com.flyfighter.entity.RectArea;
import com.flyfighter.res.RMS;
import com.flyfighter.res.ResInit;
import com.flyfighter.res.SoundHelper;
import com.flyfighter.utils.UiUtils;
import com.flyfighter.view.MainWindow;

import java.util.Random;

public class GameCanvas extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private Context context;
    private MediaPlayer[] mediaPlayers = new MediaPlayer[13];

    private int gSelectedPlayer;
    private int difficulty;
    private PlayerPlane gPlayer;
    private RectArea gSrcRect;
    private Random random;

    private boolean isThreadAlive = false;
    private boolean gIsLoadGame = true;
    private boolean gIsSaved;
    private int gContinueNum;
    private boolean gIsGameFinished;
    private boolean gIsPlayerFire;
    private int gMission;
    private int gGameScore;
    private int gMissileType;
    private int gCurrentMissileMax;
    private int gOtherTypeBomb;
    private int gGameSecretNum;
    private int gGamePause;
    private boolean gIsMissionComplete;
    private int gTempDelay;
    private int gBombDelay;
    private boolean gIsEnableEnemy;
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
    private boolean gIsPlayerDestroyed;
    private int gBossBulletDirect1;
    private int gBossBulletDirect2;
    private int gBossBulletDirect3;
    private int gCorrespondMission;
    private int gBossMoveDownRange;
    private int gBossMoveUpRange;
    private int gGameDifficulty;
    private int gBossMoveAction;
    private int gBackgroundHeight;

    private static int it_type = 0;
    private static int it_x = 1;
    private static int it_y = 2;
    private static int it_picid = 3;
    private static int it_speedx = 4;
    private static int it_speedy = 5;

    private static final int[] backGroundHeight = new int[5];

    public static final int[][] stageEnemy = new int[][]{
            {1, 2, 4, 38, 44, 19, 39, 22, 45, 26, 46, 25, 43, 36, 40},
            {5, 2, 4, 24, 29, 27, 46, 21, 23, 39, 45, 30, 28, 33, 35},
            {6, 4, 3, 43, 20, 39, 23, 31, 45, 34, 38, 46, 37, 32, 41},
            {19, 45, 20, 46, 19, 45, 20, 46, 19, 45, 20, 46, 19, 45, 20},
            {46, 44, 43, 22, 20, 39, 23, 31, 45, 34, 38, 46, 37, 32, 41}
    };

    public static final int[][] enemyData = new int[][]{{1,4,1,0,0,25,0,12,0,0,0,0,156,38,2,4,3},
            {2,2,0,0,-15,0,500,15,0,0,0,0,168,41,2,4,3},
            {3,2,0,0,15,0,500,12,0,0,0,0,158,40,2,4,3},
            {4,4,1,0,0,25,0,20,0,0,0,0,166,38,2,5,3},
            {5,2,0,0,-10,0,500,25,0,0,0,0,168,35,2,5,3},
            {6,2,0,0,10,0,500,20,0,0,0,0,156,35,2,5,3},
            {7,3,1,0,0,25,0,28,1,1,0,0,146,41,2,6,4},
            {8,1,0,0,-15,0,500,22,0,0,0,0,138,36,2,6,4},
            {9,1,0,0,15,0,500,22,0,0,0,0,156,37,2,6,4},
            {10,4,1,0,0,25,0,25,1,1,0,0,166,35,2,7,4},
            {11,2,0,0,-10,0,500,33,0,0,0,0,168,37,2,7,4},
            {12,2,0,0,10,0,500,25,0,0,0,0,158,39,2,7,4},
            {13,2,1,0,0,25,0,20,1,1,0,0,148,40,2,8,3},
            {14,2,0,0,-15,0,500,15,0,0,0,0,150,41,2,8,3},
            {15,2,0,0,15,0,500,15,0,0,0,0,136,37,2,8,3},
            {16,3,1,0,0,25,0,26,0,0,0,0,158,36,2,6,3},
            {17,1,0,0,-15,0,500,25,0,0,0,0,166,35,2,6,4},
            {18,1,0,0,15,0,500,25,0,0,0,0,168,42,2,6,4},
            {19,3,1,-1,-10,25,0,22,0,0,0,0,198,40,3,14,5},
            {20,3,1,-1,10,25,0,18,0,0,0,0,188,42,3,14,5},
            {21,3,1,0,0,15,0,100,1,1,0,0,282,45,3,15,4}
            ,{22,1,0,0,0,0,700,150,0,0,0,0,382,55,4,16,7},
            {23,1,1,0,0,10,225,160,-1,1,0,0,383,52,4,17,8},
            {24,2,1,0,0,15,0,120,1,1,0,0,369,54,3,11,6},
            {25,4,0,0,0,0,750,200,0,0,0,0,595,56,4,10,7},
            {26,2,1,0,0,15,0,200,1,1,0,0,478,52,3,14,4},
            {27,4,0,0,0,0,700,220,0,0,0,0,498,58,4,17,7},
            {28,4,1,0,0,15,0,180,1,1,0,0,462,52,4,16,8},
            {29,4,1,0,0,15,0,80,1,1,0,0,290,46,3,6,5},
            {30,2,1,0,0,15,0,200,1,1,0,0,488,48,4,12,7},
            {31,3,1,0,0,15,0,260,1,1,0,0,580,62,5,13,8},
            {32,1,1,0,0,10,250,250,-1,1,0,0,552,52,5,9,7},
            {33,3,1,0,0,15,0,250,1,1,0,0,593,54,5,16,8},
            {34,1,1,0,0,10,240,300,-1,1,0,0,758,56,5,17,7},
            {35,4,0,0,0,0,650,200,0,0,0,0,486,48,4,11,8},
            {36,4,1,0,0,10,220,160,-1,1,0,0,396,52,5,10,7},
            {37,3,1,0,0,15,0,280,1,1,0,0,786,61,5,16,8},
            {38,4,1,0,0,15,0,80,1,1,0,0,286,44,4,7,7},
            {39,4,1,0,0,15,0,80,1,1,0,0,298,46,4,15,7},
            {40,3,1,0,0,15,0,200,1,1,0,0,656,43,5,17,8},
            {41,4,1,0,0,15,0,200,1,1,0,0,631,53,5,12,7},
            {42,4,1,0,0,15,0,150,1,1,0,0,428,61,5,13,8},
            {43,4,1,0,0,10,0,120,1,1,0,0,366,46,4,7,5},
            {44,3,1,0,0,10,0,100,1,1,0,0,356,37,3,6,6},
            {45,4,1,0,0,30,0,15,0,0,0,0,388,42,3,11,5},
            {46,3,1,0,0,30,0,15,0,0,0,0,366,41,3,15,6},
            {201,1,50,0,10,10,300,5000,0,170,0,0,6688,40,0,0,8},
            {202,1,50,0,10,10,300,5000,0,170,0,0,6686,40,0,0,8},
            {203,1,50,0,10,10,300,5000,0,170,0,0,6886,40,0,0,8},
            {204,1,50,0,10,10,300,7000,0,170,0,0,8888,40,0,0,8},
            {205,1,50,0,10,10,300,9000,0,170,0,0,9999,40,0,0,8}};


    private static final int[] enemySize = new int[]{28, 23, 24, 28, 24, 28, 30, 26, 26, 30, 26, 30, 38, 28, 28, 38, 28, 38,
            42, 28, 28, 42, 28, 42, 34, 21, 22, 34, 22, 34, 32, 27, 34, 36, 34, 36, 38, 44, 38, 44, 74, 31, 58, 40, 58,
            40, 56, 31, 94, 29, 74, 36, 90, 35, 82, 32, 54, 39, 94, 28, 84, 32, 62, 42, 62, 42, 70, 36, 42, 59, 42, 59,
            74, 34, 62, 32, 56, 31, 84, 35, 70, 40, 68, 45, 54, 28, 46, 24, 38, 30, 38, 53, 128, 64, 112, 80, 206, 60,
            152, 62, 170, 53};

    private static final int[] gGetItemsList = new int[]
            {5, 5, 5, 6, 6, 6, 7, 7, 8, 1, 2, 3, 4, 12, 9, 9, 10, 10, 11, 11};

    public static final int[] bulletPic = new int[]{2, 3, 3, 1, 4, 2, 2, 2, 4, 4, 4, 4, 4, 4, 4, 2, 2, 2, 2, 2, 2};
    public static final int[][] playerData = new int[][]{
            {1, 5, 3, 3, 1}, {2, 4, 3, 3, 1}, {3, 6, 3, 3, 1}};

    private static final int[] explodePic = new int[]{4, 4, 6, 5, 6, 6, 6, 6};
    private static final int[] explodeSize = new int[]{14, 12, 14, 12, 40, 40, 50, 40, 42, 32, 42, 32, 42, 42, 42, 42};
    private static final int[] enemyPic = new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 12, 6, 3, 3, 3, 3, 3};

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

    private static final int[] bulletSpeedSL = new int[]{-2, 1, -2, 2, -1, 2, -1, 3, 3, 1, 3, 1, 2, 2, 2, 2, 1};

    private static final int[] bulletSpeedToPlayer = new int[]{0, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 1, 2, 2, -1, 2, -1, 2, -2,
            1, -2, 1, -2, -3, -1, -3, -1, -1, -2, -1, -2, -1, -2, -1, -2, -3, 1, -2, 1, -1, 1, -1, 1, -1, 3};

    private static final int[] enemyBulletToFront = new int[]{0, 4, -1, 4, 1, 4, -1, 4, 4, 1, 4, -1, 4, 4, 1, 4,
            -2, 3, -1, 4, 4, 1, 4, 2, 3, -2, 3, -1, 4, 4, 1, 4, 2, 3};

    private static final int[] playerSize = new int[]{26, 32, 30, 28, 30, 28};

    private int[] gBombType = new int[6];
    private EnemyPlane[] gEnemy = new EnemyPlane[5];

    private int[][] gBullet = new int[100][6];
    private static final byte bu_type = 0;
    private static final byte bu_picid = 1;
    private static final byte bu_x = 2;
    private static final byte bu_y = 3;
    private static final byte bu_speedx = 4;
    private static final byte bu_speedy = 5;

    private int[][] gMissile = new int[4][7];
    private static final int mi_type = 0;
    private static final int mi_aimid = 1;
    private static final int mi_x = 2;
    private static final int mi_y = 3;
    private static final int mi_speedx = 4;
    private static final int mi_speedy = 5;
    private static final int mi_power = 6;

    private int[][] gExplode = new int[30][4];
    private static int ex_type = 0;
    private static int ex_x = 1;
    private static int ex_y = 2;
    private static int ex_picid = 3;

    private int[][] gItem = new int[6][6];

    private boolean[] gShoot_SL = new boolean[5];
    private int[] gShootNum_SL = new int[5];

    private Paint mPaint = new Paint();
    private final SurfaceHolder surfaceHolder;

    public GameCanvas(Context context, int gSelectedPlayer) {
        super(context, null);
        this.context = context;

        mPaint.setAntiAlias(true);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        this.gSelectedPlayer = gSelectedPlayer;
        this.difficulty = RMS.difficulty;

        this.gPlayer = new PlayerPlane();
        this.gSrcRect = new RectArea();
        this.random = new Random();
        gameInit();
        stageInit();
    }

    private void gameInit() {
        ResInit.loadPlayer(context, gSelectedPlayer);
        SoundHelper.loadGameSounds(context, mediaPlayers);
        if (RMS.loadSound) {
            playSound(12);
        }
        int[] data = playerData[gSelectedPlayer];
        int width = ResInit.playerImage[gSelectedPlayer].getWidth();
        int height = ResInit.playerImage[gSelectedPlayer].getHeight();
        this.gPlayer.setValue(data[0], (MainWindow.windowWidth - width) / 2, MainWindow.windowHeight + height, data[1], data[2], data[3], data[4], 0);
        this.gPlayer.setLife(6);
        this.gPlayer.setBomb(6);
        for (int i = 0; i < 6; i++) {
            this.gBombType[i] = this.gPlayer.type;
        }

        this.gIsSaved = false;
        this.gContinueNum = 3;
        this.gIsGameFinished = false;
        this.gIsPlayerFire = false;
        this.gMission = 1;
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
        this.gIsEnableEnemy = false;
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
        this.gIsPlayerDestroyed = false;

        this.gBossBulletDirect1 = getRand(3);
        this.gBossBulletDirect2 = getRand(3);
        this.gBossBulletDirect3 = getRand(3);

        if (this.gMission == 1) {
            this.gCorrespondMission = gSelectedPlayer + 1;
        } else if (this.gMission == 2) {
            if (this.gSelectedPlayer == 0) {
                this.gCorrespondMission = 2;
            } else if (this.gSelectedPlayer == 1) {
                this.gCorrespondMission = 3;
            } else {
                this.gCorrespondMission = 1;
            }
        } else if (this.gMission == 3) {
            this.gCorrespondMission = 4;
        } else if (this.gMission == 4) {
            if (this.gSelectedPlayer == 0) {
                this.gCorrespondMission = 3;
            } else if (this.gSelectedPlayer == 1) {
                this.gCorrespondMission = 1;
            } else {
                this.gCorrespondMission = 2;
            }
        } else {
            this.gCorrespondMission = this.gMission;
        }

        if (this.gMission > 1) {
            this.gIsLoadGame = true;
            ResInit.loadStagePic(context, this.gCorrespondMission - 1);
        }
        ResInit.loadStagePic(context, this.gCorrespondMission);

        this.gBackgroundHeight = ResInit.BackgroundImage[this.gCorrespondMission - 1].getHeight();

        this.gBossMoveDownRange = 180;
        this.gBossMoveUpRange = 90;

        for (int i = 0; i < 5; i++) {
            EnemyPlane enemyPlane = new EnemyPlane();
            enemyPlane.setValue(0);
            this.gEnemy[i] = enemyPlane;
        }

        for (int jj = 0; jj < 100; jj++) {
            for (int j = 0; j < 6; j++) {
                this.gBullet[jj][j] = 0;
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
        //默认位置在屏幕外
        this.gPlayer.x = (MainWindow.windowWidth - ResInit.playerStateImage[5].getWidth()) / 2;
        this.gPlayer.y = MainWindow.windowHeight + ResInit.playerStateImage[5].getHeight();
    }

    private void driveObjects() {
        makeEnemy();
        //makeBoss();
        dealEnemyState();

        //dealBossState();
        //dealPlayerState();

        //dealBullet();
        //dealItems();
        //dealBomb();
    }

    private void dealEnemyState() {
        int type = 0;
        for (int i = 0; i < 5; i++) {
            type = (this.gEnemy[i]).type;
            if (type == 0 || type > 200) {
                continue;
            }
            int x = (this.gEnemy[i]).x;
            int y = (this.gEnemy[i]).y;
            int wide = enemySize[(type - 1) * 2];
            int high = enemySize[(type - 1) * 2 + 1];

            if ((this.gEnemy[i]).health <= 0) {
                int idx = mallocBlastPT();
                if (idx >= 0) {
                    this.gExplode[idx][ex_type] = (this.gEnemy[i]).explode;
                    this.gExplode[idx][ex_x] = (x + (wide >> 1) - (explodeSize[(this.gExplode[idx][ex_type] - 1) * 2] >> 1));
                    this.gExplode[idx][ex_y] = (y + (high >> 1) - (explodeSize[(this.gExplode[idx][ex_type] - 1) * 2 + 1] >> 1));
                    this.gExplode[idx][ex_picid] = 0;
                }
                this.gGameScore += (this.gEnemy[i]).reward;
                if (5 == this.gGameSecretNum) {
                    this.gGameScore += (this.gEnemy[i]).reward;
                }
                createItems(i);
                destroyEnemy(i);
                continue;
            }

            if ((this.gEnemy[i]).offset < 100) {
                if ((this.gEnemy[i]).aliveTime == 0) {
                    y = (y + (this.gEnemy[i]).speedY);
                } else if (y < (this.gEnemy[i]).pauseDelay) {
                    y = (y + (this.gEnemy[i]).speedY);
                } else if ((this.gEnemy[i]).aliveTime > 0) {
                    (this.gEnemy[i]).aliveTime = ((this.gEnemy[i]).aliveTime - 1);
                }
                x = (x + (this.gEnemy[i]).speedX);
                if ((this.gEnemy[i]).offset > 0 && this.gScreenMove % (this.gEnemy[i]).offset == 0) {
                    (this.gEnemy[i]).speedX = -(this.gEnemy[i]).speedX;
                }
                if (y < MainWindow.windowWidth / 3 && (type < 19 || type > 20)
                        && ((x < 0 - (wide >> 1) && (this.gEnemy[i]).speedX < 0) || (x > MainWindow.windowWidth - (wide >> 1) && (this.gEnemy[i]).speedX > 0))) {
                    (this.gEnemy[i]).speedX = -(this.gEnemy[i]).speedX;
                }
                if (x + (this.gEnemy[i]).speedX <= -wide || x + (this.gEnemy[i]).speedX >= MainWindow.windowWidth ||
                        y + (this.gEnemy[i]).speedY <= -high || y + (this.gEnemy[i]).speedY >= MainWindow.windowHeight) {
                    destroyEnemy(i);
                    continue;
                }
            } else {
                int enemyOffset = ((this.gEnemy[i]).offset - 100);
                x = (x + (this.gEnemy[i]).speedX);
                y = (y + (this.gEnemy[i]).speedY);
                if (enemyOffset != 0 &&
                        this.gScreenMove % enemyOffset == 0) {
                    (this.gEnemy[i]).speedY = -(this.gEnemy[i]).speedY;
                }
                if (x + (this.gEnemy[i]).speedX <= -wide || x + (this.gEnemy[i]).speedX >= MainWindow.windowWidth ||
                        y + (this.gEnemy[i]).speedY <= -high || y + (this.gEnemy[i]).speedY >= MainWindow.windowHeight) {
                    destroyEnemy(i);
                    continue;
                }
            }
            if ((this.gEnemy[i]).bulletType > 0) {
                makeEnemyBullet(i);
            }
            (this.gEnemy[i]).x = x;
            (this.gEnemy[i]).y = y;
            if ((this.gEnemy[i]).picId != enemyPic[type - 1] - 1) {
                (this.gEnemy[i]).picId = ((this.gEnemy[i]).picId + 1);
                if ((this.gEnemy[i]).type < 45 && (this.gEnemy[i]).picId > enemyPic[type - 1] - 2) {
                    (this.gEnemy[i]).picId = 0;
                }
            }
            if (this.gPlayerInit == 0 && checkIfHit(x, y, wide, high, this.gPlayer.x, this.gPlayer.y, playerSize[this.gSelectedPlayer * 2], playerSize[this.gSelectedPlayer * 2 + 1])) {
                (this.gEnemy[i]).health = ((this.gEnemy[i]).health - 20);
                this.gGameScore += 168;
                if (6 != this.gGameSecretNum) {
                    if (!this.gIsBombing) {
                        this.gIsPlayerDestroyed = true;
                    }
                }
            }
        }
    }

    private void destroyEnemy(int i) {
        this.gEnemy[i].setValue(0);
        this.gEnemyCount = (this.gEnemyCount - 1);
    }

    private void createItems(int i) {
        if ((this.gEnemy[i]).item != 0) {
            short idx = mallocItems();

            if (idx >= 0) {
                this.gItem[idx][it_type] = (this.gEnemy[i]).item;
                this.gItem[idx][it_x] = ((this.gEnemy[i]).x + (enemySize[((this.gEnemy[i]).type - 1) * 2] >> 1) - 15);
                this.gItem[idx][it_y] = ((this.gEnemy[i]).y + (enemySize[((this.gEnemy[i]).type - 1) * 2 + 1] >> 1));
                this.gItem[idx][it_speedx] = (2 + getRand(3));
                this.gItem[idx][it_speedy] = 1;
                this.gItem[idx][it_picid] = 0;
            }
        }
    }

    private short mallocItems() {
        if (this.gItemCount >= 6)
            return -1;
        for (short i = 0; i < 6; i++) {
            if (this.gItem[i][it_type] == 0) {
                this.gItemCount = (this.gItemCount + 1);
                return i;
            }
        }
        return -1;
    }
    //@TODO 有异常先注释掉
    private void makeEnemyBullet(int i) {
//        int[] shootpoint = new int[2];
//        int bulletNum = (this.gEnemy[i]).bulletMax;
//        int wide = enemySize[((this.gEnemy[i]).type - 1) * 2];
//        if ((this.gEnemy[i]).y <= 0 || (this.gEnemy[i]).y >= MainWindow.windowHeight - 10 || (this.gEnemy[i]).x <= 10 - wide ||
//                (this.gEnemy[i]).x >= MainWindow.windowWidth - 10) {
//            return;
//        }
//        while (true) {
//            if (this.gShoot_SL[i]) {
//                if (this.gScreenMove % 5 == 0) {
//                    shootpoint[0] = ((enemySize[((this.gEnemy[i]).type - 1) * 2] >> 1) - 4);
//                    shootpoint[1] = (enemySize[((this.gEnemy[i]).type - 1) * 2 + 1] - 8);
//                    makeBossBullet_SL(i, shootpoint, (this.gEnemy[i]).bulletType);
//                    this.gShootNum_SL[i] = (this.gShootNum_SL[i] + 1);
//                    if (this.gShootNum_SL[i] >= bulletNum) {
//                        this.gShoot_SL[i] = false;
//                        this.gShootNum_SL[i] = 0;
//                    }
//                }
//                return;
//            }
//            if ((this.gEnemy[i]).fireDelay == 0) {
//                return;
//            }
//            if (this.gScreenMove % (this.gEnemy[i]).fireDelay != 0) {
//                return;
//            }
//            int ranVal = getRand(3);
//            if (bulletNum >= 3 && ranVal == 0) {
//                this.gShoot_SL[i] = true;
//                continue;
//            }
//            break;
//        }
//        if (getRand(8 - this.gMission - this.gGameDifficulty) > 0) {
//            bulletNum = (1 + getRand(bulletNum));
//        }
//        int s1 = getRand(4 - this.gGameDifficulty);
//        if (s1 == 0) {
//            makeBulletToPlayer(i, bulletNum, 0, 0, 0);
//        } else {
//            makeBulletToFront(i, bulletNum, 0, 0, 0);
//        }
    }

    private void makeBulletToFront(int i, int bulletNum, int x, int y, int type) {
        int idx = 0;
        if (x == 0 && y == 0) {
            x = ((this.gEnemy[i]).x + (enemySize[((this.gEnemy[i]).type - 1) * 2] >> 1));
            y = ((this.gEnemy[i]).y + enemySize[((this.gEnemy[i]).type - 1) * 2 + 1] - 5);
            type = (this.gEnemy[i]).bulletType;
        } else {
            x = (x + (this.gEnemy[i]).x);
            y = (y + (this.gEnemy[i]).y);
        }
        short offSet = 0;
        for (i = 0; i < bulletNum; i = (short) (i + 1)) {
            offSet = (short) (offSet + i * 2);
        }
        for (i = 0; i < bulletNum; i = (short) (i + 1)) {
            offSet = (short) (offSet + 1);
            int spx = enemyBulletToFront[offSet];
            offSet = (short) (offSet + 1);
            int spy = enemyBulletToFront[offSet];
            idx = mallocBullet();
            if (idx < 0)
                return;
            if (spx == 0 && spy == 0) {
                makeEnemyBulletInit(idx, type, x, y,  0,  4);
                bulletGoToPlayer(idx);
            } else {
                makeEnemyBulletInit(idx, type, x, y, spx, spy);
            }
        }
        if (bulletNum == 1 && getRand(2) == 1) {
            bulletGoToPlayer(idx);
        }
    }

    private void makeBulletToPlayer(int i, int bulletNum, int x, int y, int type) {
        int direction;
        if (x == 0 && y == 0) {
            x = ((this.gEnemy[i]).x + (enemySize[((this.gEnemy[i]).type - 1) * 2] >> 1));
            y = ((this.gEnemy[i]).y + enemySize[((this.gEnemy[i]).type - 1) * 2 + 1] - 5);
            type = (this.gEnemy[i]).bulletType;
        } else {
            x = (x + (this.gEnemy[i]).x);
            y = (y + (this.gEnemy[i]).y);
        }
        int idx = mallocBullet();
        if (idx < 0) {
            return;
        }
        makeEnemyBulletInit(idx, type, x, y, 0, 5);
        bulletGoToPlayer(idx);
        if (bulletNum <= 1) {
            return;
        }
        if (this.gBullet[idx][4] > 0) {
            if (this.gBullet[idx][5] > 0) {
                direction = 1;
            } else if (this.gBullet[idx][5] < 0) {
                direction = 3;
            } else {
                direction = 2;
            }
        } else if (this.gBullet[idx][4] < 0) {
            if (this.gBullet[idx][5] > 0) {
                direction = 7;
            } else if (this.gBullet[idx][5] < 0) {
                direction = 5;
            } else {
                direction = 6;
            }
        } else if (this.gBullet[idx][5] > 0) {
            direction = 0;
        } else {
            direction = 4;
        }

        if (bulletNum >= 2) {
            this.gBullet[idx][4] = bulletSpeedToPlayer[direction * 6];
            this.gBullet[idx][5] = bulletSpeedToPlayer[direction * 6 + 1];

            idx = mallocBullet();
            if (idx < 0) {
                return;
            }
            makeEnemyBulletInit(idx, type, x, y, 0, 0);
            this.gBullet[idx][4] = bulletSpeedToPlayer[direction * 6 + 2];
            this.gBullet[idx][5] = bulletSpeedToPlayer[direction * 6 + 3];
        }

        if (bulletNum >= 3) {
            idx = mallocBullet();
            if (idx < 0)
                return;
            makeEnemyBulletInit(idx, type, x, y, 0, 0);
            if (direction == 0)
                direction = 8;
            this.gBullet[idx][4] = bulletSpeedToPlayer[direction * 6 - 2];
            this.gBullet[idx][5] = bulletSpeedToPlayer[direction * 6 - 1];
        }

        if (bulletNum >= 4) {
            idx = mallocBullet();
            if (idx < 0)
                return;
            makeEnemyBulletInit(idx, type, x, y, 0, 0);
            if (direction == 8)
                direction = 0;
            this.gBullet[idx][4] = bulletSpeedToPlayer[direction * 6 + 4];
            this.gBullet[idx][5] = bulletSpeedToPlayer[direction * 6 + 5];
        }
        if (bulletNum >= 5) {
            idx = mallocBullet();
            if (idx < 0)
                return;
            makeEnemyBulletInit(idx, type, x, y, 0, 0);
            if (direction == 0)
                direction = 8;
            this.gBullet[idx][4] = bulletSpeedToPlayer[direction * 6 - 4];
            this.gBullet[idx][5] = bulletSpeedToPlayer[direction * 6 - 3];
        }
        if (bulletNum >= 6) {
            idx = mallocBullet();
            if (idx < 0)
                return;
            makeEnemyBulletInit(idx, type, x, y, 0, 4);
            bulletGoToPlayer(idx);
        }
    }

    private void makeBossBullet_SL(int i, int[] shootpoint, int bullettype) {
        int idx = mallocBullet();
        if (idx < 0) {
            return;
        }
        if (bullettype == 0) {
            this.gBullet[idx][0] = bossBullet[(this.gEnemy[i]).type - 200 - 1][this.gBossBulletSequenceMax][2];
        } else {
            this.gBullet[idx][0] = bullettype;
        }
        this.gBullet[idx][2] = ((this.gEnemy[i]).x + shootpoint[0]);
        this.gBullet[idx][3] = ((this.gEnemy[i]).y + shootpoint[1]);
        this.gBullet[idx][4] = 0;
        if (bullettype == 0) {
            this.gBullet[idx][5] = 5;
        } else {
            this.gBullet[idx][5] = 4;
        }
        this.gBullet[idx][1] = getRand(bulletPic[this.gBullet[idx][0] - 1]);

        if (getRand(2) == 1) {
            bulletGoToPlayer(idx);
        } else {
            this.gBullet[idx][4] = bulletSpeedSL[getRand(9) * 2];
            this.gBullet[idx][5] = bulletSpeedSL[getRand(9) * 2 + 1];
        }
    }

    private void makeEnemy() {
        this.gScreenMove = this.gScreenMove + 1;
        if (!this.gIsEnableEnemy) {
            return;
        }
        if (this.gScreenMove % 10 == 0) {
            int missionBulletNum;
            int ranVal = getRand(this.gEnemyCount + 1);
            if (ranVal > 0) {
                return;
            }
            int idx = mallocEnemy();
            if (idx < 0) {
                return;
            }
            makeEnemyInit(idx);
            this.gAppearEnemyCount = this.gAppearEnemyCount + 1;
            if (this.gAppearEnemyCount > this.gApearEnemyType) {
                if ((this.gApearEnemyType < 15 && this.gMission != 3) || (this.gApearEnemyType < 10 && this.gMission == 3)) {
                    this.gApearEnemyType = this.gApearEnemyType + 1;
                } else {
                    playSound(3);
                    this.gIsBossAppear = true;
                    this.gIsEnableEnemy = false;
                }
                this.gAppearEnemyCount = 0;
            }
            if (1 == this.gGameDifficulty) {
                (this.gEnemy[idx]).health = ((this.gEnemy[idx]).health + ((this.gEnemy[idx]).health >> 2));
                (this.gEnemy[idx]).fireDelay = ((this.gEnemy[idx]).fireDelay - 5);
                (this.gEnemy[idx]).bulletMax = ((this.gEnemy[idx]).bulletMax + 1);
                (this.gEnemy[idx]).reward = ((this.gEnemy[idx]).reward + ((this.gEnemy[idx]).reward >> 1));
            }

            if (1 == this.gMission) {
                missionBulletNum = -1;
            } else if (3 == this.gMission || 5 == this.gMission) {
                missionBulletNum = 1;
            } else {
                missionBulletNum = 0;
            }
            if ((this.gEnemy[idx]).bulletMax + missionBulletNum <= 6 && (this.gEnemy[idx]).bulletMax + missionBulletNum >= 1) {
                (this.gEnemy[idx]).bulletMax = (this.gEnemy[idx]).bulletMax + missionBulletNum;
            }
            (this.gEnemy[idx]).colors = getRand(enemyData[(this.gEnemy[idx]).type - 1][1]);
        }
    }

    private void makeBoss() {
        if (this.gIsBossAppear && this.gBossIndex < 0 && this.gEnemyCount <= 0 && !this.gIsMissionComplete) {
            int idx = mallocEnemy();
            if (idx < 0) {
                return;
            }

            int ttmp = 47 + this.gCorrespondMission - 2;
            this.gEnemy[idx].setValue(enemyData[ttmp][0], enemyData[ttmp][1], enemyData[ttmp][2], enemyData[ttmp][3],
                    enemyData[ttmp][4], enemyData[ttmp][5], enemyData[ttmp][6],
                    enemyData[ttmp][7], enemyData[ttmp][8], enemyData[ttmp][9], enemyData[ttmp][10],
                    enemyData[ttmp][11], enemyData[ttmp][12], enemyData[ttmp][13],
                    enemyData[ttmp][14], enemyData[ttmp][15], enemyData[ttmp][16]);


            (this.gEnemy[idx]).y = -enemySize[(47 + this.gCorrespondMission - 2) * 2 + 1];
            this.gBossIndex = idx;
            this.gBossMoveAction = 1;
            this.gBossInitDirect = true;
            if (1 == this.gGameDifficulty) {
                (this.gEnemy[idx]).health = ((this.gEnemy[idx]).health + ((this.gEnemy[idx]).health >> 2));
                (this.gEnemy[idx]).reward = ((this.gEnemy[idx]).reward + ((this.gEnemy[idx]).reward >> 1));
            }
        }
    }

    private void makeEnemyInit(int id) {
        int type;
        if (this.gMission <= 4) {
            int ranValTemp = getRand(this.gApearEnemyType);
            type = (stageEnemy[this.gCorrespondMission - 1][ranValTemp] - 1);
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
        this.gEnemy[id].setValue(enemyData[type][0], enemyData[type][1],
                enemyData[type][2], enemyData[type][3], enemyData[type][4],
                enemyData[type][5], enemyData[type][6], enemyData[type][7],
                enemyData[type][8], enemyData[type][9], enemyData[type][10],
                enemyData[type][11], enemyData[type][12], enemyData[type][13],
                enemyData[type][14], enemyData[type][15], enemyData[type][16]);

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
                (this.gEnemy[id]).pauseDelay = getRand(180);
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
                    (this.gEnemy[id]).offset = (getRand(25) + 25);
                    if ((this.gEnemy[id]).x < 0 || (this.gEnemy[id]).x > MainWindow.windowWidth - wide) {
                        (this.gEnemy[id]).offset = 0;
                    }
                } else if (ranVal == 2) {
                    if ((this.gEnemy[id]).aliveTime > 0) {
                        (this.gEnemy[id]).aliveTime = (getRand(80) + 40);
                    }

                    (this.gEnemy[id]).pauseDelay = getRand(180);
                    (this.gEnemy[id]).offset = (getRand(25) + 25);
                    if ((this.gEnemy[id]).x < 0 || (this.gEnemy[id]).x > MainWindow.windowWidth - wide) {
                        (this.gEnemy[id]).offset = 0;
                    }
                } else {
                    if ((this.gEnemy[id]).aliveTime > 0) {
                        (this.gEnemy[id]).aliveTime = -1;
                    }
                    (this.gEnemy[id]).pauseDelay = getRand(150);
                    (this.gEnemy[id]).offset = getRand(25) + 25;
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
            (this.gEnemy[id]).y = getRand(140);
            if ((this.gEnemy[id]).speedY == 0) {
                int s = getRand(3);
                if (s == 1) {
                    (this.gEnemy[id]).speedY = 1;
                } else if (s == 2) {
                    (this.gEnemy[id]).speedY = -1;
                }
            }
            int ranVal = getRand(2);
            if ((this.gEnemy[id]).offset > 100 && ranVal == 1) {
                (this.gEnemy[id]).offset = 100;
            }
            if ((this.gEnemy[id]).offset > 100) {
                int ranValTemp = ((this.gEnemy[id]).offset - 100) * Math.abs((this.gEnemy[id]).speedY);
                if ((this.gEnemy[id]).y < ranValTemp) {
                    (this.gEnemy[id]).y = (ranValTemp + getRand(50));
                }
            } else if ((this.gEnemy[id]).speedY < 0) {
                int ranValTemp = (120 / Math.abs((this.gEnemy[id]).speedX) * Math.abs((this.gEnemy[id]).speedY));
                if ((this.gEnemy[id]).y < ranValTemp) {
                    (this.gEnemy[id]).y = (ranValTemp + getRand(50));
                }
            } else if ((this.gEnemy[id]).speedY > 0) {
                (this.gEnemy[id]).y = getRand(90);
            }
        }
        (this.gEnemy[id]).reward = ((this.gEnemy[id]).reward + getRand((this.gEnemy[id]).reward));
        makeGoods(id);

    }

    private void bulletGoToPlayer(int i) {
        int x = (this.gBullet[i][2] - this.gPlayer.x + (playerSize[this.gSelectedPlayer * 2] >> 1));
        int y = (this.gBullet[i][3] - this.gPlayer.y + (playerSize[this.gSelectedPlayer * 2 + 1] >> 1));

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
                this.gBullet[i][4] = this.gBullet[i][5];
                int scale = (x / this.gBullet[i][5]);
                if (scale <= 0)
                    scale = 1;
                this.gBullet[i][5] = (y / scale);
            } else {
                int scale = (y / this.gBullet[i][5]);
                if (scale <= 0)
                    scale = 1;
                this.gBullet[i][4] = (x / scale);
            }
            if (flag_x)
                this.gBullet[i][4] = -this.gBullet[i][4];
            if (flag_y) {
                this.gBullet[i][5] = -this.gBullet[i][5];
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

    private int mallocBullet() {
        if (this.gBulletCount >= 100)
            return -1;
        for (int i = 0; i < 100; i = (i + 1)) {
            if (this.gBullet[i][0] == 0) {
                this.gBulletCount = (this.gBulletCount + 1);
                return i;
            }
        }
        return -1;
    }

    private void makeEnemyBulletInit(int i, int type, int x, int y, int xspd, int yspd) {
        this.gBullet[i][0] = type;
        this.gBullet[i][2] = x;
        this.gBullet[i][3] = y;
        this.gBullet[i][4] = xspd;
        this.gBullet[i][5] = yspd;
        this.gBullet[i][1] = getRand(bulletPic[this.gBullet[i][0] - 1]);
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
            } else if (this.gPlayer.power == 1 && getRand(10) == 1) {
                (this.gEnemy[i]).item = 7;
            }
        } else {
            if (this.gPlayer.power < 3 && getRand(4) == 0) {
                (this.gEnemy[i]).item = 7;
                return;
            }
            if (getRand(6) == 0) {
                (this.gEnemy[i]).item = gGetItemsList[getRand(20)];
                if (12 == (this.gEnemy[i]).item) {
                    (this.gEnemy[i]).item = (this.gEnemy[i]).item + this.gSelectedPlayer;
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

    private void playSound(int index) {
        if (index == 12) {
            mediaPlayers[index].setLooping(true);
        }

        mediaPlayers[index].prepareAsync();
        mediaPlayers[index].isPlaying();
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

    private void setRect(int x, int y, int w, int h) {
        this.gSrcRect.x = x;
        this.gSrcRect.y = y;
        this.gSrcRect.w = w;
        this.gSrcRect.h = h;
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
        Log.d("TAG", "surfaceCreated: ");
        isThreadAlive = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        Log.d("TAG", "surfaceChanged: " + width + "  " + height);
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        Log.d("TAG", "surfaceDestroyed: ");
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
                    driveGameScreen(lockCanvas);
                    surfaceHolder.unlockCanvasAndPost(lockCanvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void driveGameScreen(Canvas canvas) throws Exception {
        showBackScreen(canvas);
        //showBoss(canvas);
        showEnemy(canvas);
        //showBomb(canvas);
        showPlayer(canvas);
        //showItems(canvas);
        //showBullet(canvas);
        //showExplode(canvas);
        showActiveLife(canvas);
        showActiveBomb(canvas);
        showActiveScore(canvas);
        showMissionInfo(canvas);
        showGameOver(canvas);
    }

    private void showEnemy(Canvas canvas) {
        for (int i = 0; i < 5; i++) {
            int type = (this.gEnemy[i]).type;
            if (type != 0 && type <= 200) {
                type = (type - 1);

                //同一张图片下的第张
                int idx = (this.gEnemy[i]).picId;
                if (idx == enemyPic[type] - 1) {//第一张和第二张图片相同就用第一张
                    (this.gEnemy[i]).picId = 0;
                }

                int x = (this.gEnemy[i]).x;
                int y = (this.gEnemy[i]).y;

                Bitmap source = ResInit.enemyImage[(this.gEnemy[i]).type - 1][(this.gEnemy[i]).colors];
                Bitmap bitmap = Bitmap.createBitmap(source, source.getWidth() / enemyPic[this.gEnemy[i].type] * idx, 0, source.getWidth() / enemyPic[this.gEnemy[i].type], source.getHeight());


                int wide = bitmap.getWidth();
                int high = bitmap.getHeight();
                //在屏幕内
                if (x > -wide && x < MainWindow.windowWidth && y > -high && y < MainWindow.windowHeight) {
                    //@TODO 这里疑似费代码,不移植
                    drawBitmapXY(canvas, bitmap, x, y);
                }
            }

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
                if (this.gMission == 5) {
                    drawBitmapCenter(canvas, ResInit.otherImage[10]);
                } else {
                    drawBitmapCenterHorizontal(canvas, ResInit.otherImage[9], ResInit.numberImageValue[0][this.gCorrespondMission], 100);
                }
            } else {
                this.gIsEnableEnemy = true;
            }
        }
        if (this.gIsMissionComplete) {
            this.gTempDelay = (this.gTempDelay + 1);
            if (this.gTempDelay > 90 && !this.gIsGameFinished) {
                if (this.gMission >= 5) {
                    drawBitmapCenterVertical(canvas, ResInit.otherImage[5], ResInit.otherImage[3], 50);
                } else {
                    drawBitmapCenterVertical(canvas, ResInit.otherImage[9], ResInit.otherImage[3], 50);
                }
            }
        }
        //按60帧算的话这里应该是6秒左右
        if (this.gTempDelay > 400) {
            if (this.gMission >= 5) {
                drawBitmapCenter(canvas, ResInit.otherImage[0]);
                if (this.gTempDelay > 800) {
                    this.gIsGameFinished = true;
                }
                return;
            }

            this.gMission =  (this.gMission + 1);
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
        for (short i = 0; i < this.gPlayer.bomb; i = (short) (i + 1)) {
            int x = (27 + i * 45);
            drawBitmapXY(canvas, ResInit.bombIcon[this.gBombType[i] - 1], x, 120);
        }
    }

    private void showActiveLife(Canvas canvas) {
        for (short i = 0; i < this.gPlayer.life - 1; i = (short) (i + 1)) {
            int x = (27 + i * 45);
            drawBitmapXY(canvas, ResInit.lifeIcon[this.gSelectedPlayer], x, 50);
        }
    }

    private void showPlayer(Canvas canvas) {
        if (this.gIsGameOver) {
            return;
        }
        int x = this.gPlayer.x;
        int y = this.gPlayer.y;

        int wide = playerSize[this.gSelectedPlayer * 2];
        int high = playerSize[this.gSelectedPlayer * 2 + 1];

        if (x <= -wide || x >= MainWindow.windowWidth || y <= -high || y >= MainWindow.windowHeight) {
            return;
        }
        if (this.gPlayerInit > 0 && (this.gScreenMove & 0x1) != 0) {
            this.gPlayer.picId = 5;
        }
        drawBitmapXY(canvas, ResInit.playerStateImage[gPlayer.picId], gPlayer.x, gPlayer.y);

        if (this.gIsPlayerFire) {
            gIsPlayerFire = false;
            drawBitmapXY(canvas, ResInit.playerStateImage[4], gPlayer.x, gPlayer.y);
        }
    }

    private void showBackScreen(Canvas canvas) {
        this.gSrcRect.x = 0;
        this.gSrcRect.w = MainWindow.windowWidth;
        int Rect_y = -(this.gBackgroundHeight - MainWindow.windowHeight - this.gBackgroundOffset);
        if (Rect_y >= 0) {
            drawBitmapXY(canvas, ResInit.BackgroundImage[this.gCorrespondMission - 1], 0, -(this.gBackgroundHeight - Rect_y));
        }
        drawBitmapXY(canvas, ResInit.BackgroundImage[this.gCorrespondMission - 1], 0, Rect_y);

        if (Rect_y >= MainWindow.windowHeight) {
            this.gBackgroundOffset = 0;
        }
        if (this.gIsBossAppear) {
            this.gBackgroundOffset = (this.gBackgroundOffset + 8);
        } else {
            this.gBackgroundOffset = (this.gBackgroundOffset + 5);
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
}
