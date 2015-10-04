package com.example.livewallpaper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public class MyWallpaperService extends WallpaperService {
    @Override
    public Engine onCreateEngine() {
        return new MyEngine();
    }

    class MyEngine extends Engine {
        private final Handler handler = new Handler();
        private Runnable runnable;

        private float surfaceWidth;   // ความกว้างของ Surface
        private float surfaceHeight;  // ความสูงของ Surface
        private float currentX = 0f;  // ตำแหน่งในแนวนอนที่จะวาดรูปหุ่นแอนดรอยด์ลงไป
        private float currentY = 0f;  // ตำแหน่งในแนวตั้งที่จะวาดรูปหุ่นแอนดรอยด์ลงไป
        private float offsetX = 5;    // ระยะที่จะขยับรูปหุ่นแอนดรอยด์ไปในแนวนอน
        private float offsetY = 4;    // ระยะที่จะขยับรูปหุ่นแอนดรอยด์ไปในแนวตั้ง

        private boolean visible;      // สถานะการมองเห็นได้ของ live wallpaper
        private Bitmap image;         // รูปหุ่นแอนดรอยด์

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);

            // ใช้รูปไอคอนที่ Eclipse เตรียมมาให้ ซึ่งจะเป็นรูปหุ่นแอนดรอยด์
            image = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            runnable = new Runnable() {
                public void run() {
                    drawFrame();
                }
            };
        }

        void drawFrame() {
            final SurfaceHolder holder = getSurfaceHolder();

            Canvas c = null;
            try {
                c = holder.lockCanvas(); // เริ่มต้นแก้ไข Canvas
                if (c != null) {
                    // ระบายพื้นหลังด้วยสีดำ และวาดรูปหุ่นแอนดรอยด์ที่ตำแหน่ง currentX, currentY
                    c.drawColor(Color.BLACK);
                    c.drawBitmap(image, currentX, currentY, null);

                    currentX += offsetX;  // เปลี่ยนตำแหน่งการวาดรูปในแนวนอน
                    /* ถ้าถึงขอบด้านซ้ายของหน้าจอให้เคลื่อนที่กลับมาทางขวา และถ้าถึงขอบด้านขวา
                       ให้เคลื่อนที่กลับมาทางซ้าย */
                    if (currentX + image.getWidth() >= surfaceWidth) {
                        offsetX = -Math.abs(offsetX);
                    }
                    else if (currentX <= 0) {
                        offsetX = Math.abs(offsetX);
                    }

                    currentY += offsetY;  // เปลี่ยนตำแหน่งการวาดรูปในแนวตั้ง
                    /* ถ้าถึงขอบด้านบนของหน้าจอให้เคลื่อนที่กลับลงมา และถ้าถึงขอบด้านล่าง
                       ให้เคลื่อนที่กลับขึ้นมา */
                    if (currentY + image.getHeight() >= surfaceHeight) {
                        offsetY = -Math.abs(offsetY);
                    }
                    else if (currentY <= 0) {
                        offsetY = Math.abs(offsetY);
                    }
                }
            } finally {
                if (c != null)
                    holder.unlockCanvasAndPost(c); // สิ้นสุดการแก้ไข Canvas
            }

            // ตั้งเวลาในการวาดเฟรมถัดไป
            handler.removeCallbacks(runnable);
            if (visible) {
                handler.postDelayed(runnable, 200);
            }
        }

        @Override
        public void onDestroy() {
            super.onDestroy();

            handler.removeCallbacks(runnable);
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);

            visible = false;
            handler.removeCallbacks(runnable);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);

            surfaceWidth = width;
            surfaceHeight = height;
            drawFrame();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);

            this.visible = visible;
            if (visible) {
                drawFrame();
            } else {
                handler.removeCallbacks(runnable);
            }
        }
    }
}
