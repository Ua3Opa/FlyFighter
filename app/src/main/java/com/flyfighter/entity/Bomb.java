package com.flyfighter.entity;

import android.graphics.Bitmap;
import android.util.Size;

import com.flyfighter.res.ResInit;

public class Bomb {

    public int type;
    public int x;
    public int y;

    public int picNum;
    public int imgIndex;

    public long boomTimeMills = System.currentTimeMillis();

    //无敌时间为5秒
    public Bomb() {
    }

    public static Bomb makeBomb(int type, PlayerPlane player) {
        Bomb bomb = new Bomb();
        bomb.type = type;

        if (type == 4) {
            Size imgSize = bomb.getImgSize();
            bomb.x = player.x + player.width / 2 - imgSize.getWidth() / 2;
            bomb.y = player.y + player.height / 2 - imgSize.getHeight() / 2;
            bomb.picNum = 4;
        } else {
            //@TODO
        }
        return bomb;
    }

    public Bitmap getImgWithType() {
        //int index = (int) (System.currentTimeMillis() / 200) % 4;
        Bitmap bitmap = null;
        switch (type) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4://护盾
                int index = imgIndex / 8;
                bitmap = Bitmap.createBitmap(ResInit.bombImage[7], ResInit.bombImage[7].getWidth() / picNum * (index % picNum), 0, ResInit.bombImage[7].getWidth() / 4, ResInit.bombImage[7].getHeight());
                break;

        }
        imgIndex++;
        return bitmap;
    }

    public Size getImgSize() {
        Size size = null;
        switch (type) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4://护盾
                size = new Size(ResInit.bombImage[7].getWidth() / 4, ResInit.bombImage[7].getHeight());
                break;

        }
        return size;
    }

}
