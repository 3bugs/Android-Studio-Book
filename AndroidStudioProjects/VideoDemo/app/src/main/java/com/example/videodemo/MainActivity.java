package com.example.videodemo;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Environment;
import android.widget.VideoView;
import android.widget.MediaController;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VideoView video = (VideoView) findViewById(R.id.video_view);

        // สร้าง MediaController แล้วกำหนดให้เป็นตัวควบคุมวิดีโอของเรา
        MediaController mc = new MediaController(this);
        video.setMediaController(mc);

        // โหลดไฟล์วิดีโอ Wildlife.mp4 จาก SD Card
        video.setVideoPath(Environment.getExternalStorageDirectory()
                + "/Wildlife.mp4");
        video.start(); // เริ่มเล่นวิดีโอ
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
