package com.flyfighter.menu;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.flyfighter.interf.Controller;

public class GameController extends FrameLayout {
    private Controller controller;
    private int playBoomDuration = 350;//间隔为200毫秒

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
    long downTime;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                checkIfPlayBomb(event);
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

    private void checkIfPlayBomb(MotionEvent event) {
        if (downX == 0 || downY == 0) {
            return;
        }
        if (System.currentTimeMillis() - downTime >= playBoomDuration) {
            downTime = System.currentTimeMillis();
            return;
        }
        downTime = System.currentTimeMillis();
        if (Math.abs(downX - event.getX()) <= 10 && Math.abs(downY - event.getY()) <= 10) {
            controller.playBomb();
        }

    }

    private void handleCalculateMoveDirection(int moveX, int moveY) {
        int distance = (int) Math.sqrt(Math.pow(moveX - downX, 2) + Math.pow(moveY - downY, 2));
        controller.updatePosition((moveX - downX) * 1.0f / distance, (moveY - downY) * 1.0f / distance);
    }
}
