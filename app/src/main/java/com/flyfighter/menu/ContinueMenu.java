package com.flyfighter.menu;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyfighter.interf.Controller;
import com.flyfighter.res.RMS;
import com.flyfighter.res.ResInit;
import com.flyfighter.view.MainWindow;


public class ContinueMenu extends FrameLayout {

    Context context;
    int counter = 9;
    Handler handler = new Handler(Looper.getMainLooper(),new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    counter--;
                    if (counter <= 0) {
                        handler.removeCallbacksAndMessages(null);
                        ((MainWindow) getParent()).showRanking();
                        ((MainWindow) getParent()).removeView(ContinueMenu.class);
                        return false;
                    }
                    ivCounter.setImageBitmap(ResInit.numberImageValue[0][counter]);
                    handler.sendEmptyMessageDelayed(0, 1000);
                    break;
            }
            return false;
        }
    });

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
    Controller controller;
    int continueNum;
    Rect backgroundRect;
    private RelativeLayout rlContent;
    private ImageView ivGameOver;
    private ImageView ivContinue;
    private ImageView ivCounter;
    private TextView tvContinue;
    private TextView tvQuit;
    private RelativeLayout rlButton;


    public ContinueMenu(Context context, Controller controller, int continueNum) {
        super(context, null, 0);
        this.controller = controller;
        this.continueNum = continueNum;
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        ResInit.initConfigMenu(context);
        initMenuGroup();
        initImageBitmap();
    }

    private void initImageBitmap() {
        RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        ivGameOver = new ImageView(context);
        ivGameOver.setImageBitmap(ResInit.otherImage[6]);
        rlContent.addView(ivGameOver, rl);

        ivContinue = new ImageView(context);
        ivContinue.setImageBitmap(ResInit.otherImage[4]);
        rlContent.addView(ivContinue, new RelativeLayout.LayoutParams(rl));

        ivCounter = new ImageView(context);
        ivCounter.setImageBitmap(ResInit.numberImageValue[0][counter]);
        rlContent.addView(ivCounter, new RelativeLayout.LayoutParams(rl));

        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        rlButton = new RelativeLayout(context);
        rlContent.addView(rlButton, lp);

        tvContinue = buildTextView(26);
        tvContinue.setText("Continue");
        rlButton.addView(tvContinue, new RelativeLayout.LayoutParams(rl));

        tvContinue.setOnClickListener(v -> {
            controller.continuePlay(continueNum--);
            handler.removeCallbacksAndMessages(null);
            ((MainWindow) getParent()).removeView(ContinueMenu.class);
        });

        tvQuit = buildTextView(26);
        tvQuit.setText("Quit");
        tvQuit.setOnClickListener(v -> {
            controller.quit();
            handler.removeCallbacksAndMessages(null);
            ((MainWindow) getParent()).removeView(ContinueMenu.class);
        });
        rlButton.addView(tvQuit, new RelativeLayout.LayoutParams(rl));

        handler.sendEmptyMessageDelayed(0, 1000);
    }

    private void initMenuGroup() {
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        rlContent = new RelativeLayout(context);
        addView(rlContent, lp);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChild(rlContent);
        measureChild(rlButton);

        RelativeLayout.LayoutParams lpGameOver = (RelativeLayout.LayoutParams) ivGameOver.getLayoutParams();
        lpGameOver.setMargins((MainWindow.windowWidth - ivGameOver.getMeasuredWidth()) / 2,
                MainWindow.windowHeight / 2 - ivGameOver.getMeasuredHeight() * 3, 0, 0);

        RelativeLayout.LayoutParams lpContinue = (RelativeLayout.LayoutParams) ivContinue.getLayoutParams();
        lpContinue.setMargins((MainWindow.windowWidth - ivContinue.getMeasuredWidth()) / 2,
                MainWindow.windowHeight / 2 - 50, 0, 0);

        RelativeLayout.LayoutParams lpCounter = (RelativeLayout.LayoutParams) ivCounter.getLayoutParams();
        lpCounter.setMargins((MainWindow.windowWidth - ivCounter.getMeasuredWidth()) / 2,
                MainWindow.windowHeight / 2 + ivContinue.getMeasuredHeight(), 0, 0);

        RelativeLayout.LayoutParams btnLp = (RelativeLayout.LayoutParams) rlButton.getLayoutParams();
        btnLp.width = tvContinue.getMeasuredWidth() + tvQuit.getMeasuredWidth() + 200;
        btnLp.setMargins((MainWindow.windowWidth - btnLp.width) / 2,
                MainWindow.windowHeight / 2 + ivContinue.getMeasuredHeight() + 140, 0, 0);

        RelativeLayout.LayoutParams lpTvContinue = (RelativeLayout.LayoutParams) tvContinue.getLayoutParams();
        lpTvContinue.addRule(RelativeLayout.ALIGN_PARENT_START);

        RelativeLayout.LayoutParams lpQuit = (RelativeLayout.LayoutParams) tvQuit.getLayoutParams();
        lpQuit.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void measureChild(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            viewGroup.getChildAt(i).measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        }
    }

    public TextView buildTextView(int textSize) {
        TextView textView = new TextView(context);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        return textView;
    }

}
