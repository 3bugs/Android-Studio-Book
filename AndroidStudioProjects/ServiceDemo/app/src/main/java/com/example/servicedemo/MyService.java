package com.example.servicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {

    private MyThread thread;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        thread = new MyThread();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started..", Toast.LENGTH_LONG).show();
        if (!thread.isAlive()) {
            thread.start();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Stopped..", Toast.LENGTH_LONG).show();
        thread.finish = true;  // ส่งสัญญาณบอกให้เธรดจบการทำงาน
    }

    private class MyThread extends Thread {
        private static final String TAG = "ServiceDemo";
        private static final int DELAY = 3000;
        private int i = 0;
        private boolean finish = false;

        public void run() {
            // พิมพ์ค่า i ออกมาในวินโดว์ LogCat ทุกๆ 3 วินาที 
            while (true) {
                Log.d(TAG, Integer.toString(i++));

                // หยุดพัก 3 วินาที
                try {
                    sleep(DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // ถ้าคลิกปุ่ม Stop Service ให้ออกจากลูป (เธรดจบการทำงาน)
                if (finish) {
                    return;
                }
            }
        }
    }
}
