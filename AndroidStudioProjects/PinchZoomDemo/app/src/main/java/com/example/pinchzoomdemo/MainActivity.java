package com.example.pinchzoomdemo;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class MainActivity extends ActionBarActivity implements OnTouchListener {

    // เมตริกซ์ที่ใช้ในการทำ transformation
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    // เราแบ่งสถานะการทัชออกเป็น 3 สถานะคือ NONE, DRAG และ ZOOM
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // ตัวแปรที่ใช้ในการย่อ-ขยายภาพ
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDistance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView view = (ImageView) findViewById(R.id.image);
        view.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ImageView view = (ImageView) v;

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                mode = DRAG;
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
                }
                else if (mode == ZOOM) {
                    float newDistance = getDistance(event);
                    if (newDistance > 10f) {
                        matrix.set(savedMatrix);
                        float scale = newDistance / oldDistance;
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                oldDistance = getDistance(event);
                if (oldDistance > 10f) {
                    savedMatrix.set(matrix);
                    getMidPoint(mid, event);
                    mode = ZOOM;
                }
                break;
        }

        view.setImageMatrix(matrix);
        return true; // บอกแอนดรอยด์ว่าเราจัดการอีเวนต์แล้ว
    }

    // ใช้หาระยะห่างระหว่างนิ้วทั้งสอง
    private float getDistance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    // ใช้หาจุดกึ่งกลางระหว่างนิ้วทั้งสอง
    private void getMidPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }
}
