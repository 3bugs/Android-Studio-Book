package com.example.browserintent;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.content.Intent;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity {

    private EditText txtURL;
    private Button btnGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtURL = (EditText) findViewById(R.id.url_field);
        btnGo = (Button) findViewById(R.id.go_button);

        // ระบุการทำงานเมื่อปุ่มถูกคลิก
        btnGo.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                openBrowser();
            }
        });

        // ระบุการทำงานเมื่อมีการกดคีย์ต่างๆใน EditText
        txtURL.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                // ถ้ากด Enter ให้เปิดบราวเซอร์ขึ้นมา ส่วนคีย์อื่นๆไม่ต้องทำอะไร
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    openBrowser();
                    return true;
                }
                return false;
            }
        });
    }

    private void openBrowser() {
        // อ่านค่าสตริงจาก EditText แล้วแปลงเป็นออบเจ็คชนิด Uri
        Uri uri = Uri.parse(txtURL.getText().toString());
        // สร้างอินเทนต์สำหรับเรียกดูออบเจ็ค Uri นั้น
        Intent i = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(i);
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
