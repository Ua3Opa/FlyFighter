package com.flyfighter.res;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;

import java.io.IOException;
import java.io.InputStream;

public class ResInit {
    public static Bitmap[] menuImage = null;
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
        otherImage = new Bitmap[12];
        AssetManager assets = context.getAssets();
        try {
            otherImage[1] = BitmapFactory.decodeStream(assets.open("Resource/fly_missionback.png"));
            otherImage[2] = BitmapFactory.decodeStream(assets.open("Resource/fly_missionload.png"));
        } catch (IOException var2) {
            var2.printStackTrace();
        }

        numberImage = new Bitmap[3];

        try {
            for (int i = 0; i < 3; ++i) {
                numberImage[i] = BitmapFactory.decodeStream(assets.open("Resource/fly_strnumber" + (i + 1) + ".png"));
            }
        } catch (IOException var3) {
            var3.printStackTrace();
        }

    }

}
