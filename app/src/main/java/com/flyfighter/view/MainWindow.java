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

import com.flyfighter.interf.Controller;
import com.flyfighter.menu.ConfigMenu;
import com.flyfighter.menu.ContinueMenu;
import com.flyfighter.menu.GameWindow;
import com.flyfighter.menu.HelpMenu;
import com.flyfighter.menu.MainMenu;
import com.flyfighter.menu.MainMenuBackground;
import com.flyfighter.menu.PauseMenu;
import com.flyfighter.menu.RankingMenu;
import com.flyfighter.menu.SelectPlayerMenu;
import com.flyfighter.res.RMS;
import com.flyfighter.res.ResInit;

public class MainWindow extends FrameLayout {

    Context mContext;
    public static int windowWidth;
    public static int windowHeight;

    //以1080 * 1920 dpi 480为基准计算缩放比
    public static float scaleW;
    public static float scaleH;
    public static float scaleDes;


    public static MediaPlayer[] soundPlayer = new MediaPlayer[2];
    private GameWindow gameWindow;
    private PauseMenu pauseMenu;
    private ContinueMenu continueMenu;
    private RankingMenu rankingMenu;

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
        wm.getDefaultDisplay().getRealMetrics(metrics);
        windowWidth = metrics.widthPixels;
        windowHeight = metrics.heightPixels;
        scaleW = windowWidth * 1.0f / 1080;
        scaleH = windowHeight * 1.0f / 1920;

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
        if (rankingMenu != null) {
            return;
        }
        rankingMenu = new RankingMenu(mContext);
        addView(rankingMenu, buildCenterLayoutParams());
    }

    public void hideRanking() {
        if (gameWindow != null) {
            removeView(gameWindow);
            gameWindow = null;
            loadMainMenu();
        }
        removeView(rankingMenu);
        rankingMenu = null;
    }

    public void reloadMainMenu() {
        removeAllViews();
        resetMenuData();
        loadMainMenu();
    }


    public void removeView(Class clazz) {
        for (int childCount = getChildCount(); childCount > 0; childCount--) {
            if (getChildAt(childCount - 1).getClass() == clazz) {
                removeView(getChildAt(childCount - 1));
            }
        }
    }

    public void showContinue(Controller controller, int continueNum) {
        if (continueMenu != null) {
            return;
        }
        continueMenu = new ContinueMenu(mContext, controller, continueNum);
        addView(continueMenu, buildCenterLayoutParams());
    }

    public void hideContinue() {
        removeView(continueMenu);
        continueMenu = null;
    }

    public void showPause(Controller controller) {
        if (pauseMenu != null) {
            return;
        }
        pauseMenu = new PauseMenu(mContext, controller);
        addView(pauseMenu, buildCenterLayoutParams());
    }

    public void hidePause() {
        removeView(pauseMenu);
        pauseMenu = null;
    }

    public void resetMenuData() {
        gameWindow = null;
        pauseMenu = null;
        continueMenu = null;
        rankingMenu = null;
    }

    public boolean onBackPressed() {
        if (gameWindow != null) {
            if (gameWindow.handlePause()) {
                return true;
            }
            reloadMainMenu();
            return true;
        }
        return false;
    }
}
