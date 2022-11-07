package com.flyfighter.menu;

import android.content.Context;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.AttributeSet;

import com.flyfighter.res.RMS;
import com.flyfighter.res.ResInit;
import com.flyfighter.view.MainWindow;

public class MainMenuBackground extends androidx.appcompat.widget.AppCompatImageView {

    Context context;
    private final int animDuration = 300;
    private int imgIndex;
    Handler handler = new Handler(msg -> {
        switch (msg.what) {
            case 0:
                handleChangeBackgroundImg();
                break;
        }
        return false;
    });

    public MainMenuBackground(Context context) {
        this(context, null);
    }

    public MainMenuBackground(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainMenuBackground(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        loadMenuTextImg();
    }

    private void init(Context context) {
        this.context = context;
        if (RMS.loadSound) {
            loadSound();
        }
        ResInit.loadMainMenuImg(context);
        setScaleType(ScaleType.CENTER_CROP);
        setImageBitmap(ResInit.mainFormImage[0]);
        handler.sendEmptyMessageDelayed(0, animDuration);
    }

    private void handleChangeBackgroundImg() {
        imgIndex++;
        setImageBitmap(ResInit.mainFormImage[imgIndex % ResInit.mainFormImage.length]);
        handler.sendEmptyMessageDelayed(0, animDuration);
    }

    private void loadSound() {
        AssetManager assets = context.getAssets();
        try {
            MainWindow.soundPlayer[0] = new MediaPlayer();
            MainWindow.soundPlayer[1] = new MediaPlayer();

            MainWindow.soundPlayer[0].setDataSource(assets.openFd("sound/0menuselect.wav"));
            MainWindow.soundPlayer[1].setDataSource(assets.openFd("sound/13menumusic.wav"));

            MainWindow.soundPlayer[1].setVolume(RMS.volume, RMS.volume);
            MainWindow.soundPlayer[1].prepare();
            MainWindow.soundPlayer[1].setLooping(true);
            MainWindow.soundPlayer[1].start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadMenuTextImg() {

    }
}
