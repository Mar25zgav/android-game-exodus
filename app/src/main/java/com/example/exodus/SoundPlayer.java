package com.example.exodus;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundPlayer {
    private MediaPlayer mediaPlayer;
    private Context context;
    private boolean isReleased;

    public SoundPlayer(Context context) {
        this.context = context;
        isReleased = false;
    }

    public void playMainMenu() {
        if (!isPlaying()) {
            mediaPlayer = MediaPlayer.create(context, R.raw.mainmenu);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
            isReleased = false;
        }
    }

    public void playGame() {
        mediaPlayer = MediaPlayer.create(context, R.raw.game);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        isReleased = false;
    }

    public void playGameOver() {
        mediaPlayer = MediaPlayer.create(context, R.raw.gameover);
        mediaPlayer.start();
        isReleased = false;
    }

    public void stopPlaying() {
        if (isPlaying() && mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            isReleased = true;
        }
    }

    public void pauseMusic() {
        if (isPlaying() && mediaPlayer != null)
            mediaPlayer.pause();
    }

    public void resetMusic() {
        if (isPlaying() && mediaPlayer != null) {
            mediaPlayer.seekTo(0);
        }
    }

    private boolean isPlaying() {
        if (!isReleased && mediaPlayer != null)
            return mediaPlayer.isPlaying();
        return false;
    }
}
