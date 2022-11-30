package com.flyfighter.res;

import android.content.Context;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SoundHelper {

    public int poolSize = 5;

    public List<MediaPlayer> mediaPlayerPool = new ArrayList<>();

    public String[] soundRes = new String[13];

    public void loadGameSounds(Context context, MediaPlayer[] mediaPlayers) {
        try {
            initializeMediaPlayer();
            soundRes[0] = "sound/1explode1.wav";
            soundRes[1] = "sound/2explode2.wav";
            soundRes[2] = "sound/3explode3.wav";
            soundRes[3] = "sound/4bossappear.wav";
            soundRes[4] = "sound/5itemlife.wav";
            soundRes[5] = "sound/6shoot1.wav";
            soundRes[6] = "sound/7shoot2.wav";
            soundRes[7] = "sound/8shoot3.wav";
            soundRes[9] = "sound/10missile1.wav";
            soundRes[10] = "sound/11missile2.wav";
            soundRes[11] = "sound/12gameover.wav";
            soundRes[12] = "sound/14stagemusic.wav";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initializeMediaPlayer() {
        for (int i = 0; i < poolSize; i++) {
            mediaPlayerPool.add(new MediaPlayer());
        }
    }

    int playCount = 0;

    public void playSound(Context context, int index) throws Exception {
        Log.d("TAG", "playSound: "+mediaPlayerPool.size());
        String source = soundRes[index];
        AssetManager assets = context.getAssets();

        MediaPlayer player;

        if (mediaPlayerPool.size() == 0) {
            player = new MediaPlayer();
        } else {
            player = mediaPlayerPool.get(0);
        }
        if (RMS.loadSound) {
            player.setVolume(RMS.volume, RMS.volume);
        } else {
            player.setVolume(0, 0);
        }
        if (index == 3) {
            player.setLooping(true);
        }

        player.setDataSource(assets.openFd(source));
        player.setOnPreparedListener(mp -> {
            player.start();
        });
        player.setOnCompletionListener(mp -> {
            if (index == 3) {
                playCount++;
                if (playCount >= 5) {
                    playCount = 0;
                    mp.reset();
                    mediaPlayerPool.add(player);
                }
            } else {
                mp.reset();
                mediaPlayerPool.add(player);
            }
        });
        mediaPlayerPool.remove(player);
        player.prepareAsync();
    }
}
