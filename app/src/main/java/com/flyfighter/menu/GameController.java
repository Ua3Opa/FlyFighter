package com.flyfighter.menu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Size;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.flyfighter.interf.Controller;

public class GameController extends FrameLayout {
    private Controller controller;
    private int playBoomDuration = 350;//间隔为200毫秒


    Size dockerSize = new Size(280, 280);
    Size pointerSize = new Size(50, 50);
    Size emptySize = new Size(80, 80);

    RectF dockerRect = new RectF();
    RectF pointerRect = new RectF();

    int backgroundColor = 0x66ffffff;
    int pointerColor = 0xaaffffff;
    int borderColor = 0x4Dffffff;
    private Paint mPaint;

    public GameController(Context context, Controller controller) {
        super(context);
        this.controller = controller;
        setBackgroundColor(0x00000000);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    int downX;
    int downY;
    long downTime;
    int pointerX;
    int pointerY;
    boolean showPointer;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                showPointer = true;
                checkIfPlayBomb(event);
                downX = (int) event.getX();
                downY = (int) event.getY();
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                pointerX = (int) event.getX();
                pointerY = (int) event.getY();
                fixPointerPos();
                handleCalculateMoveDirection(pointerX, pointerY);
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                controller.updatePosition(0, 0);
                showPointer = false;
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!showPointer) {
            return;
        }
        mPaint.setColor(backgroundColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawOval(calculateDockerPos(), mPaint);

        mPaint.setColor(pointerColor);
        canvas.drawOval(calculatePointerPos(), mPaint);

        mPaint.setColor(borderColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2.0f);
        canvas.drawOval(calculateDockerPos(), mPaint);
        canvas.drawOval(calculateBorderPos(emptySize, new RectF()), mPaint);
    }


    private void fixPointerPos() {
        int moveX = pointerX;
        int moveY = pointerY;
        int distance = (int) Math.sqrt(Math.pow(moveX - downX, 2) + Math.pow(moveY - downY, 2));
        int r = dockerSize.getWidth() / 2;//圆形范围
        int dirX = moveX - downX > 0 ? 1 : -1;
        int dirY = moveY - downY > 0 ? 1 : -1;
        if (distance >= r) {
            pointerX = (int) (Math.abs(moveX - downX) * (r * 1.0f / distance)) * dirX + downX;
            pointerY = (int) (Math.abs(moveY - downY) * (r * 1.0f / distance)) * dirY + downY;
        }
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
        if (Math.abs(downX - event.getX()) <= emptySize.getWidth() / 2 && Math.abs(downY - event.getY()) <= emptySize.getHeight() / 2) {
            controller.playBomb();
        }
    }

    private void handleCalculateMoveDirection(int moveX, int moveY) {
        int distance = (int) Math.sqrt(Math.pow(moveX - downX, 2) + Math.pow(moveY - downY, 2));
        controller.updatePosition((moveX - downX) * 1.0f / distance, (moveY - downY) * 1.0f / distance);
    }

    private RectF calculatePointerPos() {
        int left = pointerX - pointerSize.getWidth() / 2;
        int top = pointerY - pointerSize.getHeight() / 2;
        int right = left + pointerSize.getWidth();
        int bottom = top + pointerSize.getHeight();
        pointerRect.set(left, top, right, bottom);
        return pointerRect;
    }

    private RectF calculateBorderPos(Size size, RectF rectF) {
        int left = downX - size.getWidth() / 2;
        int top = downY - size.getHeight() / 2;
        int right = left + size.getWidth();
        int bottom = top + size.getHeight();
        rectF.set(left, top, right, bottom);
        return rectF;
    }

    public RectF calculateDockerPos() {
        int left = downX - dockerSize.getWidth() / 2;
        int top = downY - dockerSize.getHeight() / 2;
        int right = left + dockerSize.getWidth();
        int bottom = top + dockerSize.getHeight();
        dockerRect.set(left, top, right, bottom);
        return dockerRect;
    }
}
