package com.flyfighter.view;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.flyfighter.res.RMS;
import com.flyfighter.res.ResInit;

import java.io.IOException;

public class MainWindow extends FrameLayout {

    Context mContext;
    public static int windowWidth;
    public static int windowHeight;

    MediaPlayer[] soundPlayer = new MediaPlayer[2];

    public MainWindow(Context context) {
        this(context, null);
    }

    public MainWindow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MainWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initWindow(context);
    }

    private void initWindow(Context context) {
        this.mContext = context;
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        windowWidth = metrics.widthPixels;
        windowHeight = metrics.heightPixels;
        ResInit.loadPicInit(context);
        RMS.readConfigData();
        if (RMS.loadSound) {
            loadSound();
        }
    }

    private void loadSound() {
        AssetManager assets = mContext.getAssets();
        try {
            soundPlayer[0].setDataSource(assets.openFd("sound/0menuselect.wav"));
            soundPlayer[1].setDataSource(assets.openFd("sound/13menumusic.wav"));

            soundPlayer[1].setVolume(RMS.volume, RMS.volume);
            soundPlayer[1].setLooping(true);
            soundPlayer[1].start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
