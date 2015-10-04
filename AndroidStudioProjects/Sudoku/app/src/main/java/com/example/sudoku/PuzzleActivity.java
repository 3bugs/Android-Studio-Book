package com.example.sudoku;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

public class PuzzleActivity extends Activity {
    protected static final String KEY_DIFFICULTY = "difficulty";
    protected static final int DIFFICULTY_EASY = 0;
    protected static final int DIFFICULTY_MEDIUM = 1;
    protected static final int DIFFICULTY_HARD = 2;
    // ค่าที่บอกเราว่าให้โหลดสถานะเดิมของเกมกลับมา แทนที่จะเริ่มเกมใหม่
    protected static final int DIFFICULTY_CONTINUE = -1;

    // คีย์สำหรับระบุถึงข้อมูลสถานะของเกม
    private static final String PREF_PUZZLE = "puzzle";

    private final String easyPuzzle =
            "000070000080000273009036800" +
            "001247000807050309000389400" +
            "003510700124000090000020000";

    private final String mediumPuzzle =
            "300000056400000000000897000" +
            "002080400006103800005070300" +
            "000462000000000008510000007";

    private final String hardPuzzle =
            "462900000900100000705800000" +
            "631000000000000000000000259" +
            "000005901000008003000001542";


    private int puzzle[] = new int [9 * 9];
    private PuzzleView puzzleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int diff = getIntent().getIntExtra(KEY_DIFFICULTY,
                DIFFICULTY_EASY);
        puzzle = getPuzzle(diff);
        calculateUsedTiles();

        puzzleView = new PuzzleView(this);
        setContentView(puzzleView);
        puzzleView.requestFocus();

        getIntent().putExtra(KEY_DIFFICULTY, DIFFICULTY_CONTINUE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Music.play(this, R.raw.game);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Music.stop();

        // เก็บสถานะปัจจุบันของเกมลงใน preference
        getPreferences(MODE_PRIVATE).edit().putString(PREF_PUZZLE,
                toPuzzleString(puzzle)).commit();
    }

    protected void showKeypadOrError(int x, int y) {
        // หาตัวเลขที่ถูกใช้ไปแล้วสำหรับช่องที่ตำแหน่ง x, y
        int tiles[] = getUsedTiles(x, y);

    /* ถ้าตัวเลข 1-9 ถูกใช้หมดแล้วจะแสดง Toast บอกผู้ใช้ว่าไม่สามารถเติมตัวเลขใดๆที่ช่องนี้ได้
       แต่ถ้ายังเหลือตัวเลขที่เติมได้ก็จะแสดงหน้าจอ Keypad ขึ้นมา */
        if (tiles.length == 9) {
            Toast toast = Toast.makeText(this, R.string.no_moves_label,
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        else {
            Dialog v = new Keypad(this, tiles, puzzleView);
            v.show();
        }
    }

    protected boolean setTileIfValid(int x, int y, int value) {
        // หาตัวเลขที่ถูกใช้ไปแล้วสำหรับช่องที่ตำแหน่ง x, y
        int tiles[] = getUsedTiles(x, y);

        if (value != 0) {
            for (int tile : tiles) {
                // ถ้าตัวเลขที่ต้องการเติมได้ถูกใช้ไปแล้ว ให้ออกจากเมธอดทันที
                if (tile == value)
                    return false;
            }
        }

        setTile(x, y, value);  // เติมตัวเลข value ลงในช่องที่ตำแหน่ง x, y
        calculateUsedTiles();  // หาตัวเลขที่ถูกใช้ไปแล้วสำหรับทุกช่องใหม่
        return true;
    }

    private final int used[][][] = new int[9][9][];

    protected int[] getUsedTiles(int x, int y) {
        return used[x][y];
    }

    private void calculateUsedTiles() {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                used[x][y] = calculateUsedTiles(x, y);
            }
        }
    }

