package com.flyfighter.menu;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.flyfighter.interf.Controller;

public class GameController extends FrameLayout {
    private Controller controller;

    public GameController(Context context, Controller controller) {
        super(context);
        this.controller = controller;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    int downX;
    int downY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getX();
                int moveY = (int) event.getY();
                handleCalculateMoveDirection(moveX, moveY);
                return true;
            case MotionEvent.ACTION_UP:
                controller.updatePosition(0, 0);
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void handleCalculateMoveDirection(int moveX, int moveY) {
        int distance = (int) Math.sqrt(Math.pow(moveX - downX, 2) + Math.pow(moveY - downY, 2));
        controller.updatePosition((moveX - downX) * 1.0f / distance, (moveY - downY) * 1.0f / distance);
    }
}
