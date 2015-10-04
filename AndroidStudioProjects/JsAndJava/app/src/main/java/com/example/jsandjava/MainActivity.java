package com.example.jsandjava;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    private final Handler handler = new Handler();
    private Button button;
    private TextView textView;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.web_view);
        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.text_view);

        // ระบุการทำงานเมื่อปุ่มถูกคลิก
        button.setOnClickListener(new OnClickListener () {
            public void onClick(View view) {
                webView.loadUrl("javascript:callJS('Hello from Android')");
            }
        });

        // เปิดการใช้งานจาวาสคริปต์ใน WebView
        webView.getSettings().setJavaScriptEnabled(true);
        // เปิดเผยออบเจ็ค AndroidBridge ออกไปให้จาวาสคริปต์เห็นในชื่อ android
        webView.addJavascriptInterface(new AndroidBridge(), "android");
        // โหลดเว็บเพจ
        webView.loadUrl("file:///android_asset/webpage.html");
    }

    private class AndroidBridge {
        @JavascriptInterface
        public void callJava(final String arg) {
            Runnable runnable = new Runnable() {
                public void run() {
                    textView.setText(arg);
                }
            };
            handler.post(runnable);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
