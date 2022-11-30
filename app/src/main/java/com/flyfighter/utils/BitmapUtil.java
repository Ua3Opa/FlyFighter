package com.flyfighter.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.flyfighter.view.MainWindow;

public class BitmapUtil {

    public static Bitmap fitCenter(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float scaleX = MainWindow.windowWidth * 1.0f / width;
        float scaleY = MainWindow.windowHeight * 1.0f / height;

        float maxScale = scaleX > scaleY ? scaleX : scaleY;

        Matrix matrix = new Matrix();
        matrix.setScale(maxScale, maxScale);
        int scaleW = (int) (width * maxScale);
        int scaleH = (int) (height * maxScale);
        Bitmap source =  Bitmap.createBitmap(bitmap, 0, 0,width, height, matrix, false);
        return Bitmap.createBitmap(source, (scaleW - MainWindow.windowWidth) / 2, (scaleH - MainWindow.windowHeight) / 2, MainWindow.windowWidth, MainWindow.windowHeight);
    }
}
