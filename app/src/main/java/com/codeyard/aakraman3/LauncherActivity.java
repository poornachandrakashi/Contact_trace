package com.codeyard.aakraman3;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.codeyard.aakraman3.constants.Constants;

import androidx.appcompat.app.AppCompatActivity;

public class LauncherActivity extends AppCompatActivity {
    Animation topAnim, bottomAnim;
    TextView top, line1, line2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        top = findViewById(R.id.appName);
        line1 = findViewById(R.id.line1);
        line2 = findViewById(R.id.line2);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom);

        top.setAnimation(topAnim);
        line1.setAnimation(bottomAnim);
        line2.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                UserIDModel userIDModel = new UserIDModel(LauncherActivity.this);
//
//                String userID = userIDModel.getUserId();
//                if (userID.equals("")) {
//
//                    startActivity(new Intent(
//                            LauncherActivity.this,
//                            SignUpActivity.class));
//                    finish();
//                } else {
//                    startActivity(
//                            new Intent(
//                                    getApplicationContext(),
//                                    MainActivity.class)
//                    );
//                    finish();
//                }
                startActivity(new Intent(LauncherActivity.this, SignUpActivity.class));
            }
        }, Constants.SPLASH_SCREEN_TIMEOUT);


    }
}
