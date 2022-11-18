package com.flyfighter.menu;

import android.content.Context;
import android.widget.FrameLayout;

import com.flyfighter.interf.Controller;

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
