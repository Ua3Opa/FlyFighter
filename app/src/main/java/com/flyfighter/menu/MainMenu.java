package com.flyfighter.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;

public class MainMenu extends SurfaceView {
    public MainMenu(Context context) {
        this(context, null);
    }

    public MainMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MainMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
