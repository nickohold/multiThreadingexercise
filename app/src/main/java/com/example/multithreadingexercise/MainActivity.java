package com.example.multithreadingexercise;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Handler mMainThreadHandler;
    private Handler mCalcThreadHandler;
    private Handler mSumCalcThreadHandler;
    private int totalSum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCalcThreadHandler = getNewThreadHandler("CalcThread");
        mSumCalcThreadHandler = getNewThreadHandler("sumCalcThread");
        mMainThreadHandler = new Handler(Looper.getMainLooper());
    }

    public Handler getNewThreadHandler(String threadName) {
        HandlerThread handlerThread = new HandlerThread(threadName);
        handlerThread.start();
        return new Handler(handlerThread.getLooper());
    }

    public void sumClick(View v) {
        mCalcThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                int sum = getEditTextNum("num1") + getEditTextNum("num2");
                finishCalc(sum);
            }
        });
    }

    private void finishCalc(int sum) {
        updateSumUi(Integer.toString(sum));
        mSumCalcThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                totalSum += sum;
                updateTotalSumUi(Integer.toString(totalSum));
            }
        });

    }

    private void updateSumUi(String message) {
        String currentThreadName = Thread.currentThread().getName();
        String constText = "Updated from thread " + currentThreadName + ": " + message;
        mMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                TextView sumTextView = findViewById(R.id.sumTextView);
                sumTextView.setText(sumTextView.getText() + "\n" + constText);
            }
        });
    }

    private void updateTotalSumUi(String message) {
        String currentThreadName = Thread.currentThread().getName();
        String constText = "Updated from thread " + currentThreadName + ": " + message;
        mMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                TextView totalSumTextView = findViewById(R.id.totalSumTextView);
                totalSumTextView.setText(totalSumTextView.getText() + "\n" + constText);
            }
        });
    }

    private int getEditTextNum(String id) {
        int editTextId = getResources().getIdentifier(id, "id", getPackageName());
        EditText editText = findViewById(editTextId);
        String temp = editText.getText().toString();
        int num = 0;
        if (!"".equals(temp)){
            num = Integer.parseInt(temp);
        }
        return num;
    }
}