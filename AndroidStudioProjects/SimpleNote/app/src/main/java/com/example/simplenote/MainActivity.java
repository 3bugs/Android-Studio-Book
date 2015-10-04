package com.example.simplenote;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;

import static com.example.simplenote.Constants.CONTENT;
import static com.example.simplenote.Constants.TABLE_NAME;
import static com.example.simplenote.Constants.TIME;
import static com.example.simplenote.Constants._ID;

public class MainActivity extends AppCompatActivity {

    private NotesHelper helper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new NotesHelper(this);  // สร้าง Helper object

        // อ่านข้อมูลทั้งหมดจากฐานข้อมูลมาแสดงผล
        try {
            Cursor cursor = getAllNotes();
            showNotes(cursor);
        } finally {
            // ปิดการเชื่อมต่อกับฐานข้อมูล ไม่ว่าโค้ดในส่วนของ try จะเกิดข้อผิดพลาดหรือไม่ก็ตาม
            helper.close();
        }

        final EditText txtNewText = (EditText) findViewById(R.id.new_text);
        Button btnSave = (Button) findViewById(R.id.save_button);

        // กำหนดการทำงานเมื่อปุ่มถูกคลิก
        btnSave.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                // นำข้อความจาก EditText เพิ่มลงฐานข้อมูล แล้วอ่านข้อมูลทั้งหมดมาแสดง
                try {
                    addNote(txtNewText.getText().toString());
                    Cursor cursor = getAllNotes();
                    showNotes(cursor);
                    txtNewText.setText(null);
                } finally {
                    helper.close();
                }
            }
        });
    }

    private void addNote(String str) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TIME, System.currentTimeMillis());
        values.put(CONTENT, str);
        db.insertOrThrow(TABLE_NAME, null, values);
    }

    private static String[] COLUMNS = {_ID, TIME, CONTENT};
    private static String ORDER_BY = TIME + " DESC";

    private Cursor getAllNotes() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, COLUMNS, null, null, null, null, ORDER_BY);
        //startManagingCursor(cursor);
        return cursor;
    }

    private void showNotes(Cursor cursor) {
        StringBuilder builder = new StringBuilder("ข้อความที่บันทึกไว้:\n\n");

        while (cursor.moveToNext()) {
            long id = cursor.getLong(0);          // อ่านข้อมูลจากคอลัมน์ _id
            long time = cursor.getLong(1);        // อ่านข้อมูลจากคอลัมน์ time
            String content = cursor.getString(2); // อ่านข้อมูลจากคอลัมน์ content

            builder.append("ลำดับ ").append(id).append(": ");
            // แปลงค่า Timestamp เป็นออบเจ็ค Date แล้วจัดรูปแบบวันเวลาให้ดูง่าย
            String strDate = (String) DateFormat.format("yyyy-MM-dd hh:mm:ss", new Date(time));
            builder.append(strDate).append("\n");
            builder.append("\t").append(content).append("\n");
            builder.append("----------").append("\n");
        }

        TextView tv = (TextView) findViewById(R.id.all_text);
        tv.setText(builder);
    }
}
