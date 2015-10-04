package com.example.sudoku;

import android.content.Context;
import android.media.MediaPlayer;

public class Music {
    private static MediaPlayer mPlayer = null;

    // หยุดเล่นเพลงที่เล่นมาก่อน แล้วเริ่มเล่นเพลงใหม่ตาม Resource ID ที่ระบุ
    public static void play(Context context, int resId) {
        stop();

        if (SettingsActivity.getOptionMusic(context)) {
            mPlayer = MediaPlayer.create(context, resId);
            mPlayer.setLooping(true); // เมื่อจบเพลงให้กลับมาเริ่มใหม่ วนซ้ำไปเรื่อยๆ
            mPlayer.start();
        }
    }

    // หยุดเล่นเพลง
    public static void stop() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }
}
