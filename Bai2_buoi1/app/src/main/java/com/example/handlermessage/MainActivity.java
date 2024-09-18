package com.example.handlermessage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView textView1, textView2, textView3;
    private Button button1, button2, button3;

    private Handler handler;
    private Thread thread1, thread2, thread3;

    private boolean runningThread1 = false;
    private boolean runningThread2 = false;
    private boolean runningThread3 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);


        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        textView1.setText(String.valueOf(msg.arg1));
                        break;
                    case 2:
                        textView2.setText(String.valueOf(msg.arg1));
                        break;
                    case 3:
                        textView3.setText(String.valueOf(msg.arg1));
                        break;
                }
                return true;
            }
        });


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!runningThread1) {
                    runningThread1 = true;
                    startThread1();
                    button1.setText("Stop");
                } else {
                    runningThread1 = false;
                    button1.setText("Start");
                }
            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!runningThread2) {
                    runningThread2 = true;
                    startThread2();
                    button2.setText("Stop");
                } else {
                    runningThread2 = false;
                    button2.setText("Start");
                }
            }
        });


        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!runningThread3) {
                    runningThread3 = true;
                    startThread3();
                    button3.setText("Stop");
                } else {
                    runningThread3 = false;
                    button3.setText("Start");
                }
            }
        });
    }


    private void startThread1() {
        thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                while (runningThread1) {
                    int number = random.nextInt(51) + 50;
                    Message msg = handler.obtainMessage(1);
                    msg.arg1 = number;
                    handler.sendMessage(msg);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread1.start();
    }


    private void startThread2() {
        thread2 = new Thread(new Runnable() {
            int oddNumber = 1;
            @Override
            public void run() {
                while (runningThread2) {
                    Message msg = handler.obtainMessage(2);
                    msg.arg1 = oddNumber;
                    handler.sendMessage(msg);
                    oddNumber += 2;
                    try {
                        Thread.sleep(2500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread2.start();
    }

    private void startThread3() {
        thread3 = new Thread(new Runnable() {
            int number = 0;
            @Override
            public void run() {
                while (runningThread3) {
                    Message msg = handler.obtainMessage(3);
                    msg.arg1 = number;
                    handler.sendMessage(msg);
                    number++;
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread3.start();
    }
}
