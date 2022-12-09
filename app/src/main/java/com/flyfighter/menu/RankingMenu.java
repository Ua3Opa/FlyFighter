package com.flyfighter.menu;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.flyfighter.enums.RunState;
import com.flyfighter.holder.MainDataHolder;
import com.flyfighter.res.RMS;
import com.flyfighter.res.ResInit;
import com.flyfighter.room.PlayRecord;
import com.flyfighter.view.MainWindow;

public class RankingMenu extends FrameLayout {

    Context context;
    private ImageView imageView;
    private boolean loaded;

    public RankingMenu(Context context) {
        this(context, null);
    }

    public RankingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RankingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        try {
            ResInit.initStrNumber(context.getAssets());
            init(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(Context context) {
        this.context = context;
        ResInit.initRanking(context);
        initMenuItem();
    }

    private void initMenuItem() {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        imageView = new ImageView(context);
        imageView.setImageBitmap(ResInit.rankingImage);
        addView(imageView, lp);

        imageView.setOnClickListener(v -> {
            MainDataHolder.runState.setValue(RunState.Close_Ranking);
        });
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (RMS.playRecords.isEmpty() || loaded) {
            return;
        }
        loaded = true;
        handleAddTop();

        if (RMS.playRecords.size() >= 10) {
            RMS.playRecords = RMS.playRecords.subList(0, 10);
        }

        int offsetY = (getMeasuredHeight() - imageView.getMeasuredHeight()) / 2 + 490;
        for (PlayRecord playRecord : RMS.playRecords) {
            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            rlp.setMargins(270, offsetY, 0, 0);
            rlp.addRule(RelativeLayout.ALIGN_START);
            ViewGroup item = buildItem(playRecord);
            addView(item, rlp);
            item.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            offsetY += item.getMeasuredHeight() + 8;//+padding
        }

    }

    private void handleAddTop() {
        int offsetY = (getMeasuredHeight() - imageView.getMeasuredHeight()) / 2;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(500, offsetY + 260, 0, 0);
        lp.addRule(RelativeLayout.ALIGN_START);

        ViewGroup item0 = buildTop(RMS.playRecords.get(0));
        addView(item0, lp);
        item0.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
    }


    private ViewGroup buildTop(PlayRecord record) {
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        RelativeLayout rl = new RelativeLayout(context);
        ImageView ivLevel = new ImageView(context);
        ivLevel.setImageBitmap(ResInit.levelImage[record.scoreLevel]);
        rl.addView(ivLevel, rlp);

        rlp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rlp.setMargins(150, 0, 0, 0);
        ImageView ivCharacter = new ImageView(context);
        ivCharacter.setImageBitmap(ResInit.characterImage[record.role]);
        rl.addView(ivCharacter, rlp);

        rlp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        int color = RMS.playRecords.indexOf(record) == 0 ? 2 : 1;
        ViewGroup score = buildNumViewGroup(String.valueOf(record.score), color);
        rlp.setMargins(0, 105, 0, 0);
        rl.addView(score, rlp);
        return rl;
    }

    private ViewGroup buildItem(PlayRecord record) {
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        RelativeLayout rl = new RelativeLayout(context);
        ImageView ivLevel = new ImageView(context);
        ivLevel.setImageBitmap(ResInit.levelImage[record.scoreLevel]);
        rl.addView(ivLevel, rlp);

        rlp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rlp.setMargins(150, 0, 0, 0);
        ImageView ivCharacter = new ImageView(context);
        ivCharacter.setImageBitmap(ResInit.characterImage[record.role]);
        rl.addView(ivCharacter, rlp);

        rlp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        int color = RMS.playRecords.indexOf(record) == 0 ? 2 : 1;
        ViewGroup score = buildNumViewGroup(String.valueOf(record.score), color);
        rlp.setMargins(340, 20, 0, 0);
        rl.addView(score, rlp);
        return rl;
    }

    /**
     * @param score
     * @param color 0,1,2: 白蓝红
     * @return
     */
    public ViewGroup buildNumViewGroup(String score, int color) {
        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        Bitmap[] source = ResInit.numberImageValue[color];

        String[] split = score.split("");

        int numberCount = 0;
        for (String spNum : split) {
            if (spNum == null || "".equals(spNum)) {
                continue;
            }
            ImageView ivNum = new ImageView(context);
            ivNum.setImageBitmap(source[Integer.valueOf(spNum)]);
            ll.addView(ivNum);
            numberCount++;
        }
        if (numberCount < 8) {
            for (int i = 8 - numberCount; i > 0; i--) {
                ImageView ivNum = new ImageView(context);
                ivNum.setImageBitmap(source[0]);
                ll.addView(ivNum,0);
            }
        }
        return ll;
    }


}
