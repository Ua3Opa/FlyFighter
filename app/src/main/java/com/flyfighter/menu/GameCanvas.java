package com.flyfighter.menu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.flyfighter.entity.Bomb;
import com.flyfighter.entity.Boss;
import com.flyfighter.entity.Bullet;
import com.flyfighter.entity.EnemyPlane;
import com.flyfighter.entity.Explode;
import com.flyfighter.entity.Item;
import com.flyfighter.entity.Laser;
import com.flyfighter.entity.Missile;
import com.flyfighter.entity.PlayerBullet;
import com.flyfighter.entity.PlayerPlane;
import com.flyfighter.entity.Spirit;
import com.flyfighter.interf.Controller;
import com.flyfighter.res.RMS;
import com.flyfighter.res.ResInit;
import com.flyfighter.res.SoundHelper;
import com.flyfighter.room.PlayRecord;
import com.flyfighter.utils.MathHelper;
import com.flyfighter.utils.UiUtils;
import com.flyfighter.view.MainWindow;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameCanvas extends SurfaceView implements SurfaceHolder.Callback, Runnable, Controller {

    private Context context;
    private MediaPlayer[] mediaPlayers = new MediaPlayer[13];
    private SoundHelper soundHelper = new SoundHelper();
    private int playerType;
    private int playerLife = 3;
    private PlayerPlane mPlayer;
    private List<EnemyPlane> enemys = new ArrayList<>();
    private Random random;

    private final int maxEnemy = 4;
    private final int maxItem = 3;
    public volatile boolean isThreadAlive = false;
    private boolean gIsLoadGame = true;

    private int mContinueNum = 0;
    private int mPlayerPower;
    private boolean mIsGameFinished;
    private int mMission;
    private int mStage;
    private int mGameScore;

    private int mMissileType;
    private int mMissileMax;
    private long mMissileShootTime;

    private int gGameSecretNum;
    public boolean mGamePause;
    private boolean mResLoaded;
    //无敌时间为5秒
    private long shieldMaxMills = 5 * 1000;
    private long bombMaxMills = 4 * 1000;
    private boolean mIsMissionComplete;
    private int gTempDelay;
    private boolean mIsEnableEnemy;
    private boolean mIsGameOver;
    private boolean mIsBossAppear;
    private int gScreenMove;
    private int gBackgroundOffset;
    private int gEnemyCount;
    private int mDestroyCount;
    private int gApearEnemyType;
    private int mDifficulty;
    private int mBackgroundHeight;
    public List<Bullet> bullets = new ArrayList<>();
    public List<Laser> lasers = new ArrayList<>();
    public List<PlayerBullet> playerBullets = new ArrayList<>();
    public List<Explode> explodes = new ArrayList<>();
    public List<Item> items = new ArrayList<>();
    public List<Bomb> bombs = new ArrayList<>();
    public List<Missile[]> missiles = new ArrayList<>();
    public Boss boss;
    public static final int[][] stageEnemy = new int[][]{
            {1, 2, 4, 38, 44, 19, 39, 22, 45, 26, 46, 25, 43, 36, 40},
            {5, 2, 4, 24, 29, 27, 46, 21, 23, 39, 45, 30, 28, 33, 35},
            {6, 4, 3, 43, 20, 39, 23, 31, 45, 34, 38, 46, 37, 32, 41},
            {19, 45, 20, 46, 19, 45, 20, 46, 19, 45, 20, 46, 19, 45, 20},
            {46, 44, 43, 22, 20, 39, 23, 31, 45, 34, 38, 46, 37, 32, 41}
    };


    private static final byte[] gGetItemsList = new byte[]{5, 5, 5, 6, 6, 6, 7, 7, 8, 1, 2, 3, 4, 12, 9, 9, 10, 10, 11, 11};

    public static final int[] bulletPic = new int[]{2, 3, 3, 1, 4, 2, 2, 2, 4, 4, 4, 4, 4, 4, 4, 2, 2, 2, 2, 2, 2};

    private static final int[][][] bossBullet = new int[][][]{
            {{13, 3, 17, 35}, {3, 2, 11, 35}, {13, 4, 20, 45}, {21, 6, 21, 33}, {2, 4, 10, 28}, {13, 6, 6, 36}, {3, 3, 19, 44}, {16, 6, 16, 28}},
            {{3, 2, 18, 36}, {22, 6, 21, 25}, {13, 4, 17, 34}, {2, 4, 20, 46}, {16, 6, 12, 38}, {21, 6, 21, 28}, {3, 3, 19, 44}, {12, 5, 17, 26}},
            {{13, 3, 19, 37}, {3, 2, 13, 33}, {16, 6, 10, 45}, {22, 6, 21, 28}, {3, 3, 16, 48}, {13, 4, 20, 34}, {16, 6, 9, 46}, {3, 2, 13, 28}},
            {{3, 2, 16, 30}, {13, 3, 20, 30}, {3, 3, 11, 40}, {22, 6, 21, 20}, {16, 6, 10, 35}, {13, 4, 6, 30}, {3, 3, 19, 45}, {12, 5, 17, 20}},
            {{3, 4, 16, 20}, {13, 6, 20, 25}, {3, 4, 11, 30}, {23, 6, 21, 20}, {16, 6, 10, 35}, {13, 5, 6, 25}, {22, 5, 19, 20}, {12, 6, 17, 25}}};


    private static final int[][][] bossShootPoint = new int[][][]
            {{{-180, -20}, {0, 0}, {180, -20}, {-90, -20}, {0, 0}, {90, -20}},
                    {{-190, -80}, {0, -15}, {190, -80}, {-110, -80}, {0, -15}, {110, -80}},
                    {{-228, -60}, {0, 0}, {228, -60}, {-133, -60}, {0, 0}, {133, -60}},
                    {{-210, -60}, {0, -20}, {210, -60}, {-88, -60}, {0, -20}, {88, -60}},
                    {{-230, -32}, {0, -12}, {230, -32}, {-144, -32}, {90, -12}, {144, -32}}};

    public static final int[][] playerBulletData = new int[][]
            // 第二位改成飞机中心偏移值,新增第六位图片的index
            {{0, 0, -2, 0, -23, 0},
                    {1, -24, 0, 0, -20, 0}, {1, 24, 0, 0, -20, 0},
                    {1, -24, 0, -1, -20, 1}, {0, 0, -7, 0, -25, 0}, {1, 24, 0, 1, -20, 1},
                    {1, -32, 0, -1, -20, 1}, {0, -24, -6, 0, -25, 0}, {0, 24, -6, 0, -25, 0}, {1, 32, 0, 1, -20, 1},
                    {0, -38, 0, -2, -23, 0}, {1, -24, -2, -1, -24, 1}, {0, 0, 0, 0, -28, 0}, {1, 24, -2, 1, -24, 1}, {0, 38, 0, 2, -23, 0},

                    {1, 0, 0, 0, -23, 1},
                    {1, -26, 0, 0, -23, 1}, {1, 26, 0, 0, -23, 1},
                    {0, -26, 0, -1, -23, 0}, {1, 0, -6, 0, -30, 1}, {26, 15, 0, 1, -23, 2},
                    {0, -34, 0, -1, -23, 0}, {1, -26, 0, 0, -30, 1}, {1, 26, 0, 0, -30, 1}, {2, 34, 0, 1, -23, 2},
                    {0, -40, -2, -1, -23, 0}, {0, -26, 0, 0, -27, 0}, {1, 0, -4, 0, -33, 1}, {2, 26, 0, 0, -27, 2}, {2, 40, -2, 1, -23, 2},

                    {1, 0, -5, 0, -23, 1},
                    {2, -34, 0, 0, -23, 0}, {2, 34, 0, 0, -23, 0},
                    {2, -44, 1, 0, -23, 1}, {1, 0, -5, 0, -30, 0}, {2, 44, 1, 0, -23, 1},
                    {2, -50, 1, 0, -23, 1}, {1, -20, -5, 0, -30, 0}, {1, 20, -5, 0, -30, 0}, {2, 50, 1, 0, -23, 1},
                    {2, -55, -2, 0, -23, 1}, {1, -25, -5, 0, -30, 2}, {1, 0, -2, 0, -30, 0}, {1, 25, -5, 0, -30, 2}, {2, 55, -2, 0, -23, 1}};

    private static final int[] scoreLevel = new int[]{5000, 20000, 50000, 120000, 200000, 300000, 350000, 400000, 450000, 500000, 600000, 650000, 700000, 750000, 800000, 850000, 900000, 950000, 1000000, 1050000, 1100000, 1150000, 1200000, 1250000, 1300000};

    public static final int[] bulletSpeedSL = new int[]{-2, 4, -2, 8, -1, 8, -1, 12, 0, 12, 1, 12, 1, 8, 2, 8, 2, 4};

    private static final byte[] bulletSpeedSG = new byte[]{-1, 12, 0, 12, 1, 12};

    public static final int[][] playerBulletFloorPower = new int[][]{{6, 6, 5, 4, 4}, {13, 11, 8, 7, 6}, {11, 6, 6, 6, 5}};

    public static final byte[] enemyBulletToFront = new byte[]{0, 12, -2, 12, 2, 12, -2, 12, 0, 12, 2, 12, -4, 9, -2, 12, 2, 12, 4, 9, -4, 9, -2, 12, 0, 12, 2, 12, 4, 9, -4, 9, -2, 12, 0, 12, 2, 12, 4, 9, 0, 18};

    private static final int[][] playerBulletFloorNum = new int[][]{{1, 2, 3, 4, 5, 0}, {1, 2, 3, 4, 5, 15}, {1, 2, 3, 4, 5, 30}};

    private EnemyPlane[] gEnemy = new EnemyPlane[5];
    private Paint mPaint = new Paint();
    private final SurfaceHolder surfaceHolder;

    public GameCanvas(Context context, int playerType) {
        super(context, null);
        this.context = context;

        mPaint.setAntiAlias(true);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        this.playerType = playerType;
        this.mDifficulty = RMS.difficulty;

        this.random = new Random();

        gameInit();
        stageInit();
        mResLoaded = true;
    }

    private void gameInit() {
        ResInit.loadPlayer(context, playerType);
        soundHelper.loadGameSounds(context, mediaPlayers);

        playSound(12);
        this.mPlayer = PlayerPlane.createPlayerPlane(playerType);
        this.mPlayerPower = mPlayer.power;
        this.mIsGameFinished = false;
        this.mGameScore = 0;
        this.mMissileType = 0;
        this.mMissileMax = 0;
        this.gGameSecretNum = -1;
        this.mMission = 1;
        this.mStage = 1;
    }

    private void stageInit() {
        mPlayer.resetState();
        mIsMissionComplete = false;
        gTempDelay = 0;
        mIsEnableEnemy = false;
        mIsGameOver = false;
        mIsBossAppear = false;
        gScreenMove = 1;
        gBackgroundOffset = 0;
        gEnemyCount = 0;
        gApearEnemyType = 1;
        //0 : 1,2,4,3,5
        //1 : 2,3,4,1,5
        //2 : 3,1,4,2,5
        if (this.mStage == 1) {
            this.mMission = playerType + 1;
        } else if (this.mStage == 2) {
            if (this.playerType == 0) {
                this.mMission = 2;
            } else if (this.playerType == 1) {
                this.mMission = 3;
            } else {
                this.mMission = 1;
            }
        } else if (this.mStage == 3) {
            this.mMission = 4;
        } else if (this.mStage == 4) {
            if (this.playerType == 0) {
                this.mMission = 3;
            } else if (this.playerType == 1) {
                this.mMission = 1;
            } else {
                this.mMission = 2;
            }
        } else {
            this.mMission = 5;
        }

        ResInit.loadStagePic(context, mMission);
        this.mBackgroundHeight = ResInit.BackgroundImage[mMission - 1].getHeight();
    }

    private void driveObjects() {
        gScreenMove++;
        makeEnemy();
        makeBoss();
        dealEnemyState();

        dealBossState();
        dealPlayerState();

        dealBullet();
        dealMissile();
        dealExplodes();
        dealItems();
        dealShield();
        dealBomb();
    }

    private void dealBossState() {
        if (boss == null) {
            return;
        }
        if (boss.health <= 0) {
            Explode explode0 = Explode.dealExplodeState(boss, getRand(5) + 3);
            explode0.x = boss.x - boss.width / 2 + explode0.width;
            explode0.y = boss.y - boss.height / 2 - explode0.height;
            explodes.add(explode0);

            Explode explode1 = Explode.dealExplodeState(boss, getRand(5) + 3);
            explode1.x = boss.x;
            explode1.y = boss.y;
            explodes.add(explode1);

            Explode explode2 = Explode.dealExplodeState(boss, getRand(5) + 3);
            explode2.x = boss.x;
            explode2.y = boss.y + boss.height - explode2.height;
            explodes.add(explode2);


            Explode explode3 = Explode.dealExplodeState(boss, getRand(5) + 3);
            explode3.x = boss.x + boss.width - explode3.width;
            explode3.y = boss.y;
            explodes.add(explode3);

            Explode explode4 = Explode.dealExplodeState(boss, getRand(5) + 3);
            explode4.x = boss.x + boss.width - explode4.width;
            explode4.y = boss.y + boss.height - explode4.height;
            explodes.add(explode4);
            mGameScore += boss.reward;

            this.playSound(2);
            mIsBossAppear = false;
            mIsMissionComplete = true;
            mPlayer.onFire = false;
            bullets.clear();
            boss = null;
        } else {
            boss.dealMoveState();
            checkHitBoss(boss);
            makeBossBullet();
        }
    }

    private void makeBossBullet() {
        int bossType = EnemyPlane.enemyData[boss.type][0] - 200 - 1;
        if (boss.bulletSequence >= 8) {
            boss.bulletSequence = 0;
        }

        if (System.currentTimeMillis() - boss.lastShootTime <= (bossBullet[bossType][boss.bulletSequence][3] * 1.0f / 25 * 1000 * 2)) {
            return;
        }
        int[][] shootPoint = new int[3][2];

        if (bossBullet[bossType][boss.bulletSequence][0] < 20) {
            for (int j = 0; j < 3; ++j) {
                for (int i = 0; i < 2; ++i) {
                    shootPoint[j][i] = bossShootPoint[bossType][j][i];
                }
            }
        } else {
            for (int j = 0; j < 3; ++j) {
                for (int i = 0; i < 2; ++i) {
                    shootPoint[j][i] = bossShootPoint[bossType][j + 3][i];
                }
            }
        }
        int[] bb = bossBullet[bossType][boss.bulletSequence];
        int shootType = bb[0];
        switch (shootType) {
            case 1:
            case 2:
            case 3:
                //多发散射,每个射击点射出多发
                if (shootType == 1) {
                    this.makeBossBullet_S(bossType, shootPoint[1]);
                } else if (shootType == 2) {
                    this.makeBossBullet_S(bossType, shootPoint[0]);
                    this.makeBossBullet_S(bossType, shootPoint[2]);
                } else {
                    this.makeBossBullet_S(bossType, shootPoint[0]);
                    this.makeBossBullet_S(bossType, shootPoint[1]);
                    this.makeBossBullet_S(bossType, shootPoint[2]);
                }
                boss.bulletSequence++;
                boss.lastShootTime = System.currentTimeMillis();
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 17:
            case 18:
            case 19:
            case 11:
            case 12:
            case 13://bulletSpeedSL内部随机方向,并且多次发射
                if (boss.bulletNum < bb[1]) {
                    if ((System.currentTimeMillis() - boss.shootDuration) >= 200) {
                        if (shootType == 11) {
                            this.makeBossBullet_SL(bossType, shootPoint[1]);
                        } else if (shootType == 12) {
                            this.makeBossBullet_SL(bossType, shootPoint[0]);
                            this.makeBossBullet_SL(bossType, shootPoint[2]);
                        } else {
                            this.makeBossBullet_SL(bossType, shootPoint[0]);
                            this.makeBossBullet_SL(bossType, shootPoint[1]);
                            this.makeBossBullet_SL(bossType, shootPoint[2]);
                        }
                        ++boss.bulletNum;
                        boss.shootDuration = System.currentTimeMillis();
                    }
                } else {
                    boss.bulletNum = 0;
                    boss.bulletSequence++;
                    boss.lastShootTime = System.currentTimeMillis();
                }
                break;
            case 14:
            case 15:
            case 16://三发齐射 左中右
                if (boss.bulletNum < bb[1]) {
                    if ((System.currentTimeMillis() - boss.shootDuration) >= 200) {
                        if (shootType == 14) {
                            makeBossBullet_SG(bossType, boss.bulletDirect1, shootPoint[2]);
                        } else if (shootType == 15) {
                            makeBossBullet_SG(bossType, boss.bulletDirect1, shootPoint[0]);
                            makeBossBullet_SG(bossType, boss.bulletDirect2, shootPoint[1]);
                        } else {
                            makeBossBullet_SG(bossType, boss.bulletDirect1, shootPoint[0]);
                            makeBossBullet_SG(bossType, boss.bulletDirect2, shootPoint[1]);
                            makeBossBullet_SG(bossType, boss.bulletDirect3, shootPoint[2]);
                        }
                        boss.bulletNum++;
                        boss.shootDuration = System.currentTimeMillis();
                    }
                } else {
                    boss.bulletDirect1 = this.getRand(3);
                    if (shootType >= 15) {
                        boss.bulletDirect2 = this.getRand(3);
                    }
                    if (shootType >= 16) {
                        boss.bulletDirect3 = this.getRand(3);
                    }
                    boss.bulletNum = 0;
                    ++boss.bulletSequence;
                    boss.lastShootTime = System.currentTimeMillis();
                }
                break;
            case 21://激光
            case 22:
                if (!lasers.isEmpty()) {
                    return;
                }
                if (shootType == 21) {
                    makeBulletLaser(bossType, shootPoint[1]);
                } else {
                    makeBulletLaser(bossType, shootPoint[0]);
                    makeBulletLaser(bossType, shootPoint[2]);
                }
                ++boss.bulletSequence;
                break;
            case 23:
            default:
                makeBossMissile(bossType);
                ++boss.bulletSequence;
                break;
        }
    }

    private void makeBossMissile(int bossType) {
        int[][] sp = new int[2][2];
        sp[0] = bossShootPoint[bossType][3];
        sp[1] = bossShootPoint[bossType][5];//boss只会有导弹
        missiles.add(Missile.makeMissiles(1, boss.x + boss.width / 2, boss.y + boss.height, sp, 1));
    }

    private void makeBulletLaser(int bossType, int[] shootPoint) {
        Laser laser = Laser.mallocLaser(bossType, boss.x + boss.width / 2, boss.y + boss.height, shootPoint);
        lasers.add(laser);
    }

    /**
     * 三发齐射 左中右
     */
    private void makeBossBullet_SG(int bossType, int direction, int[] shootPoint) {
        int[] bb = bossBullet[bossType][boss.bulletSequence];
        boss.bulletType = bb[2] - 2;
        shootPoint[1] += boss.height;
        Bullet bullet = makeEnemyBullet(boss, shootPoint);
        bullet.speedX = bulletSpeedSG[direction * 2];
        bullet.speedY = bulletSpeedSG[direction * 2 + 1];
        bullets.add(bullet);
    }

    private void makeBossBullet_SL(int bossType, int[] shootPoint) {
        int[] bb = bossBullet[bossType][boss.bulletSequence];
        boss.bulletType = bb[2] - 2;
        shootPoint[1] += boss.height;
        Bullet bullet = makeEnemyBullet(boss, shootPoint);

        int rand = getRand(9);
        bullet.speedX = bulletSpeedSL[rand * 2];
        bullet.speedY = bulletSpeedSL[rand * 2 + 1];
        bullets.add(bullet);
    }

    /**
     * 多发散射,每个射击点射出多发
     *
     * @param bossType
     * @param shootPoint
     */
    private void makeBossBullet_S(int bossType, int[] shootPoint) {
        int[] bb = bossBullet[bossType][boss.bulletSequence];
        boss.bulletType = bb[2] - 2;
        shootPoint[1] += boss.height;
        makeBulletToFront(boss, shootPoint, bb[1]);
    }

    private void checkHitBoss(Boss boss) {
        for (int i = playerBullets.size() - 1; i >= 0; i--) {
            PlayerBullet bullet = playerBullets.get(i);
            if (checkIfHit(bullet.x, bullet.y, bullet.width, bullet.height, boss.x, boss.y, boss.width, boss.height)) {
                playerBullets.remove(bullet);
                boss.health -= playerBulletFloorPower[playerType][mPlayerPower - 1] - 1;
                explodes.add(Explode.dealExplodeState(bullet, getRand(2)));
                mGameScore += boss.reward;
            }
        }
    }

    private void makeBoss() {
        if (!mIsBossAppear) {
            return;
        }
        if (!enemys.isEmpty()) {
            for (EnemyPlane enemy : enemys) {
                enemy.pauseDelay = 0;
            }
        } else if (boss == null) {
            boss = Boss.makeBossByMission(mMission, mDifficulty);
        }
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
        if (bomb.type == 1 && (System.currentTimeMillis() - bomb.createTime > bombMaxMills)) {
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
        if (mPlayer == null) {
            return;
        }
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
            } else if (checkBulletHitEnemy(ms)) {//或者是boss的导弹hit player
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
        if (mMissileType == 2) {
            playSound(9);
        } else {
            playSound(10);
        }
    }

    private void checkMissileOutScreen(Missile[] ms) {
        if (ms[0] != null && outScreen(ms[0].x, ms[0].y, ms[0].firstFrame())) {
            ms[0] = null;
        }
        if (ms[1] != null && outScreen(ms[1].x, ms[1].y, ms[1].firstFrame())) {
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
                if (System.currentTimeMillis() - bomb.createTime >= shieldMaxMills) {
                    bombs.remove(bomb);
                    return;
                } else {
                    bomb.dealMoveState(mPlayer);
                }
            }
        }
    }

    private void dealItems() {
        for (int i = items.size() - 1; i >= 0; i--) {
            Item item = items.get(i);
            item.dealMoveState();
            if (checkHitPlayer(item)) {
                handleGetItems(item);
                items.remove(item);
            } else if (outScreen(item.x, item.y, item.firstFrame())) {
                items.remove(item);
            }
        }
    }


    private void dealExplodes() {
        for (int i = explodes.size() - 1; i >= 0; i--) {
            if (explodes.get(i).frameIndex >= explodes.get(i).picNum) {
                explodes.remove(explodes.get(i));
            }
        }
    }

    private void dealBullet() {
        for (int i = bullets.size() - 1; i >= 0; i--) {
            bullets.get(i).dealMoveState();
            if (outScreen(bullets.get(i).x, bullets.get(i).y, bullets.get(i).firstFrame())) {
                bullets.remove(bullets.get(i));
            } else if (checkHitPlayer(bullets.get(i))) {
                onPlayerDestroyed();
                bullets.remove(bullets.get(i));
            }
        }

        for (int i = playerBullets.size() - 1; i >= 0; i--) {
            PlayerBullet playerBullet = playerBullets.get(i);
            playerBullet.dealMoveState();

            if (outScreen(playerBullet.x, playerBullet.y, playerBullet.firstFrame())) {
                playerBullets.remove(playerBullet);
            } else if (checkBulletHitEnemy(playerBullet)) {
                explodes.add(Explode.dealExplodeState(playerBullet, getRand(2)));
                mGameScore += 28;
                playerBullets.remove(playerBullet);
            }
        }

        for (int i = lasers.size() - 1; i >= 0; i--) {
            Laser laser = lasers.get(i);
            if (boss == null) {
                lasers.remove(laser);
                break;
            }
            laser.dealMoveState(boss);

            if (System.currentTimeMillis() - laser.createTime >= 1000) {
                lasers.remove(laser);
            } else if (checkHitPlayer(laser)) {
                onPlayerDestroyed();
            }
        }
    }

    private void onPlayerDestroyed() {
        explodes.add(Explode.dealExplodeState(mPlayer.x, mPlayer.y, getRand(5) + 3));
        playerLife--;
        this.playSound(playerType);
        mPlayer = null;
        if (playerLife <= 0) {
            mIsGameOver = true;
            gTempDelay = 0;
            return;
        }
        mPlayer = PlayerPlane.createPlayerPlane(playerType);
        this.mPlayerPower = mPlayer.power;
    }

    private boolean checkBulletHitEnemy(Missile[] ms) {
        for (EnemyPlane enemy : enemys) {
            if (ms[0] != null && checkIfHit(ms[0].x, ms[0].y, ms[0].width, ms[0].height, enemy.x, enemy.y, enemy.width, enemy.height)) {
                if (ms[0].from == 0) {
                    enemy.health -= ms[0].power;
                    mGameScore += 28;
                    ms[0] = null;
                } else {
                    onPlayerDestroyed();
                }
            }
            if (ms[1] != null && checkIfHit(ms[1].x, ms[1].y, ms[1].width, ms[1].height, enemy.x, enemy.y, enemy.width, enemy.height)) {
                if (ms[1].from == 0) {
                    enemy.health -= ms[1].power;
                    mGameScore += 28;
                    ms[1] = null;
                } else {
                    onPlayerDestroyed();
                }
            }
        }
        return false;
    }

    private boolean checkBulletHitEnemy(PlayerBullet bullet) {
        for (EnemyPlane enemy : enemys) {
            if (checkHitWithCross(bullet, enemy)) {
                //{6, 6, 5, 4, 4}, {13, 11, 8, 7, 6}, {11, 6, 6, 6, 5}
                enemy.health -= playerBulletFloorPower[playerType][mPlayerPower - 1] - 1;
                return true;
            }
        }
        return false;
    }

    /**
     * @param collider 碰撞体,bullet
     * @param trigger  触发器.飞机
     * @return
     */
    public boolean checkHitWithCross(Spirit collider, Spirit trigger) {
        if (collider.perX == 0) {
            collider.perX = collider.x;
        }
        if (collider.perY == 0) {
            collider.perY = collider.y;
        }
        int[][] rect = trigger.buildRect();
        int[] cp = new int[]{
                collider.x + collider.width / 2, collider.y + collider.height / 2,
                collider.perX + collider.width / 2, collider.perY + collider.height / 2,
        };
        for (int i = 0; i < rect.length; i++) {
            int[] point = rect[i];
            int[] nextPoint = rect[i == rect.length - 1 ? 0 : i + 1];
            if (MathHelper.crossLine(point[0], point[1], nextPoint[0], nextPoint[1], cp[0], cp[1], cp[2], cp[3])) {
                return true;
            }
        }
        return false;
    }


    private boolean checkHitPlayer(Item item) {
        if (mPlayer == null || mPlayer.state == -1) {
            return false;
        }
        if (checkIfHit(item.x, item.y, item.width, item.height, mPlayer.x, mPlayer.y, mPlayer.width, mPlayer.height)) {
            return true;
        }
        return false;
    }

    private boolean checkHitPlayer(Spirit spirit) {
        if (mPlayer == null || mPlayer.state == -1 || mPlayer.state == 2) {
            return false;
        }
        if (checkIfHit(spirit.x, spirit.y, spirit.width, spirit.height, mPlayer.x, mPlayer.y, mPlayer.width, mPlayer.height)) {
            return true;
        }
        return false;
    }

    private void dealEnemyState() {
        for (int i = enemys.size() - 1; i >= 0; i--) {
            if (enemys.get(i).health <= 0) {
                //3,4,5,6,7
                explodes.add(Explode.dealExplodeState(enemys.get(i), getRand(4) + 2));
                this.playSound(2);
                makeGoods(enemys.get(i));
                mGameScore += enemys.get(i).reward;
                enemys.remove(i);
                mDestroyCount++;
            } else {
                enemys.get(i).dealMoveState();

                if (mallocBullet()) {//
                    makeEnemyBullet(enemys.get(i), mPlayer);
                }
                if (outScreen(enemys.get(i).x, enemys.get(i).y, enemys.get(i).firstFrame())) {
                    enemys.remove(enemys.get(i));
                }
            }
        }
    }

    public void makeEnemyBullet(EnemyPlane enemy, PlayerPlane player) {
        if (System.currentTimeMillis() - enemy.shootTime <= enemy.fireDelay) {
            return;
        }
        enemy.shootTime = System.currentTimeMillis();
        int bulletNum = enemy.bulletMax;
        int rand = getRand(3);
        if (bulletNum < 3 || rand == 0) {
            if (this.getRand(4 - mMission - mDifficulty) < 0) {
                bulletNum = (1 + this.getRand(enemy.bulletMax));
            }
            int ranVal = this.getRand(4 - mDifficulty);
            Bullet bullet = makeEnemyBullet(enemy);
            //makeBulletToPlayer(enemy, bullet, bulletNum);
            if (ranVal == 0) {
                makeBulletToPlayer(enemy, bullet, bulletNum);
            } else {
                makeBulletToFront(enemy, bulletNum);
            }
        }
    }

    private void handleGetItems(Item item) {
        playSound(4);
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
                if (mPlayerPower < 5) {
                    mPlayerPower++;
                }
            case 8:
                mPlayerPower = 5;
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
                if (playerLife < 3) {
                    playerLife++;
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

    private void makeBulletToPlayer(EnemyPlane enemy, Bullet bullet, int bulletNum) {
        makeBulletToPlayer(enemy, bullet, new int[]{0, 0}, bulletNum);
    }

    private void makeBulletToPlayer(EnemyPlane enemy, Bullet bullet, int[] shootPoint, int bulletNum) {
        bullet.speedX = 0;
        bullet.speedY = 15;
        bulletGoToPlayer(bullet, mPlayer);//初始化子弹xy轴速度
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

            bullets.add(bullet);

            if (bulletNum >= 2) {
                bullet.speedX = Bullet.bulletSpeedToPlayer[direction * 6];
                bullet.speedY = Bullet.bulletSpeedToPlayer[direction * 6 + 1];

                Bullet nb = makeEnemyBullet(enemy, shootPoint);
                nb.speedX = Bullet.bulletSpeedToPlayer[direction * 6 + 2];
                nb.speedY = Bullet.bulletSpeedToPlayer[direction * 6 + 3];
                bullets.add(nb);
            }
            if (bulletNum >= 3) {
                if (direction == 0) {
                    direction = 8;
                }
                Bullet nb = makeEnemyBullet(enemy, shootPoint);
                nb.speedX = Bullet.bulletSpeedToPlayer[direction * 6 - 2];
                nb.speedY = Bullet.bulletSpeedToPlayer[direction * 6 - 1];
                bullets.add(nb);
            }
            if (bulletNum >= 4) {
                if (direction == 8) {
                    direction = 0;
                }
                Bullet nb = makeEnemyBullet(enemy, shootPoint);
                nb.speedX = Bullet.bulletSpeedToPlayer[direction * 6 + 4];
                nb.speedY = Bullet.bulletSpeedToPlayer[direction * 6 + 5];
                bullets.add(nb);
            }
            if (bulletNum >= 5) {
                if (direction == 0) {
                    direction = 8;
                }
                Bullet nb = makeEnemyBullet(enemy, shootPoint);
                nb.speedX = Bullet.bulletSpeedToPlayer[direction * 6 - 4];
                nb.speedY = Bullet.bulletSpeedToPlayer[direction * 6 - 3];
                bullets.add(nb);
            }

            if (bulletNum >= 6) {
                Bullet nb = makeEnemyBullet(enemy, shootPoint);
                nb.speedY = getRand(8) + 5;
                this.bulletGoToPlayer(nb, mPlayer);
            }
        }
    }

    private void bulletGoToPlayer(Bullet bullet, PlayerPlane player) {
        if (mPlayer == null) {
            return;
        }

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
        if (x > 50 || y > 50) {
            if (x > y) {
                // sy = 100  scale time
                // sx = 100
                // time = x / sy
                // sy = y / time;
                bullet.speedX = bullet.speedY;
                int scale = (x / Math.abs(bullet.speedY));
                if (scale <= 0)
                    scale = 1;
                bullet.speedY = (y / scale) * 4;
            } else {
                int scale = (y / Math.abs(bullet.speedY));
                if (scale <= 0)
                    scale = 1;
                bullet.speedX = (x / scale) * 4;
            }
            if (flag_x)
                bullet.speedX = -bullet.speedX;
            if (flag_y) {
                bullet.speedY = -bullet.speedY;
            }
        }
    }

    private Bullet makeEnemyBullet(EnemyPlane enemy) {
        int[] shootPoint = new int[]{0, 0};
        return makeEnemyBullet(enemy, shootPoint);
    }

    private Bullet makeEnemyBullet(EnemyPlane enemy, int[] shootPoint) {
        int shootX = enemy.x + enemy.width / 2 + shootPoint[0];
        int shootY = enemy.y + enemy.height - 15 + shootPoint[1];
        int bulletTypeNum = GameCanvas.bulletPic[enemy.bulletType];

        int picIndex = 0;
        for (int j = 0; j < enemy.bulletType; ++j) {//计算子弹图片的索引
            picIndex += GameCanvas.bulletPic[j];
        }

        Bitmap bitmap = ResInit.bulletImage[picIndex];

        Bullet bullet = Bullet.mallocBullet(enemy.bulletType, shootX, shootY, 0, 0, bulletTypeNum, bitmap);
        return bullet;
    }

    private void makeBulletToFront(EnemyPlane enemy, int[] shootPoint, int bulletNum) {
        int offSet = 0;
        for (int i = 0; i < bulletNum; ++i) {
            offSet = (offSet + i * 2);
        }
        for (int i = 0; i < bulletNum; ++i) {
            if (mallocBullet()) {
                Bullet bullet = makeEnemyBullet(enemy, shootPoint);
                bullet.speedX = enemyBulletToFront[offSet++];
                bullet.speedY = enemyBulletToFront[offSet++];
                if (bullet.speedX == 0 && bullet.speedY == 0) {
                    continue;
                }
                bullets.add(bullet);
            }
        }
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
        if (enemys.size() >= maxEnemy || !mIsEnableEnemy || mIsBossAppear) {
            return;
        }
        int randomType;
        int type;
        int ranVal;
        if (this.mMission <= 4) {
            randomType = this.getRand(gApearEnemyType % stageEnemy[this.mMission - 1].length);
            type = stageEnemy[this.mMission - 1][randomType] - 1;
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
        enemys.add(EnemyPlane.mallocEnemy(type, mDifficulty));
        gEnemyCount++;
        gApearEnemyType++;
        if (mDestroyCount >= 30 && gEnemyCount >= 150) {
//        if (mDestroyCount >= 10 && gEnemyCount >= 50) {
            playSound(3);
            this.mIsBossAppear = true;
            this.mIsEnableEnemy = false;
        }
    }

    private void dealPlayerState() {
        if (mIsGameOver || mPlayer == null) {
            return;
        }
        if (mPlayer.state == -1) {//进场
            mPlayer.y -= 5;
            if (mPlayer.y <= MainWindow.windowHeight - 350) {
                mPlayer.state = 0;
                mPlayer.onFire = true;
            }
        } else {//正常状态
            if (mIsMissionComplete) {
                return;
            }
            mPlayer.handleMove();
            if (playerBullets.isEmpty()) {
                makePlayerBullet();
            } else {
                PlayerBullet lastBullet = playerBullets.get(playerBullets.size() - 1);
                if (System.currentTimeMillis() - lastBullet.createTime <= 100) {
                    return;
                }
                makePlayerBullet();
            }
        }
    }

    long lastTime;

    private void makePlayerBullet() {
        playSound(playerType + 5);
        int bulletNum;
//        if (lastTime !=0 && System.currentTimeMillis() - lastTime >= 5000) {
//            player.power++;
//            player.power = player.power >= 5 ? 5 : player.power;
//            lastTime = System.currentTimeMillis();
//        }
//        if(lastTime==0){
//            lastTime = System.currentTimeMillis();
//        }
        if (1 == this.gGameSecretNum) {
            bulletNum = 4;
        } else {
            bulletNum = mPlayerPower - 1;
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
        if (items.size() >= maxItem || mPlayer == null) {
            return;
        }
        int itemType = 0;
        if (enemy.type <= 18) {
            if (getRand(10) == 0) {
                itemType = 5;
            } else if (this.mPlayerPower == 1 && getRand(10) == 1) {
                itemType = 7;
            }
        } else {
            if (this.mPlayerPower < 3 && getRand(4) == 0) {
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
        int mw = 0;
        int mh = 0;
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
        if (cx == 0 && cy == 0) {
            return true;
        } else {
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
        }
        return Math.abs(cx) < mw && Math.abs(cy) < mh;
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
        if (bitmapL == null || bitmapR == null) {
            return;
        }
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
                if (!mResLoaded) {
                    continue;
                }
                lockCanvas = surfaceHolder.lockCanvas();
                //清空画布
                lockCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
                if (!mGamePause) {
                    driveObjects();
                } else {//暂停状态
                    post(() -> ((MainWindow) getParent().getParent()).showPause(this));
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
        showBoss(canvas);
        showEnemy(canvas);
        showBombs(canvas);
        showPlayer(canvas);
        showItems(canvas);
        showBullet(canvas);
        showMissile(canvas);
        showExplode(canvas);
        showActiveLife(canvas);
        showActiveBomb(canvas);
        showActiveScore(canvas);
        showMissionInfo(canvas);
    }

    private void showBoss(Canvas canvas) {
        if (boss == null) {
            return;
        }
        drawBitmapXY(canvas, boss.getFrame(), boss.x, boss.y);
    }

    private void showMissile(Canvas canvas) {
        for (Missile[] missile : missiles) {
            if (missile[0] != null) {
                drawBitmapXY(canvas, missile[0].getFrame(), missile[0].x, missile[0].y);
            }
            if (missile[1] != null) {
                drawBitmapXY(canvas, missile[1].getFrame(), missile[1].x, missile[1].y);
            }
        }
    }

    private void showBombs(Canvas canvas) {
        for (Bomb bomb : bombs) {
            drawBitmapXY(canvas, bomb.getFrame(), bomb.x, bomb.y);
        }
    }

    private void showItems(Canvas canvas) {
        for (Item item : items) {
            drawBitmapXY(canvas, item.getFrame(), item.x, item.y);
        }
    }

    private void showExplode(Canvas canvas) {
        for (Explode explode : explodes) {
            drawBitmapXY(canvas, explode.getFrame(), explode.x, explode.y);
        }
    }

    private void showBullet(Canvas canvas) {
        for (Bullet bullet : bullets) {
            if (bullet.type >= 21) {
                drawBitmapXY(canvas, ResInit.bulletImage[59], bullet.x, bullet.y);
            } else {
                drawBitmapXY(canvas, bullet.getFrame(), bullet.x, bullet.y);
            }
        }

        for (PlayerBullet bullet : playerBullets) {
            drawBitmapXY(canvas, bullet.getFrame(), bullet.x, bullet.y);
        }

        for (Laser laser : lasers) {
            drawBitmapXY(canvas, laser.getFrame(), laser.x, laser.y);
        }
    }

    private void showEnemy(Canvas canvas) {
        for (EnemyPlane enemy : enemys) {
            drawBitmapXY(canvas, enemy.getFrame(), enemy.x, enemy.y);
        }
    }

    private void showGameOver() {
        if (this.mIsGameFinished) {
            this.isThreadAlive = false;
            RMS.playRecords.add(new PlayRecord(getScoreLevel(), playerType, mGameScore));
            Collections.sort(RMS.playRecords, (o1, o2) -> o2.score - o1.score);
            if (RMS.playRecords.size() >= 10) {
                RMS.playRecords = RMS.playRecords.subList(0, 10);
            }
            MMKV.defaultMMKV().encode("Records", JSON.toJSONString(RMS.playRecords));
            post(() -> ((MainWindow) (getParent().getParent())).showRanking());
        }
    }

    boolean continueWindowShow;

    private void showMissionInfo(Canvas canvas) {
        if (mIsGameOver && !mIsGameFinished) {
            gTempDelay++;
            if (mContinueNum == 0) {
                this.mIsGameFinished = true;
                showGameOver();
            } else {
                post(() -> {
                    ((MainWindow) getParent().getParent()).showContinue(this, mContinueNum);
                });
            }
            return;
        }

        if (this.gScreenMove < 310 && this.gScreenMove >= 45) {
            if (this.gScreenMove < 250) {
                if (this.mMission == 5) {
                    drawBitmapCenter(canvas, ResInit.otherImage[10]);
                } else {
                    drawBitmapCenterHorizontal(canvas, ResInit.otherImage[9], ResInit.numberImageValue[0][mStage], 100);
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
            //按60帧算的话这里应该是6秒左右
            if (gTempDelay > 400) {
                if (mMission >= 5) {
                    this.mIsGameFinished = true;
                    showGameOver();
                    return;
                }
                this.mMission++;
                this.mStage++;
                this.gScreenMove = 0;
                this.gTempDelay = 0;
                this.mIsMissionComplete = false;
                stageInit();
            }
        }
    }

    private void showActiveScore(Canvas canvas) {
        int textSize = UiUtils.dp2px(context, 20);
        mPaint.setTextSize(textSize);
        mPaint.setColor(Color.WHITE);
        float textWidth = mPaint.measureText(int2string(this.mGameScore));
        canvas.drawText(int2string(this.mGameScore), MainWindow.windowWidth - textWidth, textSize + 30, mPaint);
    }

    private void showActiveBomb(Canvas canvas) {
        if (this.mIsGameOver || mPlayer == null) {
            return;
        }
        for (int i = 0; i < mPlayer.bombTypes.size(); i++) {
            int x = (27 + i * 45);
            drawBitmapXY(canvas, ResInit.bombIcon[mPlayer.bombTypes.get(i) - 1], x, 120);
        }
    }

    private void showActiveLife(Canvas canvas) {
        for (int i = 0; i < playerLife - 1; i = (i + 1)) {
            int x = (27 + i * 45);
            drawBitmapXY(canvas, ResInit.lifeIcon[this.playerType], x, 50);
        }
    }

    private void showPlayer(Canvas canvas) {
        if (this.mIsGameOver || mPlayer == null) {
            return;
        }
        drawBitmapXY(canvas, mPlayer.getFrame(), mPlayer.x, mPlayer.y);
        if (mPlayer.onFire && mPlayer.getFireFrame() != null) {
            drawBitmapXY(canvas, mPlayer.getFireFrame(), mPlayer.x, mPlayer.y);
        }
    }

    private void showBackScreen(Canvas canvas) {
        int offsetY = -(this.mBackgroundHeight - MainWindow.windowHeight - this.gBackgroundOffset);
        if (offsetY >= 0) {
            drawBitmapXY(canvas, ResInit.BackgroundImage[this.mMission - 1], 0, -(this.mBackgroundHeight - offsetY));
        }
        drawBitmapXY(canvas, ResInit.BackgroundImage[this.mMission - 1], 0, offsetY);

        if (mGamePause) {
            return;
        }
        if (offsetY >= MainWindow.windowHeight) {
            this.gBackgroundOffset = 0;
        }
        if (this.mIsBossAppear) {
            this.gBackgroundOffset = (this.gBackgroundOffset + 5);
        } else {
            this.gBackgroundOffset = (this.gBackgroundOffset + 3);
        }
    }

    public int getPlayerCenterX() {
        return mPlayer.x + mPlayer.width / 2;
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
        if (mPlayer == null || mPlayer.state == -1 || mIsGameFinished || mIsMissionComplete) {
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

    @Override
    public void setPause(int continueCount) {
        removeAllRunnable();
        mContinueNum = continueCount;
        playerLife = 3;
        mPlayer = PlayerPlane.createPlayerPlane(playerType);
        this.mPlayerPower = mPlayer.power;
        mIsGameOver = false;
        continueWindowShow = false;
    }

    @Override
    public void setPause(boolean b) {
        mGamePause = b;
        getHandler().removeCallbacksAndMessages(null);
    }

    @Override
    public void stopContinue() {
        removeAllRunnable();
        mIsGameFinished = true;
        showGameOver();
    }

    public boolean inScreen(int posX, int posY, Bitmap source) {
        if ((posX >= 0 && posX <= MainWindow.windowWidth - source.getWidth()) && posY >= 0 && posY <= MainWindow.windowHeight - source.getHeight()) {
            return true;
        }
        return false;
    }

    private void playSound(int i) {
        try {
            soundHelper.playSound(context, i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleQuit() {
        isThreadAlive = false;
        removeAllRunnable();
        ((MainWindow) getParent().getParent()).reloadMainMenu();
    }

    @Override
    public void removeAllRunnable() {
        getHandler().removeCallbacksAndMessages(null);
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

    public int getScoreLevel() {
        int index = 0;
        for (int i = 0; i < scoreLevel.length - 1; i++) {
            if (mGameScore >= scoreLevel[i] && mGameScore < scoreLevel[i + 1]) {
                return i;
            }
            index++;
        }
        return index >= 25 ? 25 : index;
    }

    public boolean handlePause() {
        if (!mGamePause) {
            mGamePause = true;
            return true;
        }
        isThreadAlive = false;
        return false;
    }
}
