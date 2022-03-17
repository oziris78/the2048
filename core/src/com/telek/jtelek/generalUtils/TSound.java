package com.telek.jtelek.generalUtils;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Timer;

public class TSound {

    private Sound sound;
    private boolean isRunning;

    public TSound(Sound sound) {
        this.sound = sound;
        isRunning = false;
    }

    public void play(float waitingTimeInSeconds) {
        if (isRunning) return;
        this.sound.play();
        this.isRunning = true;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                isRunning = false;
            }
        }, waitingTimeInSeconds);
    }

    public void play(float waitingTimeInSeconds, float volume) {
        if (isRunning) return;
        this.sound.play(volume);
        this.isRunning = true;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                isRunning = false;
            }
        }, waitingTimeInSeconds);
    }

    public void play(float waitingTimeInSeconds, float volume, float pitch, float pan) {
        if (isRunning) return;
        this.sound.play(volume, pitch, pan);
        this.isRunning = true;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                isRunning = false;
            }
        }, waitingTimeInSeconds);
    }


    public Sound getSound() {
        return sound;
    }

    public boolean isRunning() {
        return isRunning;
    }


}
