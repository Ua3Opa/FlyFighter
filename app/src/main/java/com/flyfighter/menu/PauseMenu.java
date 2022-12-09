package com.flyfighter.menu;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyfighter.enums.RunState;
import com.flyfighter.holder.MainDataHolder;
import com.flyfighter.res.ResInit;
import com.flyfighter.view.MainWindow;


public class PauseMenu extends FrameLayout {

    Context context;
    private RelativeLayout rlContent;
    private TextView tvContinue;
    private TextView tvQuit;
    private RelativeLayout rlButton;
    private TextView tvGamePause;

    public PauseMenu(Context context) {
        super(context, null, 0);
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
        tvGamePause = buildTextView(32);
        tvGamePause.setText("Game Pause");
        rlContent.addView(tvGamePause, rl);

        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        rlButton = new RelativeLayout(context);
        rlContent.addView(rlButton, lp);

        tvContinue = buildTextView(26);
        tvContinue.setText("Continue");
        rlButton.addView(tvContinue, new RelativeLayout.LayoutParams(rl));

        tvContinue.setOnClickListener(v -> {
            MainDataHolder.runState.postValue(RunState.Running);
        });

        tvQuit = buildTextView(26);
        tvQuit.setText("Quit");
        tvQuit.setOnClickListener(v -> {
            MainDataHolder.runState.setValue(RunState.Quit);
        });
        rlButton.addView(tvQuit, new RelativeLayout.LayoutParams(rl));

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

        RelativeLayout.LayoutParams lpGameOver = (RelativeLayout.LayoutParams) tvGamePause.getLayoutParams();
        lpGameOver.setMargins((MainWindow.windowWidth - tvGamePause.getMeasuredWidth()) / 2,
                MainWindow.windowHeight / 2 - tvGamePause.getMeasuredHeight(), 0, 0);

        RelativeLayout.LayoutParams btnLp = (RelativeLayout.LayoutParams) rlButton.getLayoutParams();
        btnLp.width = tvContinue.getMeasuredWidth() + tvQuit.getMeasuredWidth() + 200;
        btnLp.setMargins((MainWindow.windowWidth - btnLp.width) / 2,
                MainWindow.windowHeight / 2 + tvGamePause.getMeasuredHeight() + 50, 0, 0);

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
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
        textView.setTextColor(Color.WHITE);
        return textView;
    }

}
