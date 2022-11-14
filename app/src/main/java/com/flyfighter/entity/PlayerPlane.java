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

    public void setType(int type) {
        this.type = type;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public void setBomb(int bomb) {
        this.bomb = bomb;
    }

    public void setPicId(int picId) {
        this.picId = picId;
    }

    public void setPower(int power) {
        this.power = power;
    }
}
