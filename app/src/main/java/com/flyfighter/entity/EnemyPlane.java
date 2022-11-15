package com.flyfighter.entity;

public class EnemyPlane {
    public int type;
    public int colors;
    public int x;
    public int y;
    public int speedX;
    public int speedY;
    public int offset;
    public int health;
    public int aliveTime;
    public int pauseDelay;
    public int picId;
    public int item;
    public int reward;
    public int fireDelay;
    public int bulletMax;
    public int bulletType;
    public int explode;

    public EnemyPlane() {
        this.type = 0;
        this.colors = 0;
        this.x = 0;
        this.y = 0;
        this.speedX = 0;
        this.speedY = 0;
        this.offset = 0;
        this.health = 0;
        this.aliveTime = 0;
        this.pauseDelay = 0;
        this.picId = 0;
        this.item = 0;
        this.reward = 0;
        this.fireDelay = 0;
        this.bulletMax = 0;
        this.bulletType = 0;
        this.explode = 0;
    }


    public EnemyPlane(int type, int colors, int x, int y, int speedX, int speedY, int offset, int health, int aliveTime, int pauseDelay, int picId, int item, int reward, int fireDelay, int bulletMax, int bulletType, int explode) {
        this.type = type;
        this.colors = colors;
        this.x = x;
        this.y = y;
        this.speedX = speedX;
        this.speedY = speedY;
        this.offset = offset;
        this.health = health;
        this.aliveTime = aliveTime;
        this.pauseDelay = pauseDelay;
        this.picId = picId;
        this.item = item;
        this.reward = reward;
        this.fireDelay = fireDelay;
        this.bulletMax = bulletMax;
        this.bulletType = bulletType;
        this.explode = explode;
    }


    public void setValue(int type, int colors, int x, int y, int speedX, int speedY, int offset, int health, int aliveTime, int pauseDelay, int picId, int item, int reward, int fireDelay, int bulletMax, int bulletType, int explode) {
        this.type = type;
        this.colors = colors;
        this.x = x;
        this.y = y;
        this.speedX = speedX;
        this.speedY = speedY;
        this.offset = offset;
        this.health = health;
        this.aliveTime = aliveTime;
        this.pauseDelay = pauseDelay;
        this.picId = picId;
        this.item = item;
        this.reward = reward;
        this.fireDelay = fireDelay;
        this.bulletMax = bulletMax;
        this.bulletType = bulletType;
        this.explode = explode;
    }


    public void setValue(int value) {
        this.type = value;
        this.colors = value;
        this.x = value;
        this.y = value;
        this.speedX = value;
        this.speedY = value;
        this.offset = value;
        this.health = value;
        this.aliveTime = value;
        this.pauseDelay = value;
        this.picId = value;
        this.item = value;
        this.reward = value;
        this.fireDelay = value;
        this.bulletMax = value;
        this.bulletType = value;
        this.explode = value;
    }
}
