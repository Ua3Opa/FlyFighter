package com.flyfighter.menu;

import android.content.Context;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.flyfighter.res.RMS;
import com.flyfighter.res.ResInit;
import com.flyfighter.view.MainWindow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainMenu extends LinearLayout {

    Context context;
    private int selectedIndex = 0;
    private List<ImageView> menus = new ArrayList<>();

    public MainMenu(Context context) {
        this(context, null);
    }

    public MainMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        setLayoutParams(layoutParams);
        setOrientation(VERTICAL);
        initMenuItem();
    }

    private void initMenuItem() {
        for (int i = 0; i < 5; i++) {
            ImageView imageView = new ImageView(context);

            if (selectedIndex == i) {
                imageView.setImageBitmap(ResInit.menuImageSelected[i]);
            } else {
                imageView.setImageBitmap(ResInit.menuImage[i]);
            }
            imageView.setTag(i);
            imageView.setOnClickListener(this::handleMenuClicked);
            addView(imageView);
            menus.add(imageView);
        }
    }

    private void handleMenuClicked(View view) {
        resetMenuSelectState();
        handlePlayMenuSelectSound();
        int tag = (int) view.getTag();
        menus.get(tag).setImageBitmap(ResInit.menuImageSelected[tag]);

        ((MainWindow)getParent()).handleMenuClicked(tag);
    }

    private void resetMenuSelectState() {
        for (int i = 0; i < menus.size(); i++) {
            menus.get(i).setImageBitmap(ResInit.menuImage[i]);
        }
    }

    private void handlePlayMenuSelectSound() {
        try {
            MainWindow.soundPlayer[0].reset();
            MainWindow.soundPlayer[0].setDataSource(context.getAssets().openFd("sound/0menuselect.wav"));
            MainWindow.soundPlayer[0].setVolume(RMS.volume, RMS.volume);
            MainWindow.soundPlayer[0].prepare();
            MainWindow.soundPlayer[0].start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
