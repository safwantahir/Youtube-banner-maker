package com.example.backgroundremoverlibrary;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        //  int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        //decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            GuideFragment guideFragment = new GuideFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer,guideFragment)
                    .commit();
        }
    }

}