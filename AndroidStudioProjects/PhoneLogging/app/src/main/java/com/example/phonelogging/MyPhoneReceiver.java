package com.example.phonelogging;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class MyPhoneReceiver extends BroadcastReceiver {
    private static final String TAG = "PhoneLogging";

    @Override
    public void onReceive(Context context, Intent intent) {
        // อ่านข้อมูลทั้งหมดในอินเทนต์ (ข้อมูลในอินเทนต์จะถูกเก็บเป็นคู่ๆในรูปแบบคีย์/ค่า)
        Bundle extras = intent.getExtras();

        if (extras != null) {
            // อ่านสถานะของโทรศัพท์ 
            String state = extras.getString(TelephonyManager.EXTRA_STATE);
            String msg = "สถานะของโทรศัพท์เปลี่ยนเป็น " + state;
            Log.d(TAG, state);

            // ถ้าสถานะของโทรศัพท์คือ มีสายเรียกเข้า 
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                // อ่านหมายเลขโทรเข้า 
                String phoneNumber = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                msg += "\nหมายเลขโทรเข้าคือ " + phoneNumber;
                Log.d(TAG, phoneNumber);
            }

            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
    }
}
