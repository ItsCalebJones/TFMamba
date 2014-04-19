package com.cjones.taskforcemamba.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cjones.taskforcemamba.About;
import com.cjones.taskforcemamba.R;

/**
 * Created by Caleb on 4/14/14.
 */
public class TutViewerActivity extends Activity {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.web_view);
            //Receive intent from RSSFeedReaderActivity convert to string and pass to webview for rendering.
            Intent launchingIntent = getIntent();
            String content = launchingIntent.getData().toString();

            WebView mWebView = (WebView) findViewById(R.id.webview2);
            mWebView.loadUrl(content);
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            mWebView.setWebViewClient(new WebViewClient());
            mWebView.getSettings().setBuiltInZoomControls(true);
            mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

            final ProgressBar Pbar;
            Pbar = (ProgressBar) findViewById(R.id.pB2);

            mWebView.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress)
                {
                    if(progress < 100 && Pbar.getVisibility() == ProgressBar.GONE){
                        Pbar.setVisibility(ProgressBar.VISIBLE);
                    }
                    Pbar.setProgress(progress);
                    if(progress == 100) {
                        Pbar.setVisibility(ProgressBar.GONE);
                        Toast.makeText(getApplicationContext(), "Finished loading...", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    public static final String TAG = "Task Force Mamba";
    }

