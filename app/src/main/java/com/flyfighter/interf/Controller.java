package com.flyfighter.interf;

public interface Controller {
    void updatePosition(double x, double y);
    void playBomb();

    void continuePlay(int continueCount);

    void quit();
}
