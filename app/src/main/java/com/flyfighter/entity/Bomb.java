package com.flyfighter.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Size;

import com.flyfighter.res.ResInit;
import com.flyfighter.view.MainWindow;

import java.util.ArrayList;
import java.util.List;

public class Bomb extends Spirit {

    public int direction;//左右还是上下适用于type ==3
    public int power = 4;

    //无敌时间为5秒
    public Bomb() {
    }

    public static Bomb makeBomb(int type, PlayerPlane player) {
        Bomb bomb = new Bomb();
        bomb.type = type;
        if (type == 4) {
            bomb.source.addAll(createBomb4());
        } else if (type == 1) {
            bomb.source.addAll(createBomb1(player.height));
        } else if (type == 2) {
            bomb.source.addAll(createBomb2());
        }
        bomb.initSpiritSize();
        bomb.initPosition(player);
        return bomb;
    }

    private void initPosition(PlayerPlane player) {
        switch (type) {
            case 1:
                int index = frameIndex / 8;
                Bitmap bitmap;
                if (index <= 3) {
                    bitmap = source.get(index);
                } else {
                    bitmap = source.get(index % 2 + 4);
                }
                x = player.x + player.width / 2 - bitmap.getWidth() / 2;
                y = player.y + player.height - bitmap.getHeight() + 120;
                break;
            case 2:
                y = MainWindow.windowHeight;
                break;
            case 3:
                x = player.x + player.width / 2 - source.get(0).getWidth() / 2;
                break;
            case 4://护盾
                x = player.x + player.width / 2 - source.get(0).getWidth() / 2;
                y = player.y + player.height / 2 - source.get(0).getHeight() / 2;
                break;
        }
    }

    public static Bomb[] makeBomb3(int type, PlayerPlane player) {
        if (type != 3) {
            return null;
        }
        Bomb[] bomb = createBomb3();
        bomb[0].x = player.x + player.x / 2 - bomb[0].source.get(0).getWidth();
        bomb[1].x = player.x + player.x / 2 - bomb[1].source.get(0).getWidth();
        bomb[0].initPosition(player);
        bomb[1].initPosition(player);
        return bomb;
    }

    private static Bomb[] createBomb3() {
        Bomb[] bombs = new Bomb[2];
        bombs[0] = new Bomb();
        bombs[0].type = 3;
        bombs[1] = new Bomb();
        bombs[1].type = 3;
        int picIndex = 4;
        int picNum = 2;
        for (int index = picIndex; index < 7; index++) {
            //左右两张
            for (int i = 0; i < 2; i++) {
                Bitmap source = Bitmap.createBitmap(ResInit.bombImage[index], ResInit.bombImage[index].getWidth() / picNum * i, 0, ResInit.bombImage[index].getWidth() / picNum, ResInit.bombImage[index].getHeight());

                Bitmap bitmap;
                if (source.getHeight() <= MainWindow.windowHeight) {
                    bitmap = Bitmap.createBitmap(source.getWidth(), MainWindow.windowHeight, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    int height = 0;
                    while (height < MainWindow.windowHeight) {
                        canvas.drawBitmap(source, 0, height, new Paint());
                        height += source.getHeight();
                    }
                } else {
                    bitmap = source;
                }
                bombs[i % 2].source.add(bitmap);
                bombs[i % 2].direction = (i == 0 ? -1 : 1);
            }
        }
        return bombs;
    }

    private static List<Bitmap> createBomb2() {
        List<Bitmap> bitmaps = new ArrayList<>();
        Bitmap bitmap = Bitmap.createBitmap(ResInit.bombImage[3], 0, 0, ResInit.bombImage[3].getWidth(), ResInit.bombImage[3].getHeight());
        bitmaps.add(bitmap);
        return bitmaps;
    }

    private static List<Bitmap> createBomb4() {
        List<Bitmap> bitmaps = new ArrayList<>();
        int picNum = 4;
        for (int i = 0; i < 4; i++) {
            Bitmap bitmap = Bitmap.createBitmap(ResInit.bombImage[7], ResInit.bombImage[7].getWidth() / picNum * (i % picNum), 0, ResInit.bombImage[7].getWidth() / 4, ResInit.bombImage[7].getHeight());
            bitmaps.add(bitmap);
        }
        return bitmaps;
    }

    private static List<Bitmap> createBomb1(int playerHeight) {
        List<Bitmap> bitmaps = new ArrayList<>();

        //前三张动画
        Bitmap source = ResInit.bombImage[0];
        //闪烁部分
        int picNum = 3;
//        for (int i = 0; i < picNum; i++) {
//            bitmaps.add(Bitmap.createBitmap(source, source.getWidth() / picNum * i, 0, source.getWidth() / picNum, source.getHeight()));
//        }

        Bitmap sBottom = ResInit.bombImage[1];
        Bitmap sTop = ResInit.bombImage[2];
        Paint paint = new Paint();
        //偏移值为120+飞机高度
        int offset = 120 + playerHeight;
        //已发射变粗部分
        for (int i = 0; i < picNum; i++) {
            //加上偏移值
            Bitmap bitmap = Bitmap.createBitmap(sBottom.getWidth() / 3, MainWindow.windowHeight + offset, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            Bitmap bottomIndex = Bitmap.createBitmap(sBottom, sBottom.getWidth() / picNum * i, 0, sBottom.getWidth() / picNum, sBottom.getHeight());
            Bitmap topIndex = Bitmap.createBitmap(sTop, sTop.getWidth() / picNum * i, 0, sTop.getWidth() / picNum, sTop.getHeight());

            int posY = MainWindow.windowHeight - sBottom.getHeight() + offset;
            canvas.drawBitmap(bottomIndex, 0, posY, paint);
            //相对于飞机的下移量为120
            while (posY > -(120 + playerHeight + sBottom.getHeight())) {
                posY -= topIndex.getHeight();
                canvas.drawBitmap(topIndex, 0, posY, paint);
            }
            bitmaps.add(bitmap);
        }
        return bitmaps;
    }

    public Bitmap getFrame() {
        super.getFrame();//frameIndex++
        Bitmap bitmap = null;
        int index;
        switch (type) {
            case 1:
                index = frameIndex / 8;
                if (source.size() == 3 && index == 0) {
                    bitmap = source.get(0);
                } else if (source.size() == 3) {
                    source.remove(0);
                    bitmap = source.get(0);
                } else {
                    bitmap = source.get(index % 2);
                }
                break;
            case 2:
                bitmap = source.get(0);
                break;
            case 3:
                index = frameIndex < 2 ? frameIndex : 2;
                bitmap = source.get(index);
                break;
            case 4://护盾
                index = frameIndex / 8;
                bitmap = source.get(index % 4);
                break;
        }
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

    public void dealMoveState(PlayerPlane mPlayer) {
        recordMovePosition();
        Bitmap bitmap = null;
        switch (type) {
            case 1:
                if (mPlayer == null) {
                    return;
                }
                bitmap = source.get(0);
                x = mPlayer.x + mPlayer.width / 2 - bitmap.getWidth() / 2;
                y = mPlayer.y + mPlayer.height - bitmap.getHeight() + 120;
                break;
            case 2:
                y -= 20;
                break;
            case 3:
                if (direction == -1) {
                    x -= 15;
                } else {
                    x += 15;
                }
                break;
            case 4://护盾
                x = mPlayer.x + mPlayer.width / 2 - source.get(0).getWidth() / 2;
                y = mPlayer.y + mPlayer.height / 2 - source.get(0).getHeight() / 2;
                break;
        }
    }

    @Override
    protected void initSpiritBitmap() {

    }
}
