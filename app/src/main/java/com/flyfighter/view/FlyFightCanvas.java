package com.flyfighter.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;

public class FlyFightCanvas extends SurfaceView {
    public FlyFightCanvas(Context context) {
        this(context, null);
    }

    public FlyFightCanvas(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlyFightCanvas(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public FlyFightCanvas(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
