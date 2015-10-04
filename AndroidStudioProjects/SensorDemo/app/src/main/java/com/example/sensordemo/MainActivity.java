package com.example.sensordemo;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements SensorEventListener {

    private TextView output;
    private SensorManager mgr;
    private Sensor accelSensor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        output = (TextView) findViewById(R.id.output);
        // ขอใช้บริการด้านเซ็นเซอร์
        mgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        // เข้าถึง Accelerometer
        accelSensor = mgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // ลงทะเบียนเพื่ออัพเดทค่าจาก Accelerometer เป็นระยะๆ
        mgr.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // หยุดการอัพเดทค่าจากเซ็นเซอร์ เพื่อประหยัดแบตเตอรี่
        mgr.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        StringBuilder msg = new StringBuilder();

        msg.append("--- SENSOR ---");
        msg.append("\nชื่อ: ");
        msg.append(sensor.getName());
        msg.append("\nชนิด: ");
        msg.append(sensor.getType());
        msg.append("\nผู้ผลิต: ");
        msg.append(sensor.getVendor());
        msg.append("\nเวอร์ชั่น: ");
        msg.append(sensor.getVersion());
        msg.append("\nค่าสูงสุด: ");
        msg.append(sensor.getMaximumRange());

        msg.append("\n\n--- EVENT ---");
        msg.append("\nAccuracy: ");
        msg.append(event.accuracy);
        msg.append("\nTimestamp: ");
        msg.append(event.timestamp);
        msg.append("\nข้อมูล:\n");

        for (int i = 0; i < event.values.length; i++) {
            msg.append("   [ ");
            msg.append(i);
            msg.append(" ] = ");
            msg.append(event.values[i]);
            msg.append("\n");
        }
        output.setText(msg);
    }
}
