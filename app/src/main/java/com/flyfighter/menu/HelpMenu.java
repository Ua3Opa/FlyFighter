package com.flyfighter.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.flyfighter.res.ResInit;
import com.flyfighter.view.MainWindow;

public class HelpMenu extends FrameLayout {

    Context context;
    private ImageView imageView;
    private int helpIndex;

    public HelpMenu(Context context) {
        this(context, null);
    }

    public HelpMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HelpMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        ResInit.initHelpMenu(context);
        initMenuItem();
    }

    private void initMenuItem() {
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        imageView = new ImageView(context);
        imageView.setImageBitmap(ResInit.helpImage[0]);
        addView(imageView, lp);

        imageView.setOnClickListener(v -> {
            helpIndex++;
            if (helpIndex < 3) {
                imageView.setImageBitmap(ResInit.helpImage[helpIndex]);
            } else {
                ((MainWindow) getParent()).removeView(ConfigMenu.class);
            }
        });
    }
}
