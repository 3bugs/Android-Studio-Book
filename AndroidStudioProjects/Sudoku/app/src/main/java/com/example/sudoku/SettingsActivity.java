package com.example.sudoku;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.content.Context;


public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }

    // อ่านค่าปัจจุบันของตัวเลือก “ดนตรี (Music)”
    public static boolean getOptionMusic(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean("music", true);
    }

    // อ่านค่าปัจจุบันของตัวเลือก “ตัวช่วย (Hints)”
    public static boolean getOptionHints(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean("hints", true);
    }

}
