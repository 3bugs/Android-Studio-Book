package com.example.sudoku;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

public class Keypad extends Dialog {
    protected static final String TAG = "Sudoku";

    // อาร์เรย์สำหรับอ้างอิงไปยังปุ่มต่างๆในหน้าจอ Keypad 
    private final View keys[] = new View[10];
    // อาร์เรย์สำหรับเก็บตัวเลขที่ไม่สามารถเติมลงในช่องเคอร์เซอร์ได้
    private final int useds[];
    // ตัวแปรสำหรับอ้างอิงไปยังออบเจ็ค PuzzleView
    private final PuzzleView puzzleView;

    // คอนสตรัคเตอร์
    public Keypad(Context context, int useds[], PuzzleView puzzleView) {
        super(context);
        this.useds = useds;
        this.puzzleView = puzzleView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.keypad_title);  // กำหนดข้อความ Title ของ Keypad
        setContentView(R.layout.keypad);  // กำหนด UI ของ Keypad
        findViews();  // ค้นหาวิวต่างๆในเลย์เอาต์ เพื่ออ้างอิงด้วยตัวแปรในโค้ดจาวา
        // ซ่อนปุ่มของตัวเลขที่ไม่สามารถเติมลงในช่องเคอร์เซอร์ได้ เพื่อไม่ให้ผู้ใช้เลือก
        for (int element : useds) {
            keys[element].setVisibility(View.INVISIBLE);
        }
        setListeners();  // กำหนด Listener ให้กับปุ่มต่างๆใน Keypad
    }

    private void findViews() {
        keys[0] = findViewById(R.id.keypad_clear);
        keys[1] = findViewById(R.id.keypad_1);
        keys[2] = findViewById(R.id.keypad_2);
        keys[3] = findViewById(R.id.keypad_3);
        keys[4] = findViewById(R.id.keypad_4);
        keys[5] = findViewById(R.id.keypad_5);
        keys[6] = findViewById(R.id.keypad_6);
        keys[7] = findViewById(R.id.keypad_7);
        keys[8] = findViewById(R.id.keypad_8);
        keys[9] = findViewById(R.id.keypad_9);
    }

    private void setListeners() {
        // กำหนด Listener ให้กับแต่ละปุ่มใน Keypad
        for (int i = 0; i < keys.length; i++) {
            final int t = i;
            keys[i].setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        returnResult(t);
                    }
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int tile = 0;

        switch (keyCode) {
            case KeyEvent.KEYCODE_0:
            case KeyEvent.KEYCODE_SPACE:
                tile = 0; break;
            case KeyEvent.KEYCODE_1:
                tile = 1; break;
            case KeyEvent.KEYCODE_2:
                tile = 2; break;
            case KeyEvent.KEYCODE_3:
                tile = 3; break;
            case KeyEvent.KEYCODE_4:
                tile = 4; break;
            case KeyEvent.KEYCODE_5:
                tile = 5; break;
            case KeyEvent.KEYCODE_6:
                tile = 6; break;
            case KeyEvent.KEYCODE_7:
                tile = 7; break;
            case KeyEvent.KEYCODE_8:
                tile = 8; break;
            case KeyEvent.KEYCODE_9:
                tile = 9; break;
            default:
                return super.onKeyDown(keyCode, event);
        }

        /* ตรวจสอบว่าตัวเลขที่ผู้ใช้กดเลือกจากคีย์บอร์ดนั้นใช้ได้หรือไม่ (เติมลงในช่องเคอร์เซอร์ได้หรือไม่)
           ถ้าใช้ได้จะเรียกเมธอด returnResult และส่งตัวเลขนั้นไปให้ แต่ถ้าใช้ไม่ได้ก็จะไม่ทำอะไร */
        if (isValid(tile)) {
            returnResult(tile);
        }
        return true;
    }

    private boolean isValid(int tile) {
        for (int t : useds) {
            if (tile == t)
                return false;
        }
        return true;
    }

    private void returnResult(int tile) {
        puzzleView.setSelectedTile(tile);
        dismiss();
    }
}
