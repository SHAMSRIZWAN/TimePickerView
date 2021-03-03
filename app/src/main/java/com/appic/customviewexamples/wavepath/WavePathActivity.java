package com.appic.customviewexamples.wavepath;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.appic.customviewexamples.R;

public class WavePathActivity extends AppCompatActivity {


//    WaveView waveView;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave_path);
        webView=findViewById(R.id.Wv);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        webView.loadUrl("file:///android_asset/index.html");

        webView.setWebViewClient(new WebViewClient());
//        waveView = findViewById(R.id.BezierView);
//        waveView.post(new Runnable() {
//            @Override
//            public void run() {
//                waveView.start();
//            }
//        });

    }


}