package com.example.matthew.demo2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.Math;

import com.thalmic.myo.*;

public class MyActivity extends Activity {

    private static final String TAG = "Error";

    Context mContext = MyActivity.this;

    TextView yaw, pitch, roll, zone_text;
    final double [] NOTES = {220.00, 261.626, 293.665, 329.628, 391.995, 440.0};
    int note_zone = 0;

    float yaw_off = 0, pitch_off = 0, roll_off = 0;
    boolean first_run = true;

    int lastZone = 0;

    XDirection mXDirection = XDirection.UNKNOWN;
    private Arm mArm = Arm.UNKNOWN;

    int processed_pitch = 0;

    private DeviceListener mListener = new AbstractDeviceListener() {
        PlaySound audio;

        @Override
        public void onConnect(Myo myo, long timestamp) {
            Toast.makeText(mContext, "Myo Connected!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDisconnect(Myo myo, long timestamp) {
            Toast.makeText(mContext, "Myo Disconnected!", Toast.LENGTH_SHORT).show();
        }

        // onArmRecognized() is called whenever Myo has recognized a setup gesture after someone has put it on their
        // arm. This lets Myo know which arm it's on and which way it's facing.
        @Override
        public void onArmRecognized(Myo myo, long timestamp, Arm arm, XDirection xDirection) {
            mArm = arm;
            mXDirection = xDirection;
        }

        @Override
        public void onArmLost(Myo myo, long timestamp) {
            mArm = Arm.UNKNOWN;
            mXDirection = XDirection.UNKNOWN;
        }

        @Override
        public void onPose(Myo myo, long timestamp, Pose pose) {
            Toast.makeText(mContext, "Pose: " + pose, Toast.LENGTH_SHORT).show();

            //TODO: Do something awesome.
        }

        @Override
        public void onOrientationData(Myo myo, long timestamp, Quaternion rotation) {

            // Calculate Euler angles (roll, pitch, and yaw) from the quaternion.
            float _roll = (float) Math.toDegrees(Quaternion.roll(rotation));
            float _pitch = (float) Math.toDegrees(Quaternion.pitch(rotation));
            float _yaw = (float) Math.toDegrees(Quaternion.yaw(rotation));

            if ( first_run ) {
                pitch_off = - _pitch;
                roll_off  = - _roll;
                yaw_off   = - _yaw;
                first_run = false;

                audio = new PlaySound();
                audio.start();
            }
            // Adjust roll and pitch for the orientation of the Myo on the arm.
            if (mXDirection == XDirection.TOWARD_ELBOW) {
                _roll *= -1;
                _pitch *= -1;
            }

            _yaw = _yaw;
            _roll = _roll + roll_off;
            _pitch = _pitch + pitch_off;
            // we display the yaw, pitch and roll.
            //yaw.setText("" + _yaw);
            //pitch.setText("" + (_pitch ) );
            //roll.setText("" + (_roll) );

            float pitch_offset = (float) Math.pow(_pitch / 3, 1.0f);

            if ( _yaw < 30 ){
                note_zone = 0;
            }
            else if (_yaw < 60) {
                note_zone = 1;
            }
            else if ( _yaw < 90) {
                note_zone = 2;
            }
            else if ( _yaw < 120) {
                note_zone = 3;
            }
            else if (_yaw < 150){
                note_zone = 4;
            }
            else {
                note_zone = 5;
            }

            if(lastZone != note_zone){
                myo.vibrate(Myo.VibrationType.SHORT);
                lastZone = note_zone;
            }

            //graphView.addSeries(new GraphViewSeries(new GraphView.GraphViewData[] { new GraphView.GraphViewData(1, NOTES[note_zone] + pitch_offset) }));

            audio.setFrequency(NOTES[note_zone] + pitch_offset); //where y is the offset and x is the zone
        }
    };

            @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);


                Hub hub = Hub.getInstance();
                if (!hub.init(this)) {
                    Log.e(TAG, "Could not initialize the Hub.");
                    finish();
                    return;
                }

                hub.pairWithAdjacentMyo();

                hub.addListener(mListener);
            }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
