package com.example.animalsounds;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageView;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    // อาร์เรย์ที่เก็บ reference ของ ImageView (รูปภาพสัตว์) ทั้งเก้า
    private final ImageView imgAnimals[] = new ImageView[9];
    // ออบเจ็ค MediaPlayer ที่ใช้เล่นไฟล์เสียง
    private MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ค้นหา ImageView ต่างๆจาก Layout File แล้วเก็บการอ้างอิงลงในอาร์เรย์
        imgAnimals[0] = (ImageView) findViewById(R.id.pig);
        imgAnimals[1] = (ImageView) findViewById(R.id.dog);
        imgAnimals[2] = (ImageView) findViewById(R.id.cat);
        imgAnimals[3] = (ImageView) findViewById(R.id.elephant);
        imgAnimals[4] = (ImageView) findViewById(R.id.horse);
        imgAnimals[5] = (ImageView) findViewById(R.id.cow);
        imgAnimals[6] = (ImageView) findViewById(R.id.bird);
        imgAnimals[7] = (ImageView) findViewById(R.id.duck);
        imgAnimals[8] = (ImageView) findViewById(R.id.rooster);

        // กำหนดให้การคลิก ImageView ทุกอันเรียกมาที่ onClick ในออบเจ็คปัจจุบัน
        for (int i = 0; i < imgAnimals.length; i++) {
            imgAnimals[i].setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        int resId = 0;

        switch (v.getId()) {
            case R.id.pig:
                resId = R.raw.pig;
                break;
            case R.id.dog:
                resId = R.raw.dog;
                break;
            case R.id.cat:
                resId = R.raw.cat;
                break;
            case R.id.elephant:
                resId = R.raw.elephant;
                break;
            case R.id.horse:
                resId = R.raw.horse;
                break;
            case R.id.cow:
                resId = R.raw.cow;
                break;
            case R.id.bird:
                resId = R.raw.bird;
                break;
            case R.id.duck:
                resId = R.raw.duck;
                break;
            case R.id.rooster:
                resId = R.raw.rooster;
                break;
        }

        playSound(resId); // เล่นไฟล์เสียง
    }

    private void playSound(int id) {
        // ทำลายออบเจ็ค MediaPlayer ที่ใช้เล่นเสียงในครั้งก่อนหน้า
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }

        // สร้างออบเจ็ค MediaPlayer สำหรับเล่นไฟล์เสียงตาม Resource ID ที่ระบุ
        mPlayer = MediaPlayer.create(this, id);
        mPlayer.start();
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
