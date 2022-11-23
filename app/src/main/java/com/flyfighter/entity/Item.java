package com.flyfighter.entity;

import android.graphics.Bitmap;

import com.flyfighter.res.ResInit;

import java.util.Random;

public class Item {

    public int type;
    public int x;
    public int y;
    public int speedX;
    public int speedY;
    public int imgIndex;
    public int imgNum = 2;

    public Bitmap sourceImg;


    Random random = new Random();

    public Item(int type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
        //随机左右方向
        this.speedX = (new Random().nextInt(6) + 8) * (new Random().nextInt(2) == 0 ? -1 : 1);
        this.speedY = 4;
        this.sourceImg = ResInit.itemImage[type];
    }

    private int getRand(int i) {
        if (i == 0) {
            return 0;
        }
        int r = this.random.nextInt();
        r = (r >> 24) + (r >> 16) + (r >> 8) + r & 0xFF;
        return Math.abs(r % i);
    }


    public Bitmap getImg() {
        imgIndex++;
        int index = imgIndex / 8;
        //Log.d("TAG", "getImg: "+this.hashCode()+"   " +(picIndex - 1 + index % imgNum));
        return Bitmap.createBitmap(sourceImg, sourceImg.getWidth() / imgNum * (index % imgNum), 0, sourceImg.getWidth() / imgNum, sourceImg.getHeight());
    }

    public Bitmap getImg0() {
        //Log.d("TAG", "getImg: "+this.hashCode()+"   " +(picIndex - 1 + index % imgNum));
        return Bitmap.createBitmap(sourceImg, 0, 0, sourceImg.getWidth() / imgNum, sourceImg.getHeight());
    }
}
