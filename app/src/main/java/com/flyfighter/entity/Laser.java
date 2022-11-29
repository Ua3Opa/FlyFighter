package com.flyfighter.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.flyfighter.res.ResInit;
import com.flyfighter.view.MainWindow;

import java.util.List;

public class Laser extends Bullet {

    public final int laserTime = 2000;
    public int[] shootPoint;

    public static Laser mallocLaser(int type, int x, int y, int[] shootPoint) {
        Laser bullet = new Laser();
        bullet.type = type;
        bullet.picNum = 2;
        bullet.initSpiritBitmap();
        bullet.initSpiritSize();
        bullet.x = x - bullet.width / 2 + shootPoint[0];
        bullet.y = y + shootPoint[1];
        bullet.shootPoint = shootPoint;
        return bullet;
    }


    @Override
    protected void initSpiritBitmap() {
        Paint paint = new Paint();
        List<Bitmap> bm = splitBitmap(ResInit.bulletImage[59], 2);
        source.add(concatBitmap(bm.get(0), paint));
        source.add(concatBitmap(bm.get(1), paint));
    }


    public Bitmap concatBitmap(Bitmap bm, Paint paint) {
        Bitmap bitmap0 = Bitmap.createBitmap(bm.getWidth(), MainWindow.windowHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap0);

        Bitmap bmBottom0 = Bitmap.createBitmap(bm, 0, bm.getHeight() / 2, bm.getWidth(), bm.getHeight() / 2);
        int height = 0;
        canvas.drawBitmap(bm, 0, 0, paint);
        height += bm.getHeight();

        if (height < MainWindow.windowHeight) {
            canvas.drawBitmap(bmBottom0, 0, height, paint);
            height += bmBottom0.getHeight();
        }
        return bitmap0;
    }

    public Bitmap getFrame() {
        if (System.currentTimeMillis() - lastFrameTime >= animDuration) {
            frameIndex++;
        }
        return source.get(frameIndex % picNum);
    }

    public void dealMoveState(Boss boss) {
        x = boss.x + boss.width / 2 - width / 2 + shootPoint[0];
        y = boss.y + boss.height + shootPoint[1];
    }
}
