package com.flyfighter.res;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;

import com.flyfighter.menu.GameCanvas;

import java.io.IOException;

public class ResInit {
    public static Bitmap[] menuImage = null;
    public static Bitmap[] menuImageSelected = null;
    public static Bitmap[] mainFormImage = null;

    public static Bitmap[] helpImage = null;
    public static Bitmap[] configNumImage = null;
    public static Bitmap[] configImage = null;
    public static Bitmap[] playerSelectImage = null;
    public static Bitmap titleImage = null;
    public static Bitmap rankingImage = null;
    public static Bitmap[] BackgroundImage = null;
    public static Bitmap[] playerImage = null;
    public static Bitmap[] playerBullet = null;
    public static Bitmap[] enemyBullet = null;
    public static Bitmap[] pauseImage = null;
    public static Bitmap[] bombIcon = null;
    public static Bitmap[] lifeIcon = null;
    public static Bitmap[] characterImage = null;
    public static Bitmap[] numberImage = null;
    public static Bitmap[] itemImage = null;
    public static Bitmap[] explodeImage = null;
    public static Bitmap[] bombImage = null;
    public static Bitmap[] levelImage = null;
    public static Bitmap[] otherImage = null;
    public static Bitmap[][] enemyImage = null;
    public static Bitmap[] bulletImage = null;

    public ResInit() {
    }

