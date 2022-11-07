package com.flyfighter.menu;

import android.content.Context;
import android.view.SurfaceView;

import com.flyfighter.entity.PlayerPlane;
import com.flyfighter.entity.RectArea;
import com.flyfighter.res.RMS;
import com.flyfighter.res.ResInit;

import java.util.Random;

public class GameCanvas extends SurfaceView {

    private Context context;
    private int player;
    private int difficulty;
    private PlayerPlane playerPlane;
    private RectArea gSrcRect;
    private Random random;

    public static final byte[] bulletPic = new byte[]{2, 3, 3, 1, 4, 2, 2, 2, 4, 4, 4, 4, 4, 4, 4, 2, 2, 2, 2, 2, 2};

    public GameCanvas(Context context, int player) {
        super(context, null);
        this.context = context;
        this.player = player;
        this.difficulty = RMS.difficulty;

        this.playerPlane = new PlayerPlane();
        this.gSrcRect = new RectArea();
        this.random = new Random();
        gameInit();
    }

    private void gameInit() {
        ResInit.loadPlayer(context, player);
    }

}
