package ale.y.daniel.fotomulta;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class Velocimetro extends Activity implements SensorEventListener {

    TextView txtAngle, txtFeedback;
    private ImageView imgCompass;
    private RadioGroup rdGroup;
    private float currentDegree = 0f;
    private SensorManager mSensorManager;
    Sensor accelerometer;
    Sensor magnetometer;

    float degree;
    float azimut;
    float rb_degree;
    float[] mGravity;
    float[] mGeomagnetic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_velocimetro);

        imgCompass = (ImageView) findViewById(R.id.imgViewCompass);
        txtAngle = (TextView) findViewById(R.id.txtAngle);
        txtFeedback = (TextView) findViewById(R.id.txtFeedback);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        mGravity = null;
        mGeomagnetic = null;

        rdGroup = (RadioGroup) findViewById(R.id.rdGrp);
        rdGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if (checkedId == R.id.norte){
                    rb_degree = 0;
                    txtFeedback.setText("Norte");
                }else if (checkedId == R.id.sur){
                    rb_degree = -180;
                    txtFeedback.setText("Sur");
                }else if (checkedId == R.id.este){
                    rb_degree = 90;
                    txtFeedback.setText("Este");
                }else if (checkedId == R.id.oeste){
                    rb_degree = -90;
                    txtFeedback.setText("Oeste");
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                mGravity = event.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mGeomagnetic = event.values.clone();
                break;
        }

        if ((mGravity != null) && (mGeomagnetic != null)) {
            float RotationMatrix[] = new float[16];
            boolean success = SensorManager.getRotationMatrix(RotationMatrix, null, mGravity, mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(RotationMatrix, orientation);
                azimut = orientation[0] * (180 / (float) Math.PI);
            }
        }
        degree = azimut;

        double degree_aux = degree;
        if (degree_aux < 0)
            degree_aux = degree_aux * (-1);
        else if (degree_aux > 0)
            degree_aux = ((degree_aux - 180) * (-1)) + 180;

        txtAngle.setText("Ángulo: " + Double.toString(Math.round(degree_aux*100.0)/100.0) + "º");

        RotateAnimation ra = new RotateAnimation(
                currentDegree+rb_degree,
                degree+rb_degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        ra.setDuration(1000);
        ra.setFillAfter(true);
        imgCompass.startAnimation(ra);
        currentDegree = -degree;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_velocimetro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.inicio:
                Intent i = new Intent(this,MapsActivity.class);
                startActivity(i);
                break;
            case R.id.velocimetro:
                i = new Intent(this,MapsActivity.class);
                startActivity(i);
                break;
            case R.id.optionAyuda:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/idem.daniel")));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}