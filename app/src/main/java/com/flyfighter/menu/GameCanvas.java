package com.flyfighter.menu;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.SurfaceView;

import com.flyfighter.entity.PlayerPlane;
import com.flyfighter.entity.RectArea;
import com.flyfighter.res.RMS;
import com.flyfighter.res.ResInit;
import com.flyfighter.res.SoundHelper;
import com.flyfighter.view.MainWindow;

import java.util.Random;

public class GameCanvas extends SurfaceView {

    private Context context;
    private MediaPlayer[] mediaPlayers = new MediaPlayer[13];

    private int playerType;
    private int difficulty;
    private PlayerPlane player;
    private RectArea gSrcRect;
    private Random random;


    private boolean gIsLoadGame;
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

    public static final int[] bulletPic = new int[]{2, 3, 3, 1, 4, 2, 2, 2, 4, 4, 4, 4, 4, 4, 4, 2, 2, 2, 2, 2, 2};
    public static final int[][] playerData = new int[][]{
            {1, 5, 3, 3, 1}, {2, 4, 3, 3, 1}, {3, 6, 3, 3, 1}};
    private int[] gBombType = new int[6];


    public GameCanvas(Context context, int playerType) {
        super(context, null);
        this.context = context;
        this.playerType = playerType;
        this.difficulty = RMS.difficulty;

        this.player = new PlayerPlane();
        this.gSrcRect = new RectArea();
        this.random = new Random();
        gameInit();
        stageInit();
    }

    private void gameInit() {
        ResInit.loadPlayer(context, playerType);
        SoundHelper.loadGameSounds(context, mediaPlayers);
        if (RMS.loadSound) {
            playSound(12);
        }
        int[] data = playerData[playerType];
        int width = ResInit.playerImage[playerType].getWidth();
        int height = ResInit.playerImage[playerType].getHeight();
        this.player.setValue(data[0], (MainWindow.windowWidth - width) / 2, MainWindow.windowHeight + height, data[1], data[2], data[3], data[4], 0);
        this.player.setLife(6);
        this.player.setBomb(6);
        for (int i = 0; i < 6; i++) {
            this.gBombType[i] = this.player.type;
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
            this.gCorrespondMission = playerType + 1;
        } else if (this.gMission == 2) {
            if (this.playerType == 0) {
                this.gCorrespondMission = 2;
            } else if (this.playerType == 1) {
                this.gCorrespondMission = 3;
            } else {
                this.gCorrespondMission = 1;
            }
        } else if (this.gMission == 3) {
            this.gCorrespondMission = 4;
        } else if (this.gMission == 4) {
            if (this.playerType == 0) {
                this.gCorrespondMission = 3;
            } else if (this.playerType == 1) {
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
    }


    private void playSound(int index) {
        if (index == 12) {
            mediaPlayers[index].setLooping(true);
        }

        mediaPlayers[index].prepareAsync();
        mediaPlayers[index].isPlaying();
    }

    private int getRand(int i) {
        int r = this.random.nextInt();
        r = (r >> 24) + (r >> 16) + (r >> 8) + r & 0xFF;
        return Math.abs(r % i);
    }
}
