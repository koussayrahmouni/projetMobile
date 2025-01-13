package tn.esprit.myapplication;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
     @Override
        public final void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
    getSupportActionBar().setTitle("parkini");
     Button buttonlogin = findViewById(R.id.button_login);
     buttonlogin.setOnClickListener(new View.OnClickListener()
  {
   @Override
  public void onClick(View v)
  {
   Intent intent = new Intent(MainActivity.this, tn.esprit.myapplication.LoginActivity.class);
    startActivity(intent);
     }
      }
     );
     Button register = findViewById(R.id.button_register);
     register.setOnClickListener(new View.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(View v)
                                        {
                                            Intent intent = new Intent(MainActivity.this, tn.esprit.myapplication.RegisterActivity.class);
                                            startActivity(intent);
                                        }
                                    }
     );





 }


}