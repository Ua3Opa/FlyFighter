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

import com.flyfighter.enums.RunState;
import com.flyfighter.holder.MainDataHolder;
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


    public static MediaPlayer[] soundPlayer = new MediaPlayer[2];
    private GameWindow gameWindow;
    private PauseMenu pauseMenu;
    private ContinueMenu continueMenu;
    private RankingMenu rankingMenu;
    private ConfigMenu configMenu;
    private SelectPlayerMenu selectPlayerMenu;

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
                MainDataHolder.runState.setValue(RunState.SelectPlayer);
                break;
            case 1://游戏设置
                MainDataHolder.runState.setValue(RunState.Config);
                break;
            case 2://战绩排行
                MainDataHolder.runState.setValue(RunState.Ranking);
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

    public void startGame(int player) {
        removeAllViews();
        stopPlayMedia();
        gameWindow = new GameWindow(mContext, player);
        addView(gameWindow, buildCenterLayoutParams());
        MainDataHolder.mainAdState.postValue(false);
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

    public void showSelectPlayer() {
        selectPlayerMenu = new SelectPlayerMenu(mContext);
        addView(selectPlayerMenu, buildCenterLayoutParams());
    }

    public void showContinue() {
        continueMenu = new ContinueMenu(mContext);
        addView(continueMenu, buildCenterLayoutParams());
    }

    public void hideContinue() {
        if (continueMenu == null) {
            return;
        }
        removeView(continueMenu);
        continueMenu = null;
    }

    public void resetMenuData() {
        gameWindow = null;
        pauseMenu = null;
        continueMenu = null;
        rankingMenu = null;
    }

    public void handleRunState(RunState state) {
        switch (state) {
            case Main:
                reloadMainMenu();
                break;
            case SelectPlayer:
                showSelectPlayer();
                break;
            case Running:
                hideContinue();
                hideRanking();
                removePause();
                removeContinue();
                break;
            case Pause:
                showPause();
                break;
            case GameOver:
                showContinue();
                break;
            case ContinuePlay:
                gameWindow.handleContinuePlay();
                break;
            case Config:
                showConfig();
                break;
            case Close_Config:
                closeConfig();
                break;
            case Ranking:
                showRanking();
                break;
            case Close_Ranking:
                if (gameWindow != null) {
                    reloadMainMenu();
                    return;
                }
                hideRanking();
                break;
            case Quit:
                gameWindow.handleQuitGame();
                break;
        }

    }

    private void showConfig() {
        configMenu = new ConfigMenu(mContext);
        addView(configMenu, buildCenterLayoutParams());
    }

    private void closeConfig() {
        if (configMenu == null) {
            return;
        }
        removeView(configMenu);
        configMenu = null;
    }

    public void showRanking() {
        rankingMenu = new RankingMenu(mContext);
        addView(rankingMenu, buildCenterLayoutParams());
    }


    public void hideRanking() {
        if (rankingMenu == null) {
            return;
        }
        removeView(rankingMenu);
        rankingMenu = null;
    }

    public void showPause() {
        if (gameWindow == null) {
            return;
        }
        pauseMenu = new PauseMenu(mContext);
        addView(pauseMenu, buildCenterLayoutParams());
    }

    private void removePause() {
        if (pauseMenu == null) {
            return;
        }
        removeView(pauseMenu);
        pauseMenu = null;
    }

    private void removeContinue() {

    }
}
