package com.codeyard.aakraman3;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class SelfTestActivity extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_test);
        final String TAG = SelfTestActivity.class.getName();
        WebView webView = findViewById(R.id.self_test_web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        Log.e(TAG, "onCreate: " + getIntent().getStringExtra("WHERE"));
        webView.loadUrl("http://" + getIntent().getStringExtra("WHERE"));
    }
}
