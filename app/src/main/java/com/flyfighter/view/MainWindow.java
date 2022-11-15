package com.flyfighter.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.flyfighter.menu.GameCanvas;
import com.flyfighter.menu.MainMenu;
import com.flyfighter.menu.MainMenuBackground;
import com.flyfighter.menu.SelectPlayerMenu;
import com.flyfighter.res.RMS;
import com.flyfighter.res.ResInit;

public class MainWindow extends FrameLayout {

    Context mContext;
    public static int windowWidth;
    public static int windowHeight;

    public static MediaPlayer[] soundPlayer = new MediaPlayer[2];

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
        loadMainMenu();
    }

    private void loadMainMenu() {
        addView(new MainMenuBackground(mContext));
        addView(new MainMenu(mContext), buildCenterLayoutParams());
    }


    public void handleMenuClicked(int position) {
        switch (position) {
            case 0://开始游戏-先选择角色
                addView(new SelectPlayerMenu(mContext), buildCenterLayoutParams());
                break;
            case 1://游戏设置
                break;
            case 2://战绩排行
                break;
            case 3://游戏说明
                break;
            case 4://退出游戏
                ((Activity) mContext).finish();
                break;
            default:
                break;

        }
    }

    public void startGame(SelectPlayerMenu menu, int player) {
        removeAllViews();
        stopPlayMedia();
        addView(new GameCanvas(mContext, player),buildCenterLayoutParams());
    }

    private void stopPlayMedia() {
        for (MediaPlayer mediaPlayer : soundPlayer) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    private LayoutParams buildCenterLayoutParams() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;
        return layoutParams;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
