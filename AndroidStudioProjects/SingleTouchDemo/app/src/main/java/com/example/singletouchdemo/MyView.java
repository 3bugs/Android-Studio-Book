package com.example.singletouchdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

public class MyView extends View {

    private Paint paint = new Paint();  // พู่กันที่ใช้วาดเส้น
    private Path path = new Path();     // พาธของเส้นที่จะวาด

    // คอนสตรัคเตอร์
    public MyView(Context context) {
        super(context);

        // กำหนดลักษณะต่างๆของพู่กัน ซึ่งจะมีผลต่อลักษณะของเส้นที่วาดออกมา
        paint.setAntiAlias(true);
        paint.setStrokeWidth(6f);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // เก็บตำแหน่งที่เกิดอีเวนต์ลงในตัวแปร x, y
        float x = event.getX();
        float y = event.getY();

        // ตรวจสอบ action
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                // ไม่ต้องทำอะไรเมื่อยกนิ้วขึ้น (ปล่อยนิ้วจากการทัช)
                break;
            default:
                return false;
        }

        invalidate(); // ให้วาดหน้าจอใหม่
        return true;  // ส่งคืนค่า true เพื่อบอกแอนดรอยด์ว่าเราได้จัดการอีเวนต์แล้ว
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, paint);
    }
}
