package com.example.matthew.demo2;

/**
 * Created by dongs_000 on 2014-09-20.
 */
public class ThereminEngine {
    //Engine instantiated for playing sounds
    PlaySound audio;

    //Notes Constants
    final double A = 220.00;
    final double C = 261.626;
    final double D = 293.665;
    final double E = 329.628;
    final double G = 391.995;
    final double A2 = 440.000;

    final double [] NOTES = {220.00, 261.626, 293.665, 329.628, 391.995, 440.0};

    //State Variables
    int zone = 0;
    float offset = 0;
    //CONSTRUCTOR: Instantiates Audio Engine
    public ThereminEngine(){
        audio = new PlaySound();
        audio.run();
    }

    //Set Zone/Note
    //int z: z = [0, 5]
    public void setZone(int z){
        zone = z;
        updateAudio();
    }

    //Set the offset pitch from perfect
    public void setOffset(int os){
        offset = os;
        updateAudio();
    }

    //PRIVATES
    private void updateAudio(){
        audio.setFrequency(NOTES[zone] + offset);
    }
}
