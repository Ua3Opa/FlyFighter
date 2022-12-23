package com.flyfighter.menu;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.flyfighter.enums.RunState;
import com.flyfighter.holder.MainDataHolder;
import com.flyfighter.res.RMS;
import com.flyfighter.res.ResInit;
import com.flyfighter.view.MainWindow;

public class ConfigMenu extends FrameLayout {

    Context context;
    private ImageView imgBg;
    private ImageView ivDifficulty;
    private ImageView ivSound;

    //以原始图片1080p下的左上角的偏移值
    private Rect[] clickRect = new Rect[]{
            //难度小
            new Rect(530, 195, 720, 300),
            //难度大
            new Rect(730, 195, 920, 300),
            //音效开
            new Rect(530, 325, 720, 430),
            //音效开
            new Rect(730, 325, 920, 430),
            //音量大小
            new Rect(590, 490, 885, 530),
            //关闭
            new Rect(430, 584, 650, 700),
    };

    Rect backgroundRect;

    private int offX;
    private int offY;
    private ImageView ivProgress;

    private boolean initialized = false;

    public ConfigMenu(Context context) {
        this(context, null);
    }

    public ConfigMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ConfigMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        ResInit.initConfigMenu(context);
        initMenuBg();

        initDifficultySwitch();
        initSoundSwitch();
        initSoundProgress();
    }

    private void initSoundProgress() {
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        ivProgress = new ImageView(context);
        ivProgress.setScaleType(ImageView.ScaleType.FIT_XY);
        ivProgress.setImageBitmap(ResInit.configImage[2]);
        addView(ivProgress, lp);
    }

    private void initSoundSwitch() {
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        ivSound = new ImageView(context);
        ivSound.setImageBitmap(ResInit.configImage[1]);
        addView(ivSound, lp);
    }

    private void initDifficultySwitch() {
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        ivDifficulty = new ImageView(context);
        ivDifficulty.setImageBitmap(ResInit.configImage[1]);
        addView(ivDifficulty, lp);
    }

    private void initMenuBg() {
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        imgBg = new ImageView(context);
        imgBg.setImageBitmap(ResInit.configImage[0]);
        addView(imgBg, lp);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (initialized) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        initialized = true;
        imgBg.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        offX = (MainWindow.windowWidth - imgBg.getMeasuredWidth()) / 2;
        offY = (MainWindow.windowHeight - imgBg.getMeasuredHeight()) / 2;

        backgroundRect = new Rect(offX, offY + 150, offX + imgBg.getMeasuredWidth(), offY + imgBg.getMeasuredHeight());

        Rect rect = RMS.difficulty == 1 ? clickRect[1] : clickRect[0];
        ivDifficulty.setLayoutParams(setPosition(rect, ivDifficulty.getLayoutParams()));

        Rect rect1 = RMS.loadSound ? clickRect[2] : clickRect[3];
        ivSound.setLayoutParams(setPosition(rect1, ivSound.getLayoutParams()));

        ivProgress.setLayoutParams(setProgress(clickRect[4], ivProgress));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private ViewGroup.LayoutParams setProgress(Rect rect, ImageView iv) {
        FrameLayout.LayoutParams lp = (LayoutParams) iv.getLayoutParams();
        rect = scaleRect(rect);
        lp.setMargins(rect.left, rect.top, 0, 0);
        lp.width = (int) (rect.width() * (RMS.volume / 0.3f));
        lp.height = rect.height();
        return lp;
    }


    int padding = 15;

    private ViewGroup.LayoutParams setPosition(Rect rect, ViewGroup.LayoutParams layoutParams) {
        FrameLayout.LayoutParams lp = (LayoutParams) layoutParams;
        rect = scaleRect(rect);
        lp.setMargins(rect.left + padding, rect.top + padding, 0, 0);
        return lp;
    }

    private Rect scaleRect(Rect rect) {
        return new Rect(rect.left + offX, rect.top + offY, rect.right + offX, rect.bottom + offY);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }


    private int downX;
    private int downY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (inRect(clickRect[4], downX, downY)) {
                    setProgressWidth(event, clickRect[4]);
                }
                break;
            case MotionEvent.ACTION_UP:
                //范音量条范围外,click
                if (!(inRect(clickRect[4], downX, downY)) && Math.abs(event.getX() - downX) <= 10 && Math.abs(event.getY() - downY) <= 10) {
                    handleClickEvent(event.getX(), event.getY());
                }
                break;
        }
        return true;
    }

    private void setProgressWidth(MotionEvent event, Rect rect) {
        if (!inRect(rect, event.getX(), event.getY())) {
            return;
        }
        FrameLayout.LayoutParams lp = (LayoutParams) ivProgress.getLayoutParams();
        rect = scaleRect(rect);
        lp.setMargins(rect.left, rect.top, 0, 0);
        lp.width = (int) (event.getX() - rect.left);
        ivProgress.setLayoutParams(lp);
        RMS.volume = 0.3f * (lp.width * 1.0f / clickRect[4].width());
    }

    private void handleClickEvent(float x, float y) {
        if (!inRect(backgroundRect, x, y, false)) {
            MainDataHolder.runState.setValue(RunState.Close_Config);
            RMS.saveConfigSetting();
            return;
        }
        if (inRect(clickRect[0], x, y)) {
            ivDifficulty.setLayoutParams(setPosition(clickRect[0], ivDifficulty.getLayoutParams()));
            RMS.difficulty = 0;
        } else if (inRect(clickRect[1], x, y)) {
            ivDifficulty.setLayoutParams(setPosition(clickRect[1], ivDifficulty.getLayoutParams()));
            RMS.difficulty = 1;
        } else if (inRect(clickRect[2], x, y)) {
            ivSound.setLayoutParams(setPosition(clickRect[2], ivSound.getLayoutParams()));
            RMS.loadSound = true;
            RMS.volume = 0.3f;
        } else if (inRect(clickRect[3], x, y)) {
            ivSound.setLayoutParams(setPosition(clickRect[3], ivSound.getLayoutParams()));
            RMS.loadSound = false;
            RMS.volume = 0;
        } else if (inRect(clickRect[5], x, y)) {
            MainDataHolder.runState.setValue(RunState.Close_Config);
        }
        RMS.saveConfigSetting();
    }


    public boolean inRect(Rect rect, float x, float y) {
        rect = scaleRect(rect);
        if (x >= rect.left && x <= rect.right && y >= rect.top && y <= rect.bottom) {
            return true;
        }
        return inRect(rect, x, y, true);
    }

    public boolean inRect(Rect rect, float x, float y, boolean scale) {
        if (scale) {
            rect = scaleRect(rect);
        }
        if (x >= rect.left && x <= rect.right && y >= rect.top && y <= rect.bottom) {
            return true;
        }
        return false;
    }
}
