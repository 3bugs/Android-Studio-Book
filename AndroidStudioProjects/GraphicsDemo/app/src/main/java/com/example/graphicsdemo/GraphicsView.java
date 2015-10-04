package com.example.graphicsdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class GraphicsView extends View {
    public GraphicsView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // วาดสี่เหลี่ยม
        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(50, 50, 200, 200, paint);

        // วาดกรอบสี่เหลี่ยม
        paint.setColor(Color.argb(127, 255, 0, 0));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        canvas.drawRect(100, 100, 450, 320, paint);

        // วาดข้อความ
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.text_color));
        paint.setAntiAlias(true);
        paint.setTextSize(80);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("แอนดรอยด์", getWidth() / 2, 400, paint);

        // วาดภาพจากไฟล์
        Bitmap image = BitmapFactory.decodeResource(getResources(),
                R.drawable.family);
        canvas.drawBitmap(image, 100, 450, null);

        image = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);
        canvas.drawBitmap(image, 20, 400, null);
    }
}
