package com.example.sudoku;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;


public class MainActivity extends ActionBarActivity
        implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // กำหนด Listener ให้กับปุ่ม “เกี่ยวกับซูโดกุ”
        Button btnAbout = (Button) findViewById(R.id.about_button);
        btnAbout.setOnClickListener(this);
        // กำหนด Listener ให้กับปุ่ม “เริ่มเกมใหม่”
        View btnNewGame = findViewById(R.id.new_game_button);
        btnNewGame.setOnClickListener(this);
        // กำหนด Listener ให้กับปุ่ม “เล่นเกมต่อจากเดิม”
        View btnContinue = findViewById(R.id.continue_button);
        btnContinue.setOnClickListener(this);
        // กำหนด Listener ให้กับปุ่ม “ออกจากเกม”
        View btnExit = findViewById(R.id.exit_button);
        btnExit.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Music.play(this, R.raw.main);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Music.stop();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.continue_button:
                startGame(PuzzleActivity.DIFFICULTY_CONTINUE);
                break;
            case R.id.about_button:
                Intent i = new Intent(this, AboutActivity.class);
                startActivity(i);
                break;
            case R.id.new_game_button:
                openNewGameDialog();
                break;
            case R.id.exit_button:
                finish();
                break;
        }
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
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static final String TAG = "Sudoku";

    private void openNewGameDialog() {
        // สร้าง Alert Dialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        // กำหนด Title ของ Alert Dialog
        dialog.setTitle(R.string.new_game_title);

        /* กำหนดรายการตัวเลือกใน Alert Dialog และ Listener
           ที่ระบุการทำงานเมื่อตัวเลือกใน Alert Dialog ถูกเลือก */
        dialog.setItems(R.array.difficulty,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        startGame(i);  // เริ่มเกมใหม่
                    }
                });

        // แสดง Alert Dialog ออกมา
        dialog.show();
    }

    private void startGame(int i) {
        Log.d(TAG, "คุณเลือก " + i);

        Intent intent = new Intent(this, PuzzleActivity.class);
        intent.putExtra(PuzzleActivity.KEY_DIFFICULTY, i);
        startActivity(intent);
    }
}
