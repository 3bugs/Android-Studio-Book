package com.example.simplenote2;

import android.app.ListActivity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;

import static com.example.simplenote2.Constants.CONTENT;
import static com.example.simplenote2.Constants.TABLE_NAME;
import static com.example.simplenote2.Constants.TIME;
import static com.example.simplenote2.Constants._ID;

public class MainActivity extends ListActivity {

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
        //Cursor cursor = db.query(TABLE_NAME, COLUMNS, null, null, null, null, ORDER_BY);
        String sql = "SELECT _id, datetime(time/1000, 'unixepoch', 'localtime') as time, content " +
                "FROM " + TABLE_NAME + " ORDER BY " + ORDER_BY;
        Cursor cursor = db.rawQuery(sql, null);
        //startManagingCursor(cursor);
        return cursor;
    }

    private static int[] VIEWS = {R.id.rowid, R.id.time, R.id.content};

    private void showNotes(Cursor cursor) {
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.item, cursor, COLUMNS, VIEWS, 0);
        setListAdapter(adapter);
    }
}