    public static void loadPicInit(Context context) {
        AssetManager assets = context.getAssets();
        try {
            initStrNumber(assets);
            loadPlayer(assets);
            gamePicInit(assets);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void initStrNumber(AssetManager assets) throws Exception {

        otherImage = new Bitmap[12];
        otherImage[1] = BitmapFactory.decodeStream(assets.open("Resource/fly_missionback.png"));
        otherImage[2] = BitmapFactory.decodeStream(assets.open("Resource/fly_missionload.png"));

        numberImage = new Bitmap[3];
        for (int i = 0; i < 3; ++i) {
            numberImage[i] = BitmapFactory.decodeStream(assets.open("Resource/fly_strnumber" + (i + 1) + ".png"));
        }

    }

    private static void loadPlayer(AssetManager assets) throws Exception {
        playerSelectImage = new Bitmap[5];
        for (int i = 0; i < 5; i++) {
            playerSelectImage[i] = BitmapFactory.decodeStream(assets.open("SelectPlayer/fly_selectplayer" + (i + 1) + ".png"));
        }

        characterImage = new Bitmap[3];
        for (int i = 0; i < 3; i++) {
            characterImage[i] = BitmapFactory.decodeStream(assets.open("Resource/fly_selectname" + (i + 1) + ".png"));
        }

        levelImage = new Bitmap[26];
        for (int i = 0; i < 26; i++) {
            if (i < 9) {
                levelImage[i] = BitmapFactory.decodeStream(assets.open("Level/fly_level0" + (i + 1) + ".png"));
            } else {
                levelImage[i] = BitmapFactory.decodeStream(assets.open("Level/fly_level" + (i + 1) + ".png"));
            }
        }

        otherImage[7] = BitmapFactory.decodeStream(assets.open("Resource/fly_strline2.png"));
        otherImage[8] = BitmapFactory.decodeStream(assets.open("Resource/fly_strline3.png"));

    }


    public static void loadMainMenuImg(Context context) {
        menuImage = new Bitmap[5];
        menuImageSelected = new Bitmap[5];
        mainFormImage = new Bitmap[4];

        try {
            AssetManager assets = context.getAssets();
            for (int i = 0; i < 4; i++) {
                mainFormImage[i] = BitmapFactory.decodeStream(assets.open("Resource/fly_mission0" + (i + 1) + ".png"));
            }

            for (int i = 0; i < 5; i++) {
                Bitmap bitmap = BitmapFactory.decodeStream(assets.open("StartSelect/fly_startselect" + (i + 1) + ".png"));
                //选中状态的图片
                menuImageSelected[i] = Bitmap.createBitmap(bitmap, 0, i * bitmap.getHeight() / 5, bitmap.getWidth(), bitmap.getHeight() / 5);

                if (i == 0) {
                    menuImage[1] = Bitmap.createBitmap(bitmap, 0, 1 * bitmap.getHeight() / 5, bitmap.getWidth(), bitmap.getHeight() / 5);
                    menuImage[2] = Bitmap.createBitmap(bitmap, 0, 2 * bitmap.getHeight() / 5, bitmap.getWidth(), bitmap.getHeight() / 5);
                    menuImage[3] = Bitmap.createBitmap(bitmap, 0, 3 * bitmap.getHeight() / 5, bitmap.getWidth(), bitmap.getHeight() / 5);
                    menuImage[4] = Bitmap.createBitmap(bitmap, 0, 4 * bitmap.getHeight() / 5, bitmap.getWidth(), bitmap.getHeight() / 5);
                } else if (i == 1) {
                    menuImage[0] = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight() / 5);
                }

            }

            playerSelectImage = new Bitmap[5];
            for (int i = 0; i < 5; i++) {
                playerSelectImage[i] = BitmapFactory.decodeStream(assets.open("SelectPlayer/fly_selectplayer" + (i + 1) + ".png"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void gamePicInit(AssetManager assets) throws Exception {

        otherImage[0] = BitmapFactory.decodeStream(assets.open("Resource/fly_gamecomplete.png"));
        otherImage[3] = BitmapFactory.decodeStream(assets.open("Resource/fly_strcomplete.png"));
        otherImage[4] = BitmapFactory.decodeStream(assets.open("Resource/fly_strcontinue.png"));

        otherImage[5] = BitmapFactory.decodeStream(assets.open("Resource/fly_strgame.png"));
        otherImage[6] = BitmapFactory.decodeStream(assets.open("Resource/fly_strgameover.png"));
        otherImage[9] = BitmapFactory.decodeStream(assets.open("Resource/fly_strgameover.png"));
        otherImage[10] = BitmapFactory.decodeStream(assets.open("Resource/fly_lastmission.png"));
        otherImage[11] = BitmapFactory.decodeStream(assets.open("Resource/continuetips.png"));

    }

    public static void loadPlayer(Context context, int player) {
        try {
            playerImage = new Bitmap[3];
            playerImage[player] = BitmapFactory.decodeStream(context.getAssets().open("Player/fly_player" + (player + 1) + ".png"));

            bombImage = new Bitmap[8];
            for (int i = 0; i < 3; i++) {
                bombImage[i] = BitmapFactory.decodeStream(context.getAssets().open("Bomb/fly_bomb1" + (i + 1) + ".png"));
            }
            bombImage[3] = BitmapFactory.decodeStream(context.getAssets().open("Bomb/fly_bomb2.png"));
            for (int i = 4; i < 7; i++) {
                bombImage[i] = BitmapFactory.decodeStream(context.getAssets().open("Bomb/fly_bomb3" + (i - 3) + ".png"));
            }
            bombImage[3] = BitmapFactory.decodeStream(context.getAssets().open("Bomb/fly_bomb4.png"));


            bombIcon = new Bitmap[3];
            for (int i = 0; i < 3; i++) {
                bombIcon[i] = BitmapFactory.decodeStream(context.getAssets().open("Resource/fly_disbomb" + (i + 1) + ".png"));
            }

            lifeIcon = new Bitmap[3];
            lifeIcon[player] = BitmapFactory.decodeStream(context.getAssets().open("Resource/fly_playerlife" + (player + 1) + ".png"));

            bulletImage = new Bitmap[60];

            int j = 0;
            for (int i = 0; i < 2; i++, j++) {
                bulletImage[j] = BitmapFactory.decodeStream(context.getAssets().open("Bullet/fly_playerbullet1" + (i + 1) + ".png"));
            }
            for (int i = 0; i < 3; i++, j++) {
                bulletImage[j] = BitmapFactory.decodeStream(context.getAssets().open("Bullet/fly_playerbullet2" + (i + 1) + ".png"));
            }
            for (int i = 0; i < 3; i++, j++) {
                bulletImage[j] = bulletImage[j] = BitmapFactory.decodeStream(context.getAssets().open("Bullet/fly_playerbullet3" + (i + 1) + ".png"));
            }
            bulletImage[j] = BitmapFactory.decodeStream(context.getAssets().open("Bullet/fly_tenemybullet04.png"));
            j++;

            for (int i = 4; i < 20; i++) {
                for (int k = 0; k < GameCanvas.bulletPic[i]; k++, j++) {
                    if (i < 9) {
                        bulletImage[j] = BitmapFactory.decodeStream(context.getAssets().open("Bullet/fly_tenemybullet0" + (i + 1) + (k + 1) + ".png"));
                    } else {
                        bulletImage[j] = BitmapFactory.decodeStream(context.getAssets().open("Bullet/fly_tenemybullet" + (i + 1) + (k + 1) + ".png"));
                    }
                }
            }

            for (int i = 0; i < 2; i++, j++) {
                bulletImage[j] = BitmapFactory.decodeStream(context.getAssets().open("Bullet/fly_misbullet" + (i + 1) + ".png"));
            }
            bulletImage[59] = BitmapFactory.decodeStream(context.getAssets().open("Bullet/fly_tenemybullet21.png"));


            itemImage = new Bitmap[14];
            for (int i = 0; i < 14; i++) {
                if (i < 9) {
                    itemImage[i] = BitmapFactory.decodeStream(context.getAssets().open("Goods/fly_goods0" + (i + 1) + ".png"));
                } else {
                    itemImage[i] = BitmapFactory.decodeStream(context.getAssets().open("Goods/fly_goods" + (i + 1) + ".png"));
                }
            }


            explodeImage = new Bitmap[14];
            for (int i = 0; i < 4; i++) {
                explodeImage[i] = BitmapFactory.decodeStream(context.getAssets().open("Blast/fly_blast1" + (i + 1) + ".png"));
            }
            for (int i = 4; i < 8; i++) {
                explodeImage[i] = BitmapFactory.decodeStream(context.getAssets().open("Blast/fly_blast2" + (i - 3) + ".png"));
            }
            for (int i = 8; i < 14; i++) {
                explodeImage[i] = BitmapFactory.decodeStream(context.getAssets().open("Blast/fly_blast" + (i - 5) + ".png"));
            }

            pauseImage = new Bitmap[2];
            try {
                for (int i = 0; i < 2; i++) {
                    pauseImage[i] = BitmapFactory.decodeStream(context.getAssets().open("Pause/fly_pause" + (i + 2) + ".png"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
