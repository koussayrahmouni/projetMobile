package tn.esprit.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView ivLogo = findViewById(R.id.iv_logo);

        // Use Glide to load the GIF into the ImageView
        Glide.with(this)
                .asGif()
                .load(R.drawable.anim1) // Your GIF resource
                .into(ivLogo);

        // Delay the transition to AddVehicleActivity for 3 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Navigate to the next screen (AddVehicleActivity)
                Intent intent = new Intent(SplashActivity.this, AddVehicleActivity.class);
                startActivity(intent);

                // Finish the SplashActivity so the user can't go back to it
                finish();
            }
        }, 3000); // 3000 milliseconds = 3 seconds
    }
}
