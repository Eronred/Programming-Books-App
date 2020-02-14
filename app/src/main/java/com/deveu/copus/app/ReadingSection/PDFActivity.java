package com.deveu.copus.app.ReadingSection;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


import com.deveu.copus.app.R;

import java.io.UnsupportedEncodingException;

import java.net.URLEncoder;

public class PDFActivity extends AppCompatActivity {
        private WebView web_view;
        //private PDFView pdfView;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        web_view = findViewById(R.id.webUrl);
        //pdfView = findViewById(R.id.pdfView);
//        new RetrievePDFStream().execute(url);
        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.getSettings().setBuiltInZoomControls(true);
        web_view.setWebViewClient(new WebViewClient() {
        });
        Intent intent = getIntent();
        String url = intent.getStringExtra("link");
        try {
            url= URLEncoder.encode(url,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String finalURL = "http://drive.google.com/viewerng/viewer?embedded=true&url="+url;
        web_view.loadUrl(finalURL);

        Toast.makeText(PDFActivity.this, "The book is loading to preview...", Toast.LENGTH_SHORT).show();

    }
    }
