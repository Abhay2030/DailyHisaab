package com.example.dailyhisaab;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView ivLogo = findViewById(R.id.ivLogo);
        TextView tvAppName = findViewById(R.id.tvAppName);

        // Subtle entrance animation
        ivLogo.animate()
                .alpha(1f)
                .translationYBy(-30f)
                .setDuration(800)
                .setInterpolator(new DecelerateInterpolator())
                .start();

        tvAppName.animate()
                .alpha(1f)
                .translationYBy(-30f)
                .setDuration(800)
                .setStartDelay(200)
                .setInterpolator(new DecelerateInterpolator())
                .start();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, 1500);
    }
}