package com.flyfighter.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.flyfighter.res.ResInit;

public class RankingMenu extends FrameLayout {

    Context context;

    public RankingMenu(Context context) {
        this(context, null);
    }

    public RankingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RankingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        ResInit.initRanking(context);
        initMenuItem();
    }

    private void initMenuItem() {
        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(ResInit.rankingImage);
        addView(imageView);
    }
}
