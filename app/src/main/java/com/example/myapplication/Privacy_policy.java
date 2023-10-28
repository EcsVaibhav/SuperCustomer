package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ZoomControls;

public class Privacy_policy extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        WebView PPcontent = findViewById(R.id.privacyWebview);
        WebSettings webSettings = PPcontent.getSettings();
        webSettings.setJavaScriptEnabled(true);

        PPcontent.loadUrl("file:///android_asset/ppmaincontent.html");

        PPcontent.getSettings().setBuiltInZoomControls(true);
        PPcontent.getSettings().setDisplayZoomControls(true);


        PPcontent.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });


    }


}