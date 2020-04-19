package tw.org.iii.brad.brad36;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.PixelCopy;
import android.view.View;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileOutputStream;

public class CameraActivity extends AppCompatActivity {
    private Camera camera;
    private CameraPreview cameraPreview;
    private FrameLayout container;
    private File sdroot;
    private SensorManager sensorManager;
    private Sensor sensor;
    private MyListener myListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        myListener = new MyListener();

        sdroot = Environment.getExternalStorageDirectory();

        camera = getCameraInstance();
        //camera.getParameters().set

        container = findViewById(R.id.preview);
        cameraPreview = new CameraPreview(this, camera);
        container.addView(cameraPreview,0);
        //camera.setDisplayOrientation(90);

//        int r = getWindowManager().getDefaultDisplay().getRotation();
//        Log.v("brad", "r = " + r);
//        if (r == 0){
//            camera.setDisplayOrientation(90);
//        }else if(r == 1) {
//            camera.setDisplayOrientation(0);
//        }else {
//            camera.setDisplayOrientation(180);
//        }


        sensorManager.registerListener(myListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);


    }

    private class MyListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            float v = values[0];
            Log.v("brad", "o = " + v);
            try {
                camera.setDisplayOrientation((int) v);
            }catch (Exception e){

            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(myListener);
        releaseCamera();
    }

    private void releaseCamera(){
        if (camera != null){
            camera.release();        // release the camera for other applications
            camera = null;
        }
    }

    public void takPic(View view) {

        camera.takePicture(new Camera.ShutterCallback() {
            @Override
            public void onShutter() {
                Log.v("brad", "shutter");
            }
        }, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Log.v("brad", "debug1");
            }
        }, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Log.v("brad", "debug2");
                savePic(data);
            }
        });
    }

    private void savePic(byte[] data) {
        Log.v("brad", "file : " + data.length);
        try {
            FileOutputStream fout = new FileOutputStream(
                    new File(sdroot, "iii02.jpg"));
            fout.write(data);
            fout.flush();
            fout.close();

            setResult(RESULT_OK);
            finish();
        }catch (Exception e){
            Log.v("brad", e.toString());
        }
    }

}
