package com.flyfighter.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.flyfighter.menu.ConfigMenu;
import com.flyfighter.menu.GameWindow;
import com.flyfighter.menu.HelpMenu;
import com.flyfighter.menu.MainMenu;
import com.flyfighter.menu.MainMenuBackground;
import com.flyfighter.menu.RankingMenu;
import com.flyfighter.menu.SelectPlayerMenu;
import com.flyfighter.res.RMS;
import com.flyfighter.res.ResInit;

public class MainWindow extends FrameLayout {

    Context mContext;
    public static int windowWidth;
    public static int windowHeight;

    public static MediaPlayer[] soundPlayer = new MediaPlayer[2];
    private GameWindow gameWindow;

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
                addView(new ConfigMenu(mContext), buildCenterLayoutParams());
                break;
            case 2://战绩排行
                showRanking();
                break;
            case 3://游戏说明
                addView(new HelpMenu(mContext), buildCenterLayoutParams());
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
        gameWindow = new GameWindow(mContext, player);
        addView(gameWindow, buildCenterLayoutParams());
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

    public void showRanking() {
        addView(new RankingMenu(mContext), buildCenterLayoutParams());
    }

    public void handleCloseRanking() {
        if (gameWindow != null) {
            removeAllViews();
            gameWindow = null;
            loadMainMenu();
        }else{
            removeView(RankingMenu.class);
        }
    }


    public void removeView(Class clazz) {
        for (int childCount = getChildCount(); childCount > 0; childCount--) {
            if (getChildAt(childCount - 1).getClass() == clazz) {
                removeView(getChildAt(childCount - 1));
            }
        }
    }
}
