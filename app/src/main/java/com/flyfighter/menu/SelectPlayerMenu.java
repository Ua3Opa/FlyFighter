package com.flyfighter.menu;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.flyfighter.res.ResInit;
import com.flyfighter.view.MainWindow;

public class SelectPlayerMenu extends FrameLayout {

    Context context;
    public int selectPlayer = 0;
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
    private ImageView ivPlayerBg;

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
        ivPlayerBg = new ImageView(context);
        ivPlayerBg.setImageBitmap(ResInit.playerSelectImage[selectPlayer]);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        ivPlayerBg.setLayoutParams(layoutParams);
        addView(ivPlayerBg);

        ivRect = new ImageView(context);
        ivRect.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        addView(ivRect);
        handler.sendEmptyMessage(0);
    }

    private void handleChangeBackgroundImg() {
        rectIndex++;
        ivPlayerBg.setImageBitmap(ResInit.playerSelectImage[selectPlayer]);
        ivRect.setImageBitmap(ResInit.playerSelectImage[3 + rectIndex % 2]);
        FrameLayout.LayoutParams params = (LayoutParams) ivRect.getLayoutParams();
        params.gravity = Gravity.LEFT | Gravity.BOTTOM;
        params.leftMargin = getWidth() / 2 - 90 + 195 * this.selectPlayer;
        params.bottomMargin = (int) (getHeight() - ivPlayerBg.getY() - ivPlayerBg.getHeight());
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

    /**
     * 这里是相对坐标没有影响
     * @param x
     * @param y
     */
    public void handleSelectPlayerByPosition(int x, int y) {
        int playerBgBottom = (int) (ivPlayerBg.getY() + ivPlayerBg.getHeight());
        if(y< ivPlayerBg.getY() || y > playerBgBottom){
            ((ViewGroup)getParent()).removeView(this);
            return;
        }

        if (y < (playerBgBottom - ivRect.getHeight()) || y > playerBgBottom) {
            return;
        }

        if (x < getWidth() / 2 - 90 || x > getWidth() / 2 - 90 + 195 * 3) {
            return;
        }

        int selectNum = (x - (getWidth() / 2 - 90)) / 195;
        if (selectPlayer == selectNum) {
            ((MainWindow) getParent()).startGame(this, selectPlayer);
        } else {
            selectPlayer = selectNum;
            handler.sendEmptyMessage(0);
        }
    }
}
