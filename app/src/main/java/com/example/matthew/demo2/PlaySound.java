package com.example.matthew.demo2;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * Created by dongs_000 on 2014-09-20.
 */
public class PlaySound extends Thread{
    private static final int sampleRate = 4000;
    private static final int numSamples = 2000;
    byte [] sound;

    AudioTrack audio;

    int targetSampleRate = 4000;
    int currentSampleRate = 4000;

    public PlaySound (){
        double[] soundSample = new double[numSamples];
        byte[] sound = new byte[2 * numSamples];

        for (int i = 0; i < numSamples; i++) {
            soundSample[i] = Math.sin((2.00 * Math.PI) * ((double)i / (sampleRate/100.00)));
            short val = (short)(soundSample[i] * 32767.00);
            sound[i] = (byte) (val & 0x00ff);
            sound[i++] = (byte) ((val & 0xff00) >>> 8);
            i++;
        }

        audio = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, AudioFormat.CHANNEL_OUT_DEFAULT,
                AudioFormat.ENCODING_PCM_16BIT, numSamples, AudioTrack.MODE_STATIC);
        audio.write(sound, 0, 2000);

        setFrequency(100);
        audio.setLoopPoints (0, 900, -1);//HOW THE FUCK DID THIS HAPPEN?????????????
    }

    public void run(){
        audio.play();
        while(true){
            if (targetSampleRate > currentSampleRate) currentSampleRate++;
            else currentSampleRate--;
            audio.setPlaybackRate(currentSampleRate);
        }
    }

    public void setFrequency(double hz){
        targetSampleRate = (int)(sampleRate * (hz/100));
    }
}
