package com.flyfighter.interf;

public interface Controller {
    void updatePosition(double x, double y);
    void playBomb();

    void setPause(int continueCount);

    void setPause(boolean continuePlay);

    void stopContinue();

    void removeAllRunnable();
    void handleQuit();
}
