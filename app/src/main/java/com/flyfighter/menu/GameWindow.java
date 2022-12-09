package com.flyfighter.menu;

import android.content.Context;
import android.widget.FrameLayout;

public class GameWindow extends FrameLayout {

    private GameCanvas gameCanvas;

    public GameWindow(Context context, int playerType) {
        super(context);
        init(context, playerType);
    }

    private void init(Context context, int playerType) {
        gameCanvas = new GameCanvas(context, playerType);
        addView(gameCanvas);
        addView(new GameController(context, gameCanvas));
    }

    public void handleQuitGame() {
        gameCanvas.QuitGame();
    }

    public void handleContinuePlay() {
        gameCanvas.continuePlay();
    }
}
