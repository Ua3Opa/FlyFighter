package com.flyfighter.menu;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

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
            //音量小
            new Rect(530, 195, 720, 300),
            //音量大
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
    private int offX;
    private int offY;
    private ImageView ivProgress;

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

        imgBg.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        offX = (MainWindow.windowWidth - imgBg.getMeasuredWidth()) / 2;
        offY = (MainWindow.windowHeight - imgBg.getMeasuredHeight()) / 2;

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
        lp.width = rect.width();
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
        rect.left += offX;
        rect.top += offY;
        return rect;
    }

}
