package com.flyfighter.menu;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.flyfighter.interf.Controller;
import com.flyfighter.utils.UiUtils;

public class GameWindow extends FrameLayout {

    public GameWindow(Context context, int playerType) {
        super(context);
        init(context, playerType);
    }

    private void init(Context context, int playerType) {
        GameCanvas gameCanvas = new GameCanvas(context, playerType);
        addView(gameCanvas);
        addView(new GameController(context,gameCanvas));
    }

}
