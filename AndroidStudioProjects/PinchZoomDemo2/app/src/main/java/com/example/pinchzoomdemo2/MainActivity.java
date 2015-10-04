package com.example.pinchzoomdemo2;

import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class MainActivity extends ActionBarActivity implements OnTouchListener {

    private ScaleGestureDetector sgd;
    Matrix matrix = new Matrix();  // เมตริกซ์ที่ใช้ทำ Transformation

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sgd = new ScaleGestureDetector(this, new ScaleListener());

        ImageView view = (ImageView) findViewById(R.id.image);
        view.setOnTouchListener(this);
    }

    public boolean onTouch(View v, MotionEvent event) {
        sgd.onTouchEvent(event);

        ImageView view = (ImageView) v;
        view.setImageMatrix(matrix);

        return true; // บอกแอนดรอยด์ว่าเราจัดการอีเวนต์แล้ว
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            matrix.postScale(scaleFactor, scaleFactor);

            return true; // บอกแอนดรอยด์ว่าเราจัดการอีเวนต์แล้ว
        }
    }
}
