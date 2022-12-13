package com.flyfighter.entity;

import android.graphics.Bitmap;

import com.flyfighter.res.ResInit;
import com.flyfighter.view.MainWindow;

import java.util.Random;

public class Item extends Spirit {

    public Item(int type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
        //随机左右方向
        this.speedX = (new Random().nextInt(6) + 8) * (new Random().nextInt(2) == 0 ? -1 : 1);
        this.speedY = 4;
        this.picNum = 2;
        initSpiritBitmap();
        initSpiritSize();
    }

    @Override
    public Bitmap getFrame() {
        super.getFrame();
        return source.get(frameIndex % picNum);
    }

    @Override
    protected void initSpiritBitmap() {
        source.addAll(splitBitmap(ResInit.itemImage[type-1], picNum));
    }

    @Override
    public void dealMoveState() {
        recordMovePosition();
        x += speedX;
        y += speedY;
        if (x < 0 || x > (MainWindow.windowWidth - width)) {
            speedX = -speedX;
        }
    }
}
