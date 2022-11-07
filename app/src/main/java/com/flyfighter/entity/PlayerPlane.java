package com.flyfighter.entity;

public class PlayerPlane {
    public int type;
    public int x;
    public int y;
    public int speed;
    public int life;
    public int bomb;
    public int picId;
    public int power;

    public PlayerPlane() {
    }

    public void setValue(int type, int x, int y, int speed, int life, int bomb, int picId, int power) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.life = life;
        this.bomb = bomb;
        this.picId = picId;
        this.power = power;
    }


}
