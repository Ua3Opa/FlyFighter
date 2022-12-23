package com.flyfighter.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import com.flyfighter.R;
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
    private int currentSelect = 0;

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
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
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
        if (currentSelect == tag) {
            ((MainWindow) getParent()).handleMenuClicked(currentSelect);
        }
        currentSelect = tag;
    }

    private void resetMenuSelectState() {
        for (int i = 0; i < menus.size(); i++) {
            menus.get(i).setImageBitmap(ResInit.menuImage[i]);
        }
    }

    public void handlePlayMenuSelectSound() {
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
