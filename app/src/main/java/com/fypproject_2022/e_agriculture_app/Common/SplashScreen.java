package com.fypproject_2022.e_agriculture_app.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.widget.TextView;

import com.fypproject_2022.e_agriculture_app.R;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1000;
    Animation botton_animation;
    TextView s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }
}