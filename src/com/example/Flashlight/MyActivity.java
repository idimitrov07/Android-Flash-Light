package com.example.Flashlight;

import android.app.Activity;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

public class MyActivity extends Activity {

    ImageButton btnSwitch;
    private Camera camera;
    private boolean isFlashOn;
    android.hardware.Camera.Parameters params;
    MediaPlayer mp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.main);

        btnSwitch = (ImageButton) findViewById(R.id.btnSwitch);

        //get the camera
        getCamera();

        btnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFlashOn){
                    turnOffFlash();
                } else {
                    turnOnFlash();
                }
            }
        });
    }

    //get camera parameters
    private void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                params = camera.getParameters();
            } catch (RuntimeException e) {
                Log.e("Camera error. Failed to open. Error: ", e.getMessage());
            }
        }
    }

    //turn on the Flash
    private void turnOnFlash(){
        if(!isFlashOn) {
            if(camera == null && params == null) {
                return;
            }
            //play sound
            playSound();

            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;
        }
    }

    //turn on the Flash
    private void turnOffFlash() {
        if(isFlashOn) {
            if (camera == null || params == null) {
                return;
            }
            //play sound
            playSound();

            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            isFlashOn = false;
        }
    }

    //play sound
    private void playSound(){
        mp = MediaPlayer.create(MyActivity.this, R.raw.sound);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mp.release();
            }
        });
        mp.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getCamera();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        turnOnFlash();
    }

    @Override
    protected void onPause() {
        super.onPause();
        turnOffFlash();
    }

    @Override
    protected void onStop() {
        super.onStop();

        //release the camera
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
