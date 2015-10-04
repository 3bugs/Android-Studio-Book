package com.example.testcontentprovider;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
    private static final Uri URI =
            Uri.parse("content://com.example.simplenote3/notes");
    private static final String[] COLUMNS = { "_id", "time", "content" };
    private static final String ORDER_BY = "time DESC";

    private TextView tv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv = new TextView(this);
        tv.setTextSize(16);
        tv.setPadding(30, 30, 30, 30);
        setContentView(tv); // กำหนด TextView เป็น UI ของแอพ (ไม่ใช้ Layout File)

        // คิวรีข้อมูลจาก URI ที่ระบุ
        Cursor cursor = managedQuery(URI, COLUMNS, null, null, ORDER_BY);
        showNotes(cursor);
    }

    private void showNotes(Cursor cursor) {
        StringBuilder builder = new StringBuilder("ข้อความที่บันทึกไว้:\n\n");

        while (cursor.moveToNext()) {
            long id = cursor.getLong(0);          // อ่านข้อมูลจากคอลัมน์ _id
            String time = cursor.getString(1);    // อ่านข้อมูลจากคอลัมน์ time
            String content = cursor.getString(2); // อ่านข้อมูลจากคอลัมน์ content

            builder.append("ลำดับ ").append(id).append(": ");
            builder.append(time).append("\n");
            builder.append("\t").append(content).append("\n\n");
        }

        tv.setText(builder);
    }
}
