package tw.org.iii.brad.brad36;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.CompoundButton;

public class MainActivity extends AppCompatActivity {
    private Vibrator vibrator;
    private SwitchCompat switchCompat;
    private CameraManager cameraManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init(){
        cameraManager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
        switchCompat = findViewById(R.id.switchLight);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //
                    if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                        onFlashLight();
                    }else {
                        setOnLight();
                    }
                }else{
                    if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                        offFlashLight();
                    }else {
                        setOffLight();
                    }
                }
            }
        });

        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
    }

    private Camera camera;
    private void setOnLight(){
        camera = Camera.open();
        Camera.Parameters p = camera.getParameters();
        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(p);
        camera.startPreview();
    }
    private void setOffLight(){
        camera.stopPreview();
        camera.release();
    }

    private void onFlashLight(){
        try {
            cameraManager.setTorchMode("0", true);
        }catch (Exception e){

        }
    }
    private void offFlashLight(){
        try {
            cameraManager.setTorchMode("0", false);
        }catch (Exception e){

        }
    }

    public void test1(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            vibrator.vibrate(
                    VibrationEffect.createOneShot(1*1000,
                            VibrationEffect.DEFAULT_AMPLITUDE));
        }else {
            vibrator.vibrate(1 * 1000);
        }
    }

    public void test2(View view){
        int dot = 200;      // Length of a Morse Code "dot" in milliseconds
        int dash = 500;     // Length of a Morse Code "dash" in milliseconds
        int short_gap = 200;    // Length of Gap Between dots/dashes
        int medium_gap = 500;   // Length of Gap Between Letters
        int long_gap = 1000;    // Length of Gap Between Words
        long[] pattern = {
                0,  // Start immediately
                dot, short_gap, dot, short_gap, dot,    // s
                medium_gap,
                dash, short_gap, dash, short_gap, dash, // o
                medium_gap,
                dot, short_gap, dot, short_gap, dot,    // s
                long_gap
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(pattern, -1);
        }else{
            vibrator.vibrate(pattern, 0);
        }

    }



}
