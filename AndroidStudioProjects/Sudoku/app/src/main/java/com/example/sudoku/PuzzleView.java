package com.example.sudoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.os.Bundle;
import android.os.Parcelable;


public class PuzzleView extends View {

    private static final String SELX = "selX";
    private static final String SELY = "selY";
    private static final String VIEW_STATE = "viewState";

    private final PuzzleActivity game;

    // คอนสตรัคเตอร์
    public PuzzleView(Context context) {
        super(context);
        // เก็บการอ้างอิงของ PuzzleActivity ไว้เพื่อเรียกใช้เมธอดใน PuzzleActivity
        this.game = (PuzzleActivity) context;
        setFocusable(true);
        setFocusableInTouchMode(true);
        setId(R.id.puzzle_view_id);
    }

    private float width;                     // ความกว้างของช่องตาราง
    private float height;                    // ความสูงของช่องตาราง
    private int selX;                        // ตำแหน่งในแนวนอนของเคอร์เซอร์
    private int selY;                        // ตำแหน่งในแนวตั้งของเคอร์เซอร์
    private final Rect selRect = new Rect(); // พื้นที่ของช่องตารางที่เป็นตำแหน่งเคอร์เซอร์

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w / 9f;
        height = h / 9f;
        getRect(selX, selY, selRect);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void getRect(int x, int y, Rect rect) {
        rect.set((int) (x * width), (int) (y * height),
                 (int) (x * width + width), (int) (y * height + height));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // +++ วาดพื้นหลัง +++
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.puzzle_background));
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);

        // +++ วาดเส้นตาราง +++
        // กำหนดสีของเส้นตาราง
        Paint dark = new Paint();
        dark.setColor(getResources().getColor(R.color.puzzle_dark));

        Paint highlight = new Paint();
        highlight.setColor(getResources().getColor(R.color.puzzle_highlight));

        Paint light = new Paint();
        light.setColor(getResources().getColor(R.color.puzzle_light));

        // วาดเส้นตารางย่อย
        for (int i = 0; i < 9; i++) {
            canvas.drawLine(0, i * height, getWidth(), i * height, light);
            canvas.drawLine(0, i * height + 1, getWidth(), i * height + 1,
                    highlight);
            canvas.drawLine(i * width, 0, i * width, getHeight(), light);
            canvas.drawLine(i * width + 1, 0, i * width + 1, getHeight(),
                    highlight);
        }

        // วาดเส้นตารางหลัก (เส้นแบ่งระหว่างตารางย่อย 3x3)
        for (int i = 0; i < 9; i++) {
            if (i % 3 != 0)
                continue;
            canvas.drawLine(0, i * height, getWidth(), i * height, dark);
            canvas.drawLine(0, i * height + 1, getWidth(), i * height + 1,
                    highlight);
            canvas.drawLine(i * width, 0, i * width, getHeight(), dark);
            canvas.drawLine(i * width + 1, 0, i * width + 1, getHeight(),
                    highlight);
        }

        // +++ วาดตัวเลข +++
        // กำหนดสีและรูปแบบของตัวเลข
        Paint fg = new Paint(Paint.ANTI_ALIAS_FLAG);
        fg.setColor(getResources().getColor(R.color.puzzle_foreground));
        fg.setStyle(Style.FILL);
        fg.setTextSize(height * 0.75f);
        fg.setTextScaleX(width / height);
        fg.setTextAlign(Paint.Align.CENTER);

        // วาดตัวเลขตรงกึ่งกลางช่องตาราง
        FontMetrics fm = fg.getFontMetrics();
        float x = width / 2;
        float y = height / 2 - (fm.ascent + fm.descent) / 2;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                canvas.drawText(this.game.getTileString(i, j), i * width + x,
                        j * height + y, fg);
            }
        }

        // +++ วาดตัวช่วย +++
        if (SettingsActivity.getOptionHints(getContext())) {
            // อ่านค่าสี 3 สีที่ใช้วาดตัวช่วยจากรีซอร์สมาเก็บในอาร์เรย์ c
            int c[] = { getResources().getColor(R.color.puzzle_hint_0),
                    getResources().getColor(R.color.puzzle_hint_1),
                    getResources().getColor(R.color.puzzle_hint_2) };

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    // หาว่าเหลือตัวเลขกี่ตัวที่สามารถเติมลงในแต่ละช่องได้
                    int numLeft = 9 - game.getUsedTiles(i, j).length;
                    // ถ้าเหลือน้อยกว่า 3 ตัวจะวาดสี่เหลี่ยมทับลงบนช่องนั้น โดยใช้สีที่เก็บไว้ในอาร์เรย์ c ข้างต้น
                    if (numLeft < c.length) {
                        Rect r = new Rect();
                        getRect(i, j, r);

                        Paint hint = new Paint();
                        hint.setColor(c[numLeft]);
                        canvas.drawRect(r, hint);
                    }
                }
            }
        }

        // +++ วาดเคอร์เซอร์ +++
        Paint selected = new Paint();
        selected.setColor(getResources().getColor(R.color.puzzle_selected));
        canvas.drawRect(selRect, selected);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                select(selX, selY - 1);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                select(selX, selY + 1);
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                select(selX - 1, selY);
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                select(selX + 1, selY);
                break;
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_DPAD_CENTER:
                game.showKeypadOrError(selX, selY);
                break;

            case KeyEvent.KEYCODE_0:
            case KeyEvent.KEYCODE_SPACE:
                setSelectedTile(0);
                break;
            case KeyEvent.KEYCODE_1:
                setSelectedTile(1);
                break;
            case KeyEvent.KEYCODE_2:
                setSelectedTile(2);
                break;
            case KeyEvent.KEYCODE_3:
                setSelectedTile(3);
                break;
            case KeyEvent.KEYCODE_4:
                setSelectedTile(4);
                break;
            case KeyEvent.KEYCODE_5:
                setSelectedTile(5);
                break;
            case KeyEvent.KEYCODE_6:
                setSelectedTile(6);
                break;
            case KeyEvent.KEYCODE_7:
                setSelectedTile(7);
                break;
            case KeyEvent.KEYCODE_8:
                setSelectedTile(8);
                break;
            case KeyEvent.KEYCODE_9:
                setSelectedTile(9);
                break;

            default:
                return super.onKeyDown(keyCode, event);
        }

        return true;
    }

    private void select(int x, int y) {
        invalidate(selRect);
        selX = Math.min(Math.max(x, 0), 8);
        selY = Math.min(Math.max(y, 0), 8);
        getRect(selX, selY, selRect);
        invalidate(selRect);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN)
            return super.onTouchEvent(event);

        // ย้ายเคอร์เซอร์ไปยังช่องที่ผู้ใช้แตะ แล้วแสดง Keypad ออกมา
        select((int) (event.getX() / width), (int) (event.getY() / height));
        game.showKeypadOrError(selX, selY);
        return true;
    }

    public void setSelectedTile(int num) {
        // ถ้าสามารถเติมตัวเลขในช่องเคอร์เซอร์ได้ถูกต้องตามกฎของซูโดกุ ให้วาดหน้าจอใหม่ทั้งหน้าจอ
        if (game.setTileIfValid(selX, selY, num)) {
            invalidate();
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable p = super.onSaveInstanceState();

        Bundle b = new Bundle();
        b.putInt(SELX, selX);
        b.putInt(SELY, selY);
        b.putParcelable(VIEW_STATE, p);

        return b;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle b = (Bundle) state;
        select(b.getInt(SELX), b.getInt(SELY));

        super.onRestoreInstanceState(b.getParcelable(VIEW_STATE));

        return;
    }
}
