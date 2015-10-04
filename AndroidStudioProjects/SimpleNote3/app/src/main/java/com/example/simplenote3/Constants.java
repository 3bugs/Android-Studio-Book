package com.example.simplenote3;

import android.net.Uri;
import android.provider.BaseColumns;

public interface Constants extends BaseColumns {
    public static final String TABLE_NAME = "notes"; // ชื่อเทเบิล notes
    public static final String TIME = "time";        // ชื่อคอลัมน์ time
    public static final String CONTENT = "content";  // ชื่อคอลัมน์ content

    public static final String AUTHORITY = "com.example.simplenote3";
    public static final Uri URI_NOTES = Uri.parse("content://" +
            AUTHORITY + "/" + TABLE_NAME);
}
