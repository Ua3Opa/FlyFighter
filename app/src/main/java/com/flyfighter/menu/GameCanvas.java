package com.flyfighter.menu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.flyfighter.entity.Bomb;
import com.flyfighter.entity.Bullet;
import com.flyfighter.entity.EnemyPlane;
import com.flyfighter.entity.Explode;
import com.flyfighter.entity.Item;
import com.flyfighter.entity.Missile;
import com.flyfighter.entity.PlayerBullet;
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
    private final int maxItem = 6;
    public volatile boolean isThreadAlive = false;
    private boolean gIsLoadGame = true;
    private boolean gIsSaved;
    private int gContinueNum;
    private boolean mIsGameFinished;
    private boolean gIsPlayerFire = true;
    private int mMission;
    private int mGameScore;

    private int mMissileType;
    private int mMissileMax;
    private long mMissileShootTime;

    private int gOtherTypeBomb;
    private int gGameSecretNum;
    private int gGamePause;
    //无敌时间为5秒
    private long shieldMaxMills = 5 * 1000;
    private long bombMaxMills = 4 * 1000;
    private boolean mIsMissionComplete;
    private int gTempDelay;
    private int gBombDelay;
    private boolean mIsEnableEnemy;
    private boolean gIsGameOver;
    private boolean mIsBombing;
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
    private int mFireDelay;
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
    public List<PlayerBullet> playerBullets = new ArrayList<>();
    public List<Explode> explodes = new ArrayList<>();
    public List<Item> items = new ArrayList<>();
    public List<Bomb> bombs = new ArrayList<>();
    public List<Missile[]> missiles = new ArrayList<>();

    public static final int[][] stageEnemy = new int[][]{
            {1, 2, 4, 38, 44, 19, 39, 22, 45, 26, 46, 25, 43, 36, 40},
            {5, 2, 4, 24, 29, 27, 46, 21, 23, 39, 45, 30, 28, 33, 35},
            {6, 4, 3, 43, 20, 39, 23, 31, 45, 34, 38, 46, 37, 32, 41},
            {19, 45, 20, 46, 19, 45, 20, 46, 19, 45, 20, 46, 19, 45, 20},
            {46, 44, 43, 22, 20, 39, 23, 31, 45, 34, 38, 46, 37, 32, 41}
    };

    private static final int[] enemySize = new int[]{126, 103, 108, 126, 108, 126, 135, 117, 117, 135, 117, 135, 171, 126, 126, 171, 126, 171, 189, 126, 126, 189, 126, 189, 153, 94, 99, 153, 99, 153, 144, 121, 153, 162, 153, 162, 171, 198, 171, 198, 333, 139, 261, 180, 261, 180, 252, 139, 423, 130, 333, 162, 405, 157, 369, 144, 243, 175, 423, 126, 378, 144, 279, 189, 279, 189, 315, 162, 189, 265, 189, 265, 333, 153, 279, 144, 252, 139, 378, 157, 315, 180, 306, 202, 243, 126, 207, 108, 171, 135, 171, 238, 576, 288, 504, 360, 927, 270, 684, 279, 765, 238};

    private static final byte[] gGetItemsList = new byte[]{5, 5, 5, 6, 6, 6, 7, 7, 8, 1, 2, 3, 4, 12, 9, 9, 10, 10, 11, 11};

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

    public static final int[][] playerBulletData = new int[][]
            // 第二位改成飞机中心偏移值
            {{0, 0, -2, 0, -23},
                    {1, -24, 0, 0, -20}, {1, 24, 0, 0, -20},
                    {1, -24, 0, -1, -20}, {0, 0, -7, 0, -25}, {1, 24, 0, 1, -20},
                    {1, -32, 0, -1, -20}, {0, -24, -6, 0, -25}, {0, 24, -6, 0, -25}, {1, 32, 0, 1, -20},
                    {0, -38, 0, -2, -23}, {1, -24, -2, -1, -24}, {0, 0, 0, 0, -28}, {1, 24, -2, 1, -24}, {0, 38, 0, 2, -23},

                    {1, 0, 0, 0, -23},
                    {1, -26, 0, 0, -23}, {1, 26, 0, 0, -23},
                    {0, -26, 0, -1, -23}, {1, 0, -6, 0, -30}, {26, 15, 0, 1, -23},
                    {0, -34, 0, -1, -23}, {1, -26, 0, 0, -30}, {1, 26, 0, 0, -30}, {2, 34, 0, 1, -23},
                    {0, -40, -2, -1, -23}, {0, -26, 0, 0, -27}, {1, 0, -4, 0, -33}, {2, 26, 0, 0, -27}, {2, 40, -2, 1, -23},

                    {1, 0, -5, 0, -23},
                    {2, -34, 0, 0, -23}, {2, 34, 0, 0, -23},
                    {2, -44, 1, 0, -23}, {1, 0, -5, 0, -30}, {2, 44, 1, 0, -23},
                    {2, -50, 1, 0, -23}, {1, -20, -5, 0, -30}, {1, 20, -5, 0, -30}, {2, 50, 1, 0, -23},
                    {2, -55, -2, 0, -23}, {1, -25, -5, 0, -30}, {1, 0, -2, 0, -30}, {1, 25, -5, 0, -30}, {2, 55, -2, 0, -23}};

    public static final int[] bulletSize = new int[]{7, 22, 9, 20, 2, 20, 8, 8, 10, 8, 8, 8, 8, 8, 8, 8, 14, 13, 14, 13, 10, 9, 10, 9, 10, 9, 8, 8, 8, 8, 12, 12, 12, 12, 16, 16, 16, 16, 16, 16, 8, 320};

    public static final int[] bulletSpeedSL = new int[]{-2, 1, -2, 2, -1, 2, -1, 3, 0, 3, 1, 3, 1, 2, 2, 2, 2, 1};

    public static final int[][] playerBulletFloorPower = new int[][]{{6, 6, 5, 4, 4}, {13, 11, 8, 7, 6}, {11, 6, 6, 6, 5}};

    public static final byte[] enemyBulletToFront = new byte[]{0, 4, -1, 4, 1, 4, -1, 4, 0, 4, 1, 4, -1, 4, 0, 4, 1, 4, 0, 0, -2, 3, -1, 4, 0, 4, 1, 4, 2, 3, -2, 3, -1, 4, 0, 4, 1, 4, 2, 3, 0, 0};

    public static final int[] playerSize = new int[]{26, 32, 30, 28, 30, 28};

    public static final int[] bulletSpeedMissile = new int[]{0, -7, -3, -7, -5, -5, -7, -3, -7, 0, -7, 3, -5, 5, -3, 7, 0, 7, 3, 7, 5, 5, 7, 3, 7, 0, 7, -3, 5, -5, 3, -7};

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
        this.mIsGameFinished = false;
        this.gIsPlayerFire = false;
        this.mMission = 1;
        this.mGameScore = 0;
        this.mMissileType = 0;
        this.mMissileMax = 0;
        this.gOtherTypeBomb = 0;
        this.gGameSecretNum = -1;
    }

    private void stageInit() {
        this.gGamePause = 0;
        this.mIsMissionComplete = false;
        this.gTempDelay = 0;
        this.gBombDelay = 0;
        this.mIsEnableEnemy = false;
        this.gIsGameOver = false;
        this.mIsBombing = false;
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
        this.mFireDelay = 0;
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
        dealMissile();
        dealExplodes();
        dealItems();
        dealShield();
        dealBomb();
    }

    private void dealBomb() {
        for (int i = 0; i < bombs.size(); i++) {
            Bomb bomb = bombs.get(i);
            bomb.dealMoveState(mPlayer);
            if (BombOutScreen(bomb)) {
                bombs.remove(bomb);
            } else if (BombHitEnemy(bomb)) {

            } else if (BombHitBullets(bomb)) {
            }
        }
    }

    private boolean BombHitBullets(Bomb bomb) {
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            if (checkIfHit(bullet.x, bullet.y, bullet.width, bullet.height, bomb.x, bomb.y, bomb.source.get(0).getWidth(), bomb.source.get(0).getHeight())) {
                bullets.remove(bullet);
            }
        }
        return false;
    }

    private boolean BombOutScreen(Bomb bomb) {
        //5秒
        if (bomb.type == 1 && (System.currentTimeMillis() - bomb.boomTimeMills > bombMaxMills)) {
            return true;
        }
        if (bomb.type == 2 && (bomb.y + bomb.source.get(0).getHeight()) < 0) {
            return true;
        }
        if (bomb.type == 3) {
            if (bomb.direction == -1 && (bomb.x + bomb.source.get(0).getWidth()) < 0) {
                return true;
            } else if (bomb.direction == 1 && bomb.x > MainWindow.windowWidth) {
                return true;
            }
        }
        return false;
    }

    private boolean BombHitEnemy(Bomb bomb) {
        for (int i = enemys.size() - 1; i >= 0; i--) {
            EnemyPlane enemy = enemys.get(i);
            if (checkIfHit(enemy.x, enemy.y, enemy.width, enemy.height, bomb.x, bomb.y, bomb.source.get(0).getWidth(), bomb.source.get(0).getHeight())) {
                enemy.health -= bomb.power;
                mGameScore += 88;
            }
        }
        return false;
    }

    /**
     * 做成成对出现的
     * 为了保证y轴的位置相同 必须使用 List<Missile[]> 结构的数据
     */
    private void dealMissile() {
        mMissileType = 2;
        mMissileMax = 2;
        for (int i = 0; i < missiles.size(); i++) {
            Missile[] ms = missiles.get(i);
            if (ms[0] != null) {
                ms[0].dealMoveState(getNearestEnemy(ms[0]));
            }
            if (ms[1] != null) {
                ms[1].dealMoveState(getNearestEnemy(ms[1]));
            }
            checkMissileOutScreen(ms);
            if (ms[0] == null && ms[1] == null) {
                missiles.remove(ms);
            } else if (checkMissileHitEnemy(ms)) {
                //在checkMissileHitEnemy处理逻辑
            }
        }
        if (missiles.size() >= mMissileMax) {
            return;
        }
        //限制发射频率
        if (System.currentTimeMillis() - mMissileShootTime <= 400) {
            return;
        }
        mMissileShootTime = System.currentTimeMillis();
        missiles.add(Missile.makeMissiles(mMissileType, getPlayerCenterX(), mPlayer.y));
    }

    private void checkMissileOutScreen(Missile[] ms) {
        if (ms[0] != null && outScreen(ms[0].x, ms[0].y, ms[0].getImgBitmap())) {
            ms[0] = null;
        }
        if (ms[1] != null && outScreen(ms[1].x, ms[1].y, ms[1].getImgBitmap())) {
            ms[1] = null;
        }
    }

    private void dealShield() {
        if (bombs.size() == 0) {
            return;
        }
        for (int i = 0; i < bombs.size(); i++) {
            Bomb bomb = bombs.get(i);
            if (bomb.type == 4) {
                if (System.currentTimeMillis() - bomb.boomTimeMills >= shieldMaxMills) {
                    bombs.remove(bomb);
                    return;
                } else {
                    Size imgSize = bomb.getImgSize();
                    bomb.x = mPlayer.x + mPlayer.width / 2 - imgSize.getWidth() / 2;
                    bomb.y = mPlayer.y + mPlayer.height / 2 - imgSize.getHeight() / 2;
                }
            }
        }
    }

    private void dealItems() {
        for (int i = items.size() - 1; i >= 0; i--) {
            Item item = items.get(i);
            item.x += item.speedX;
            item.y += item.speedY;
            if (item.x < 0 || item.x > (MainWindow.windowWidth - item.sourceImg.getWidth() / 2)) {
                item.speedX = -item.speedX;
            }
            if (checkHitPlayer(item)) {
                handleGetItems(item);
                items.remove(item);
            } else if (outScreen(item.x, item.y, item.getImg0())) {
                items.remove(item);
            }
        }
    }


    private void dealExplodes() {
        for (int i = explodes.size() - 1; i >= 0; i--) {
            if (explodes.get(i).picIndex >= explodes.get(i).picNum) {
                explodes.remove(explodes.get(i));
            }
        }
    }

    private void dealBullet() {
        for (int i = bullets.size() - 1; i >= 0; i--) {
            bullets.get(i).x += bullets.get(i).speedX;
            bullets.get(i).y += bullets.get(i).speedY;
            if (outScreen(bullets.get(i).x, bullets.get(i).y, bullets.get(i).sourceImg)) {
                bullets.remove(bullets.get(i));
            } else if (checkHitPlayer(bullets.get(i))) {

            }
        }

        for (int i = playerBullets.size() - 1; i >= 0; i--) {
            playerBullets.get(i).x += playerBullets.get(i).speedX;
            playerBullets.get(i).y += playerBullets.get(i).speedY;
            if (outScreen(playerBullets.get(i).x, playerBullets.get(i).y, playerBullets.get(i).sourceImg)) {
                playerBullets.remove(playerBullets.get(i));
            } else if (checkMissileHitEnemy(playerBullets.get(i))) {
                explodes.add(Explode.dealExplodeState(playerBullets.get(i), getRand(2)));
                mGameScore += 28;
                playerBullets.remove(playerBullets.get(i));
            }
        }
    }

    private boolean checkMissileHitEnemy(Missile[] ms) {
        for (EnemyPlane enemy : enemys) {
            if (ms[0] != null && checkIfHit(ms[0].x, ms[0].y, ms[0].getImgBitmap().getWidth(), ms[0].getImgBitmap().getHeight(), enemy.x, enemy.y, enemy.width, enemy.height)) {
                enemy.health -= ms[0].power;
                mGameScore += 28;
                ms[0] = null;
            }
            if (ms[1] != null && checkIfHit(ms[1].x, ms[1].y, ms[1].getImgBitmap().getWidth(), ms[1].getImgBitmap().getHeight(), enemy.x, enemy.y, enemy.width, enemy.height)) {
                enemy.health -= ms[1].power;
                mGameScore += 28;
                ms[1] = null;
            }
        }
        return false;
    }

    private boolean checkMissileHitEnemy(PlayerBullet bullet) {
        for (EnemyPlane enemy : enemys) {
            if (checkIfHit(bullet.x, bullet.y, bullet.getImg().getWidth(), bullet.getImg().getHeight(), enemy.x, enemy.y, enemy.width, enemy.height)) {
                //{6, 6, 5, 4, 4}, {13, 11, 8, 7, 6}, {11, 6, 6, 6, 5}
                enemy.health -= playerBulletFloorPower[playerType][mPlayer.power - 1] - 1;
                return true;
            }
        }
        return false;
    }

    private boolean checkHitPlayer(Item item) {
        if (checkIfHit(item.x, item.y, item.getImg0().getWidth(), item.getImg0().getHeight(), mPlayer.x, mPlayer.y, mPlayer.width, mPlayer.height)) {
            return true;
        }
        return false;
    }

    //@TODO 先保持无敌状态
    private boolean checkHitPlayer(Bullet bullets) {

        return false;
    }

    private void dealEnemyState() {
        for (int i = enemys.size() - 1; i >= 0; i--) {
            if (enemys.get(i).health <= 0) {
                //3,4,5,6,7
                explodes.add(Explode.dealExplodeState(enemys.get(i), getRand(4) + 2));
                makeGoods(enemys.get(i));
                mGameScore += enemys.get(i).reward;
                enemys.remove(i);
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
        //Log.d("TAG", "makeEnemyBullet: " + (System.currentTimeMillis() - enemy.createTime) + "    " + delay);
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
            } else if (ranVal == 1) {
                makeBulletToFront(enemy, bulletNum);
            }
        }
    }

    private void handleGetItems(Item item) {
        //this.playSound(4);
        switch (item.type) {
            case 1://boom类型
            case 2:
            case 3:
                handleGetBomb(item);
                break;
            case 4://炸弹类型4 圆环效果
                bombs.add(Bomb.makeBomb(4, mPlayer));
                break;
            case 5:
                mGameScore += 555;
                break;
            case 6:
                mGameScore += 999;
                break;
            case 7:
                if (mPlayer.power < 5) {
                    mPlayer.power++;
                }
            case 8:
                mPlayer.power = 5;
                break;
            case 9://追踪弹
                if (mMissileType != 2) {
                    mMissileType = 2;
                    mMissileMax = 0;
                }
                if (mMissileMax + 1 <= 2) {
                    mMissileMax++;
                }
                break;
            case 10://双导弹
                if (mMissileType != 1) {
                    mMissileType = 1;
                    mMissileMax = 0;
                }
                if (mMissileMax + 1 <= 2) {
                    mMissileMax++;
                }
                break;
            case 11:
                bullets.clear();
                break;
            case 12:
            case 13:
            case 14:
                if (mPlayer.life < 6) {
                    mPlayer.life++;
                }
                break;
            default:
                break;

        }
    }

    private void handleGetBomb(Item item) {
        if (mPlayer.bomb < 6) {
            mPlayer.bombTypes.add(item.type - 1);
            mPlayer.bomb++;
        } else {
            mGameScore += 888;
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
        int bulletTypeNum = GameCanvas.bulletPic[enemy.bulletType];

        int picIndex = 0;
        for (int j = 0; j < enemy.bulletType; ++j) {//计算子弹图片的索引
            picIndex += GameCanvas.bulletPic[j];
        }

        Bitmap bitmap = ResInit.bulletImage[picIndex];

        Bullet bullet = Bullet.mallocBullet(enemy.bulletType, shootX, shootY, 0, 10, bulletTypeNum, bitmap);
        if (bullet.speedY == 0) {
            bullet.speedY = -new Random().nextInt(13);
        }
        bullets.add(bullet);
        return bullet;
    }

    private void makeBulletToFront(EnemyPlane enemy, int bulletNum) {
        int offSet = 0;

        for (int i = 0; i < bulletNum; ++i) {
            offSet = (offSet + i * 2);
        }

        for (int i = 0; i < bulletNum; ++i) {
            if (mallocBullet()) {
                Bullet bullet = makeEnemyBullet(enemy);
                bullet.speedX = enemyBulletToFront[offSet++];
                bullet.speedY = enemyBulletToFront[offSet++];
                if (bullet.speedX == 0 && bullet.speedY == 0) {
                    continue;
                }
                bullets.add(bullet);
            }
        }
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
                if (mIsMissionComplete) {
                    return;
                }
                mPlayer.handleMove();
                if (mFireDelay > 6) {//按60帧的帧率控制
                    mFireDelay = 0;
                    makePlayerBullet(mPlayer);
                }
                mFireDelay++;
            }
        }
    }

    long lastTime = 0;

    private void makePlayerBullet(PlayerPlane player) {
        //playSound(this.gSelectedPlayer + 5);
        int bulletNum;
//        if (System.currentTimeMillis() - lastTime >= 5000) {
//            player.power++;
//            player.power = player.power >= 5 ? 5 : player.power;
//            lastTime = System.currentTimeMillis();
//        }
        if (1 == this.gGameSecretNum) {
            bulletNum = 4;
        } else {
            bulletNum = mPlayer.power - 1;
        }

        int bulletNumTemp = bulletNum;

        int offSet = playerBulletFloorNum[playerType][5];

        for (int i = bulletNumTemp - 1; i >= 0; i--) {
            offSet += playerBulletFloorNum[playerType][i];
        }

        for (bulletNumTemp = playerBulletFloorNum[playerType][bulletNum]; bulletNumTemp > 0; bulletNumTemp--) {
            PlayerBullet bullet = mPlayer.makeBullet(playerBulletData[offSet]);
            playerBullets.add(bullet);
            ++offSet;
        }

    }

    private EnemyPlane getNearestEnemy(Missile missile) {
        if (enemys.isEmpty()) {
            return null;
        }
        EnemyPlane nearest = null;
        for (EnemyPlane enemy : enemys) {
            if (nearest == null) {
                nearest = enemy;
                continue;
            }
            int disE = (int) (Math.pow(enemy.x - missile.x, 2) + Math.pow(enemy.y - missile.y, 2));
            int disN = (int) (Math.pow(nearest.x - missile.x, 2) + Math.pow(nearest.y - missile.y, 2));

            if (disE < disN) {
                nearest = enemy;
            }
        }
        return nearest;
    }

    private boolean mallocBullet() {
        if (bullets.size() >= 100)
            return false;
        return true;
    }

    /**
     * 生成奖励
     */
    private void makeGoods(EnemyPlane enemy) {
        if (items.size() >= maxItem) {
            return;
        }
        int itemType = 0;
        if (enemy.type <= 18) {
            if (getRand(10) == 0) {
                itemType = 5;
            } else if (this.mPlayer.power == 1 && getRand(10) == 1) {
                itemType = 7;
            }
        } else {
            if (this.mPlayer.power < 3 && getRand(4) == 0) {
                itemType = 7;
                return;
            }
            if (getRand(6) == 0) {
                itemType = gGetItemsList[getRand(20)];
                if (12 == itemType) {
                    itemType += playerType;
                }
            }
        }
        Item item = new Item(itemType, enemy.x, enemy.y);
        items.add(item);
    }

    private boolean checkIfHit(int sx, int sy, int sw, int sh, int dx, int dy, int dw, int dh) {
        int mw = 0, mh = 0;
        sw = (sw - sw / 3);
        sh = (sh - sh / 3);
        dw = (dw - dw / 3);
        dh = (dh - dh / 3);
        if (dw > sw) {
            dx = (dx + dw / 5);
        } else {
            sx = (sx + sw / 5);
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
        this.isThreadAlive = false;
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
        showBombs(canvas);
        //showBomb(canvas);
        showPlayer(canvas);
        showItems(canvas);
        showBullet(canvas);
        showMissile(canvas);
        showExplode(canvas);
        showActiveLife(canvas);
        showActiveBomb(canvas);
        showActiveScore(canvas);
        showMissionInfo(canvas);
        showGameOver(canvas);
    }

    private void showMissile(Canvas canvas) {
        for (Missile[] missile : missiles) {
            if (missile[0] != null) {
                drawBitmapXY(canvas, missile[0].getImg(), missile[0].x, missile[0].y);
            }
            if (missile[1] != null) {
                drawBitmapXY(canvas, missile[1].getImg(), missile[1].x, missile[1].y);
            }
        }
    }

    private void showBombs(Canvas canvas) {
        for (Bomb bomb : bombs) {
            drawBitmapXY(canvas, bomb.getImgWithType(), bomb.x, bomb.y);
        }
    }

    private void showItems(Canvas canvas) {
        for (Item item : items) {
            drawBitmapXY(canvas, item.getImg(), item.x, item.y);
        }
    }

    private void showExplode(Canvas canvas) {
        for (Explode explode : explodes) {
            drawBitmapXY(canvas, explode.getImg(), explode.x, explode.y);
        }
    }

    private void showBullet(Canvas canvas) {
        for (Bullet bullet : bullets) {
            if (bullet.type >= 21) {
                drawBitmapXY(canvas, ResInit.bulletImage[59], bullet.x, bullet.y);
            } else {
                drawBitmapXY(canvas, bullet.getImg(), bullet.x, bullet.y);
            }
        }

        for (PlayerBullet bullet : playerBullets) {
            drawBitmapXY(canvas, bullet.getImg(), bullet.x, bullet.y);
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
        if (this.mIsGameFinished) {
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

        if (this.mIsMissionComplete) {
            this.gTempDelay = (this.gTempDelay + 1);
            if (this.gTempDelay > 90 && !this.mIsGameFinished) {
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
                    this.mIsGameFinished = true;
                }
                return;
            }

            this.mMission = (this.mMission + 1);
            this.gScreenMove = 0;
            this.gTempDelay = 0;
            this.mIsMissionComplete = false;
            stageInit();
        }
    }

    private void showActiveScore(Canvas canvas) {
        int textSize = UiUtils.dp2px(context, 20);
        mPaint.setTextSize(textSize);
        mPaint.setColor(Color.BLUE);
        float textWidth = mPaint.measureText(int2string(this.mGameScore));
        canvas.drawText(int2string(this.mGameScore), MainWindow.windowWidth - textWidth, textSize + 30, mPaint);
    }

    private void showActiveBomb(Canvas canvas) {
        if (this.gIsGameOver) {
            return;
        }
        for (int i = 0; i < mPlayer.bombTypes.size(); i++) {
            int x = (27 + i * 45);
            drawBitmapXY(canvas, ResInit.bombIcon[mPlayer.bombTypes.get(i) - 1], x, 120);
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

    public int getPlayerCenterX() {

        return mPlayer.x + mPlayer.width / 2;
    }

    public int getPlayerCenterY() {
        return mPlayer.y + mPlayer.height / 2;
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
        if (mPlayer == null || mIsMissionComplete) {
            return;
        }
        mPlayer.handleSpeed(xPercent, yPercent);
    }

    @Override
    public void playBomb() {
        if (mPlayer == null || mPlayer.state != 0 || mIsGameFinished) {
            return;
        }
        //
        if (mPlayer.bombTypes.size() == 0 || bombs.size() > 2) {
            return;
        }
        //当前没有炸弹,或者是有无敌护盾的时候也可释放
        if (bombs.size() == 0 || (bombs.size() == 1 && bombs.get(0).type == 4)) {
            int lastBombType = mPlayer.bombTypes.get(mPlayer.bombTypes.size() - 1);
            if (lastBombType == 3) {
                Bomb[] b = Bomb.makeBomb3(3, mPlayer);
                bombs.add(b[0]);
                bombs.add(b[1]);
            } else {
                bombs.add(Bomb.makeBomb(lastBombType, mPlayer));
            }
            mPlayer.bombTypes.remove(mPlayer.bombTypes.size() - 1);
        }
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
