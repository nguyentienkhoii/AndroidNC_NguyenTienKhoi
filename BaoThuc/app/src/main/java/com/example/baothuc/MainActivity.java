package com.example.baothuc;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.util.Calendar;

public class MainActivity extends Activity {

    private TimePicker timePicker;
    private EditText editTextMinutes;
    private Button setAlarmButton, setTimerButton, stopButton;
    private Vibrator vibrator;
    private PendingIntent pendingIntent;
    private PendingIntent timerPendingIntent;
    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timePicker = findViewById(R.id.timePicker);
        editTextMinutes = findViewById(R.id.editTextMinutes);
        setAlarmButton = findViewById(R.id.setAlarmButton);
        setTimerButton = findViewById(R.id.setTimerButton);
        stopButton = findViewById(R.id.stopButton);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);
            }
        }

        setAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                calendar.set(Calendar.MINUTE, timePicker.getMinute());
                calendar.set(Calendar.SECOND, 0);

                Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                Toast.makeText(MainActivity.this, "Đã cài báo thức vào " + timePicker.getHour() + ":" + timePicker.getMinute(), Toast.LENGTH_SHORT).show();
            }
        });

        setTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int minutes = Integer.parseInt(editTextMinutes.getText().toString());
                    long triggerAtMillis = System.currentTimeMillis() + (minutes * 60 * 1000);

                    Intent intent = new Intent(MainActivity.this, TimerReceiver.class);
                    timerPendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, timerPendingIntent);

                    Toast.makeText(MainActivity.this, "Đã cài hẹn giờ " + minutes + " phút", Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập số phút muốn hẹn giờ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vibrator != null && vibrator.hasVibrator()) {
                    vibrator.cancel();
                }
                if (pendingIntent != null) {
                    alarmManager.cancel(pendingIntent);
                }
                if (timerPendingIntent != null) {
                    alarmManager.cancel(timerPendingIntent);
                }
                Toast.makeText(MainActivity.this, "Dừng thông báo", Toast.LENGTH_SHORT).show();
            }
        });
    }
}