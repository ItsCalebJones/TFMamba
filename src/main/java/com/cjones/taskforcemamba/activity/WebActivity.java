/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cjones.taskforcemamba.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;
import com.apigee.sdk.ApigeeClient;

import com.cjones.taskforcemamba.About;
import com.cjones.taskforcemamba.R;
import com.cjones.taskforcemamba.activity.RssFeedReaderActivity;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.concurrent.atomic.AtomicInteger;

public class WebActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    public WebView myWebView;
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String ANY = "Any";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();

        //WebView
        Intent launchingIntent = getIntent();
//        String content = launchingIntent.getData().toString();
        String home = "http://www.tfmamba.com";
//        Log.i(TAG, "Content = " + content + " . Home = " + home);
//        if(content == home){
//            content = content + "&styleid=2";
//            Log.i(TAG, "IF Statement - Content = " + content + " . Home = " + home);
//        } else {
//            Log.i(TAG, "Else Statement - Content = " + content + " . Home = " + home);
//        }
//        Log.i(TAG, "conte = " + content);
        myWebView = (WebView) findViewById(R.id.webview);
//            if(content == null){
                myWebView.loadUrl(home);
//        }else {
//                myWebView.loadUrl(content);
//            }
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.getSettings().setBuiltInZoomControls(true);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        final ProgressBar Pbar;
        Pbar = (ProgressBar) findViewById(R.id.pB1);

        myWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {
                if(progress < 100 && Pbar.getVisibility() == ProgressBar.GONE){
                    Pbar.setVisibility(ProgressBar.VISIBLE);
                }
                Pbar.setProgress(progress);
                if(progress == 100) {
                    Pbar.setVisibility(ProgressBar.GONE);
                }
            }
        });
        if (savedInstanceState != null) {
            myWebView.restoreState(savedInstanceState);
        } else {
//            if(content == null && !content.isEmpty()){
//            myWebView.loadUrl("http://www.tfmamba.com");
            }
        }
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(
                menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.apply:
                Log.i(TAG, "Login trigger");
                myWebView.loadUrl("http://www.tfmamba.com/register.php");
                return true;
            case R.id.refresh:
                Log.i(TAG, "Refresh trigger");
                if(myWebView == null){
                    finish();
                    startActivity(getIntent());
                } else {
                myWebView.reload();
                return true;
                }
            case R.id.home:
                Log.i(TAG, "Home trigger");
                Intent intent = new Intent(this, NavDrawer.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
    @Override
    public void onResume() {
        super.onResume();
//        checkPlayServices();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.myWebView.canGoBack()) {
            this.myWebView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }
    public static final String TAG = "Task Force Mamba";

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }
}


