package com.flyfighter.res;

import android.content.Context;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

public class SoundHelper {
    public static void loadGameSounds(Context context, MediaPlayer[] mediaPlayers) {
        initializeMediaPlayer(mediaPlayers);
        try {
            AssetManager assets = context.getAssets();

            mediaPlayers[0].setDataSource(assets.openFd("sound/1explode1.wav"));
            mediaPlayers[3].setDataSource(assets.openFd("sound/4bossappear.wav"));
            mediaPlayers[4].setDataSource(assets.openFd("sound/5itemlife.wav"));
            mediaPlayers[5].setDataSource(assets.openFd("sound/6shoot1.wav"));
            mediaPlayers[6].setDataSource(assets.openFd("sound/7shoot2.wav"));
            mediaPlayers[7].setDataSource(assets.openFd("sound/8shoot3.wav"));

            mediaPlayers[9].setDataSource(assets.openFd("sound/10missile1.wav"));
            mediaPlayers[10].setDataSource(assets.openFd("sound/11missile2.wav"));
            mediaPlayers[11].setDataSource(assets.openFd("sound/12gameover.wav"));
            mediaPlayers[12].setDataSource(assets.openFd("sound/14stagemusic.wav"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initializeMediaPlayer(MediaPlayer[] mediaPlayers) {
        if (mediaPlayers == null) {
            return;
        }
        for (int i = 0; i < mediaPlayers.length; i++) {
            mediaPlayers[i] = new MediaPlayer();
            if (RMS.loadSound) {
                mediaPlayers[i].setVolume(RMS.volume, RMS.volume);
            } else {
                mediaPlayers[i].setVolume(0, 0);
            }

        }
    }
}
