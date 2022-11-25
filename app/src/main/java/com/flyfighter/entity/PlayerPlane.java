package com.flyfighter.entity;

import android.graphics.Bitmap;

import com.flyfighter.menu.GameCanvas;
import com.flyfighter.res.ResInit;
import com.flyfighter.view.MainWindow;

import java.util.ArrayList;
import java.util.List;

public class PlayerPlane extends Spirit {

    public static final int[][] playerData = new int[][]{{1, 106, 320, 20, 3, 3, 0, 1}, {2, 105, 320, 16, 3, 3, 0, 1}, {3, 105, 320, 24, 3, 3, 0, 1}};
    //-1 : 初始化完成,需要进场
    // 0 : 正常可控制状态
    public int state = -1;

    public int type;//类型
    public Bitmap[] stateImg;
    public volatile int x;
    public volatile int y;

    public int width;
    public int height;

    public int speed;
    public volatile int speedX;
    public volatile int speedY;
    public int life;
    public int bomb;//个数
    public int picId;
    public int power;

    public int bombType;
    //类型
    public List<Integer> bombTypes = new ArrayList<>();

    public PlayerPlane() {
    }

    public void setValue(int type, int x, int y, int speed, int life, int bomb, int picId, int power) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.life = life;
        this.bomb = bomb;
        this.picId = picId;
        this.power = power;
    }


    public static PlayerPlane createPlayerPlane(int playerType) {
        PlayerPlane player = new PlayerPlane();
        int[] data = playerData[playerType];
        player.stateImg = ResInit.playerStateImage;

        player.setValue(data[0],
                (MainWindow.windowWidth - player.stateImg[0].getWidth()) / 2,
                (MainWindow.windowHeight + player.stateImg[0].getHeight()),
                data[3], data[4], data[5], data[6], data[7]);

        player.width = player.stateImg[0].getWidth();
        player.height = player.stateImg[0].getHeight();

        player.life = 6;
        player.bomb = 6;
        player.bombType = playerType;
//        for (int i = 0; i < player.bomb; i++) {
//            player.bombTypes.add(playerType + 1);
//        }
        player.bombTypes.add(3);
        player.bombTypes.add(2);
        player.bombTypes.add(1);
        player.bombTypes.add(3);
        player.bombTypes.add(2);
        player.bombTypes.add(1);
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
        if ((x > 0 && x <= MainWindow.windowWidth - stateImg[0].getWidth()) ||
                (x <= 0 && speedX >= 0) ||
                (x >= MainWindow.windowWidth - stateImg[0].getWidth() && speedX <= 0)) {
            x += speedX;
        }
        //在屏幕范围之内
        if ((y > 0 && y <= MainWindow.windowHeight - stateImg[0].getHeight()) ||
                (y <= 0 && speedY >= 0) ||
                (y >= MainWindow.windowHeight - stateImg[0].getHeight() && speedY <= 0)) {
            y += speedY;
        }
    }

    public PlayerBullet makeBullet(int[] data) {
        int picIndex = 0;
        for (int j = 0; j < type; ++j) {//计算子弹图片的索引
            picIndex += GameCanvas.bulletPic[j];
        }
        Bitmap bitmap = ResInit.bulletImage[picIndex - 1];
        PlayerBullet bullet = PlayerBullet.mallocBullet(type, x + width / 2 + bitmap.getWidth() / 2 + data[1], y + data[2], data[3], data[4], 1);
        return bullet;
    }

    @Override
    protected void initSpiritBitmap() {

    }
}
