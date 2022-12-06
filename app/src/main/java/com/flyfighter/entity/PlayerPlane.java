package com.flyfighter.entity;

import android.graphics.Bitmap;

import com.flyfighter.res.ResInit;
import com.flyfighter.view.MainWindow;

import java.util.ArrayList;
import java.util.List;

public class PlayerPlane extends Spirit {

    public static final int[][] playerData = new int[][]{
            {1, 106, 320, 20, 6, 6, 1},
            {2, 105, 320, 16, 6, 6, 1},
            {3, 105, 320, 24, 6, 6, 1}};
    //-1 : 初始化完成,需要进场
    // 0 : 正常可控制状态
    // 2 : 无敌
    public int state = -1;

    public int type;//类型

    public int speed;
    public int life;
    public int bomb;//个数
    public int picId;
    public int power;

    public int bombType;
    //类型
    public List<Integer> bombTypes = new ArrayList<>();

    public boolean onFire;

    public PlayerPlane() {
    }

    public void setValue(int type, int x, int y, int speed, int life, int bomb, int power) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.life = life;
        this.bomb = bomb;
        this.power = power;
    }


    public static PlayerPlane createPlayerPlane(int playerType) {
        PlayerPlane player = new PlayerPlane();
        int[] data = playerData[playerType];
        player.type = playerType;
        player.initSpiritBitmap();
        player.initSpiritSize();

        player.setValue(data[0],
                (MainWindow.windowWidth - player.width) / 2,
                (MainWindow.windowHeight + player.height),
                data[3], data[4], data[5], data[6]);

        player.bombType = playerType;
        for (int i = 0; i < player.bomb; i++) {
            player.bombTypes.add(playerType + 1);
        }
        return player;
    }

    public void handleSpeed(double xPercent, double yPercent) {
        speedX = (int) (speed * xPercent);
        speedY = (int) (speed * yPercent);
        //Log.d("TAG", "handleSpeed: " + speedX + "  " + speedY);
    }

    public void handleMove() {
        //Log.d("TAG", "handleMove: " + x + "  " + speedX);
        //在屏幕范围之内
        if ((x > 0 && x <= MainWindow.windowWidth - width) ||
                (x <= 0 && speedX >= 0) ||
                (x >= MainWindow.windowWidth - height && speedX <= 0)) {
            x += speedX;
        }
        //在屏幕范围之内
        if ((y > 0 && y <= MainWindow.windowHeight - height) ||
                (y <= 0 && speedY >= 0) ||
                (y >= MainWindow.windowHeight - height && speedY <= 0)) {
            y += speedY;
        }
    }

    public PlayerBullet makeBullet(int[] data) {
        PlayerBullet bullet = PlayerBullet.mallocBullet(type, x + width / 2 + data[1], y + data[2] -5, data[3], data[4], data[5]);
        return bullet;
    }

    @Override
    protected void initSpiritBitmap() {
        Bitmap bitmap = ResInit.playerImage[type];
        source.addAll(splitBitmap(bitmap, 6));
    }

    @Override
    public Bitmap getFrame() {
        super.getFrame();
        Bitmap bitmap = null;
        if (state == -1) {//进场
            if (frameIndex % 2 == 0) {
                bitmap = source.get(0);
            } else {
                bitmap = source.get(5);
            }
        } else {//正常状态
            if (speedX < 0) {
                bitmap = source.get(2);
            } else if (speedX > 0) {
                bitmap = source.get(3);
            } else {
                bitmap = source.get(frameIndex % 2);
            }
        }
        return bitmap;
    }

    public Bitmap getFireFrame() {
        if (onFire && frameIndex % 4 == 0) {
            return source.get(4);
        }
        return null;
    }

    public void resetState() {
        x = (MainWindow.windowWidth - width) / 2;
        y = MainWindow.windowHeight + height;
        state = -1;
    }
}