    private int[] calculateUsedTiles(int x, int y) {
        int c[] = new int[9];  // อาร์เรย์ขนาด 9 สมาชิก สำหรับเก็บตัวเลข 1 ถึง 9

        // รวบรวมตัวเลขที่ถูกใช้ไปแล้วในแถวเดียวกัน (แนวนอน)
        for (int i = 0; i < 9; i++) {
            if (i == y)  // ข้ามตัวเลขในช่องของตัวเองไป
                continue;
            int t = getTile(x, i);
            if (t != 0)  // ไม่เอาเลข 0 ซึ่งหมายถึงช่องว่าง
                c[t - 1] = t;
        }

        // รวบรวมตัวเลขที่ถูกใช้ไปแล้วในคอลัมน์เดียวกัน (แนวตั้ง)
        for (int i = 0; i < 9; i++) {
            if (i == x)  // ข้ามตัวเลขในช่องของตัวเองไป
                continue;
            int t = getTile(i, y);
            if (t != 0)  // ไม่เอาเลข 0 ซึ่งหมายถึงช่องว่าง
                c[t - 1] = t;
        }

        // รวบรวมตัวเลขในตารางย่อย 3x3 เดียวกัน
        int startx = (x / 3) * 3;  // จุดเริ่มต้นในแนวนอนของตารางย่อย 3x3
        int starty = (y / 3) * 3;  // จุดเริ่มต้นในแนวตั้งของตารางย่อย 3x3
        for (int i = startx; i < startx + 3; i++) {
            for (int j = starty; j < starty + 3; j++) {
                if (i == x && j == y)  // ข้ามตัวเลขในช่องของตัวเองไป
                    continue;
                int t = getTile(i, j);
                if (t != 0)  // ไม่เอาเลข 0 ซึ่งหมายถึงช่องว่าง
                    c[t - 1] = t;
            }
        }

        // แปลงอาร์เรย์ c เป็นอาร์เรย์ c1 ที่มีขนาดเท่ากับจำนวนตัวเลขที่ถูกใช้ไปจริงๆ
        int nused = 0;
        for (int t : c) {  // หาว่าอาร์เรย์ c1 ควรมีขนาดเท่าใด
            if (t != 0)
                nused++;
        }
        int c1[] = new int[nused];  // สร้างอาร์เรย์ c1 ตามขนาดที่หาได้
        nused = 0;
        for (int t : c) {  // เอาตัวเลขที่ไม่ใช่ 0 จากอาร์เรย์ c มาเก็บลงในอาร์เรย์ c1
            if (t != 0)
                c1[nused++] = t;
        }

        return c1;  // ส่งคืนอาร์เรย์ c1 กลับออกไปจากเมธอด
    }


    private void setTile(int x, int y, int value) {
        puzzle[y * 9 + x] = value;
    }

    private int getTile(int x, int y) {
        return puzzle[y * 9 + x];
    }

    protected String getTileString(int x, int y) {
        int v = getTile(x, y);

        // ถ้าเป็นเลข 0 จะคืนสตริงว่างออกไป แต่ถ้าไม่ใช่ก็จะคืนค่าสตริงของเลขนั้นออกไป
        if (v == 0)
            return "";
        else
            return String.valueOf(v);
    }

    private int[] getPuzzle(int diff) {
        String puz;

        switch (diff) {
            case DIFFICULTY_CONTINUE:
                puz = getPreferences(MODE_PRIVATE).getString(PREF_PUZZLE,
                        easyPuzzle);
                break;
            case DIFFICULTY_HARD:
                puz = hardPuzzle;
                break;
            case DIFFICULTY_MEDIUM:
                puz = mediumPuzzle;
                break;
            case DIFFICULTY_EASY:
            default:
                puz = easyPuzzle;
                break;
        }

        return fromPuzzleString(puz);
    }

    static protected int[] fromPuzzleString(String str) {
        int[] puz = new int[str.length()];
        for (int i = 0; i < puz.length; i++) {
            puz[i] = str.charAt(i) - '0';
        }
        return puz;
    }

    static private String toPuzzleString(int[] puz) {
        StringBuilder buf = new StringBuilder();
        for (int element : puz) {
            buf.append(element);
        }
        return buf.toString();
    }

}
