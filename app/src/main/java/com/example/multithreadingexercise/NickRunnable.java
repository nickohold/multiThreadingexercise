package com.example.multithreadingexercise;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Thread.currentThread;

public class NickRunnable implements Runnable{
    private int num1;
    private int num2;
    private Context context;

    public NickRunnable(Context context, int num1, int num2) {
        this.num1 = num1;
        this.num2 = num2;
        this.context = context;
    }

    @Override
    public void run() {
        try {
            String currentThread = currentThread().getName();
            updateResultsTextBox(currentThread + " - " + "Get Ready!");
            Thread.sleep(1000);
            updateResultsTextBox(currentThread + " - " + Integer.toString(this.num1 + this.num2));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void updateResultsTextBox(String msg) {
        Activity activity = (Activity) context;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView resTextView = activity.findViewById(R.id.resTextView);
                resTextView.setText(msg);
            }
        });
    }
}
