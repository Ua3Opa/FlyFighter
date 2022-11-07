package com.flyfighter.menu;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.flyfighter.res.ResInit;
import com.flyfighter.view.MainWindow;

public class SelectPlayerMenu extends FrameLayout {

    Context context;
    public  int selectPlayer = 0;
    private int rectIndex;
    Handler handler = new Handler(msg -> {
        switch (msg.what) {
            case 0:
                handleChangeBackgroundImg();
                break;
        }
        return false;
    });
    private ImageView ivRect;

    public SelectPlayerMenu(Context context) {
        this(context, null);
    }

    public SelectPlayerMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectPlayerMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        initSelectImage();
    }

    private void initSelectImage() {
        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(ResInit.playerSelectImage[selectPlayer]);
        addView(imageView);

        ivRect = new ImageView(context);
        ivRect.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        addView(ivRect);
        handler.sendEmptyMessage(0);


    }

    private void handleChangeBackgroundImg() {
        rectIndex++;
        ivRect.setImageBitmap(ResInit.playerSelectImage[3 + rectIndex % 2]);
        FrameLayout.LayoutParams params = (LayoutParams) ivRect.getLayoutParams();
        params.gravity = Gravity.LEFT | Gravity.BOTTOM;
        params.leftMargin = getWidth() / 2 - 90 + 190 * this.selectPlayer;
        ivRect.setLayoutParams(params);
        handler.sendEmptyMessageDelayed(0, 500);
    }


    private int downX;
    private int downY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                //click
                if (Math.abs(event.getX() - downX) <= 10 && Math.abs(event.getY() - downY) <= 10) {
                    handleSelectPlayerByPosition(downX, downY);
                }
                break;
        }
        return true;
    }

    public void handleSelectPlayerByPosition(int x, int y) {
        if (y <= getHeight() - ivRect.getHeight()) {
            return;
        }
        if (x < getWidth() / 2 - 90 || x > getWidth() / 2 - 90 + 190 * 3) {
            return;
        }
        int selectNum = (x - (getWidth() / 2 - 90)) / 190;
        if (selectPlayer == selectNum) {
            ((MainWindow) getParent()).startGame(this,selectPlayer);
        } else {
            selectPlayer = selectNum;
            handler.sendEmptyMessage(0);
        }
    }
}
