package com.example.boundservicedemo;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class MyService extends Service {
    private static final long UPDATE_INTERVAL = 5000;
    private final IBinder mBinder = new MyBinder();
    private Timer timer = new Timer();
    private ArrayList<String> list = new ArrayList<String>();
    private String[] fixedList = { "Java", "PHP", "C++", "C#", "Visual Basic", "Python", "Ruby" };
    private int i = 0;

    @Override
    public void onCreate() {
        pollForUpdates();
    }

    /* เมธอดที่จำลองการอัพเดทข้อมูลทุกๆ 5 วินาที การทำงานในที่นี้คือ จะนำข้อมูลจากอาร์เรย์
     * fixedList เพิ่มลงในอาร์เรย์ list โดยไล่ไปทีละตัวตามลำดับ และเมื่อถึงข้อมูลตัวสุดท้ายใน
     * fixedList ก็จะกลับมาเริ่มที่ตัวแรกใหม่ ทั้งนี้จะเก็บข้อมูลไว้ใน list ไม่เกิน 5 ตัวเท่านั้น
     * ถ้าเกินจะลบข้อมูลแรกออกไป */
    private void pollForUpdates() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                list.add(fixedList[i++]);
                if (i >= fixedList.length) {
                    i = 0;
                }
                if (list.size() > 5) {
                    list.remove(0);
                }
            }
        }, 0, UPDATE_INTERVAL);
    }

    @Override
    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;  // ออบเจ็ค MyBinder
    }

    /* คลาส MyBinder สืบทอดจากคลาส Binder และมีเมธอด getService ที่จะส่งคืนตัวเซอร์วิซ
     * (ออบเจ็ค MyService) ออกไปเพื่อให้ client อ้างอิงถึงเซอร์วิซได้ */
    public class MyBinder extends Binder {
        MyService getService() {
            return MyService.this;
        }
    }

    // เมธอดที่เตรียมไว้ให้ client มาเรียกใช้ความสามารถของเซอร์วิซ
    public ArrayList<String> getWordList() {
        return list;
    }
}
