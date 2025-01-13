package com.example.projetgyroscope;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;
    private Vibrator vibrator;
    private TextView directionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        directionTextView = findViewById(R.id.directionTextView);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        // Initialiser le gestionnaire de capteurs
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        // Vérifier si le gyroscope est disponible
        if (gyroscopeSensor == null) {
            directionTextView.setText("Gyroscope non disponible !");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gyroscopeSensor != null) {
            sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (gyroscopeSensor != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            float x = event.values[0];

            // Détection de la direction à l'aide du gyroscope
            if (x > 1.0) {
                directionTextView.setText("Tournez à gauche");
                vibrator.vibrate(200);
            } else if (x < -1.0) {
                directionTextView.setText("Tournez à droite");
                vibrator.vibrate(200);
            } else {
                directionTextView.setText("Continuez tout droit");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Non utilisé ici
    }
}
