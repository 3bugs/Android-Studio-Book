package com.example.simplenote3;

import android.app.ListActivity;
import android.content.ContentValues;
import android.database.Cursor;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;

import static com.example.simplenote3.Constants.CONTENT;
import static com.example.simplenote3.Constants.TABLE_NAME;
import static com.example.simplenote3.Constants.TIME;
import static com.example.simplenote3.Constants._ID;
import static com.example.simplenote3.Constants.URI_NOTES;

public class MainActivity extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Cursor cursor = getAllNotes();
        showNotes(cursor);

        final EditText txtNewText = (EditText) findViewById(R.id.new_text);
        Button btnSave = (Button) findViewById(R.id.save_button);

        // กำหนดการทำงานเมื่อปุ่มถูกคลิก
        btnSave.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                addNote(txtNewText.getText().toString());
                Cursor cursor = getAllNotes();
                showNotes(cursor);
                txtNewText.setText(null);
            }
        });
    }

    private void addNote(String str) {
        ContentValues values = new ContentValues();
        values.put(TIME, System.currentTimeMillis());
        values.put(CONTENT, str);
        getContentResolver().insert(URI_NOTES, values);
    }

    private static String[] COLUMNS = {_ID, TIME, CONTENT};
    private static String ORDER_BY = TIME + " DESC";

    private Cursor getAllNotes() {
        return managedQuery(URI_NOTES, COLUMNS, null, null, ORDER_BY);
    }

    private static int[] VIEWS = {R.id.rowid, R.id.time, R.id.content};

    private void showNotes(Cursor cursor) {
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.item, cursor, COLUMNS, VIEWS, 0);
        setListAdapter(adapter);
    }
}
