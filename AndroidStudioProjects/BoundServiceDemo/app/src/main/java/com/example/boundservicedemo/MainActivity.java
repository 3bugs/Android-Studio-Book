package com.example.boundservicedemo;

import java.util.ArrayList;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    private MyService service;
    private ArrayList<String> values;
    private ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        doBindService();  // ผูกเซอร์วิซ

        values = new ArrayList<String>();
        // สร้าง Adapter ที่ผูก ArrayList เข้ากับ ListView
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);
        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);

        // กำหนด Listener ให้กับการคลิกปุ่ม
        Button btnReload = (Button) findViewById(R.id.reload_button);
        btnReload.setOnClickListener(this);
    }

    private void doBindService() {
        ServiceConnection cn;

        // เตรียมออบเจ็ค ServiceConnection ที่จะส่งให้เมธอด bindService ในบรรทัดสุดท้าย
        cn = new ServiceConnection() {
            /* เมธอดที่จะทำงานเมื่อเชื่อมต่อกับเซอร์วิซแล้ว ในที่นี้เราใช้ตัวแปร service อ้างอิงไปยัง
               เซอร์วิซ เพื่อที่จะเรียกใช้เมธอดที่เป็นความสามารถของเซอร์วิซต่อไป */
            public void onServiceConnected(ComponentName className, IBinder binder) {
                service = ((MyService.MyBinder) binder).getService();
                Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
            }

            public void onServiceDisconnected(ComponentName className) {
                service = null;
            }
        };

        // ทำการผูกแอคทิวิตีเข้ากับเซอร์วิซ
        Intent i = new Intent(this, MyService.class);
        bindService(i, cn, Context.BIND_AUTO_CREATE);
    }

    public void onClick(View src) {
        switch (src.getId()) {
            case R.id.reload_button:
                if (service != null) {
                    ArrayList<String> wordList = service.getWordList();
                    values.clear();
                    values.addAll(wordList);
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }
}
