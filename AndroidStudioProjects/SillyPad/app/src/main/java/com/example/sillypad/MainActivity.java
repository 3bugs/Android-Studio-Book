package com.example.sillypad;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private TextView tvFilename; // อ้างอิง TextView ที่แสดงชื่อไฟล์
    private EditText etContent;  // อ้างอิง EditText ที่แสดงเนื้อหาไฟล์
    private String strCurrentFilename; // ชื่อไฟล์ที่ถูกเปิดอยู่

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvFilename = (TextView) findViewById(R.id.filename);
        etContent = (EditText) findViewById(R.id.content_area);

        newFile();
    }

    private void newFile() {
        tvFilename.setText("[Untitled]");
        etContent.setText("");
        strCurrentFilename = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_new) {
            newFile();
            return true;
        } else if (id == R.id.menu_open) {
            showOpenFileDialog();
            return true;
        } else if (id == R.id.menu_save) {
            if (strCurrentFilename == null) {
                showSaveFileDialog();
            } else {
                saveFile();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showOpenFileDialog() {
        // สร้างไดอะล็อก (Alert Dialog)
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        // กำหนด Title และไอคอนให้กับไดอะล็อก
        dialog.setTitle("เลือกไฟล์ที่จะเปิด");
        dialog.setIcon(R.drawable.file_open);

        final String allFiles[] = fileList();
        dialog.setItems(allFiles, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                strCurrentFilename = allFiles[i];
                openFile(strCurrentFilename);
            }
        });

        dialog.show();
    }

    private void openFile(String filename) {
        try {
            FileInputStream fis = openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader br = new BufferedReader(isr);

            StringBuilder sBuilder = new StringBuilder();
            String strLine = null;
            while ((strLine = br.readLine()) != null) {
                sBuilder.append(strLine + "\n");
            }

            etContent.setText(sBuilder);
            strCurrentFilename = filename;
            tvFilename.setText(strCurrentFilename);

            br.close();
            isr.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSaveFileDialog() {
        // สร้างไดอะล็อก (Alert Dialog)
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        // กำหนด Title และไอคอนให้กับไดอะล็อก
        dialog.setTitle("ชื่อไฟล์ที่จะบันทึก");
        dialog.setIcon(R.drawable.file_save);

        // สร้าง EditText สำหรับกรอกชื่อไฟล์ แล้วกำหนดเป็นวิวของไดอะล็อก
        final EditText input = new EditText(this);
        dialog.setView(input);

        // กำหนดปุ่ม “บันทึก” ในไดอะล็อก
        dialog.setPositiveButton("บันทึก",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        strCurrentFilename = input.getText().toString();
                        saveFile();
                    }
                });

        // กำหนดปุ่ม “ยกเลิก” ในไดอะล็อก
        dialog.setNegativeButton("ยกเลิก",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String msg = "คุณยกเลิกการบันทึกไฟล์";
                        showToast(msg);
                    }
                });

        dialog.show();
    }

    private void saveFile() {
        FileOutputStream fos;
        String strContent = etContent.getText().toString();

        try {
            fos = openFileOutput(strCurrentFilename, MODE_PRIVATE);
            fos.write(strContent.getBytes());
            fos.close();

            tvFilename.setText(strCurrentFilename);

            String msg = "บันทึกไฟล์ " + strCurrentFilename + " แล้ว";
            showToast(msg);
        } catch (Exception e) {
            e.printStackTrace();

            String msg = "เกิดข้อผิดพลาด ไม่สามารถบันทึกไฟล์ได้";
            showToast(msg);
        }
    }

    private void showToast(String msg) {
        Toast t = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        t.show();
    }
}
