package com.example.multithreadingexercise;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "NICKTag";
    private Handler mMainThreadHandler;
    private Handler mCalcThreadHandler;
    private Handler mSumCalcThreadHandler;
    private List<Integer> mSumsArrayList = Collections.synchronizedList(new ArrayList<Integer>());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setScrollableViews();

        mCalcThreadHandler = getNewThreadHandler("CalcThread");
        mSumCalcThreadHandler = getNewThreadHandler("sumCalcThread");
        mMainThreadHandler = new Handler(Looper.getMainLooper());

        setRandValueToEditTextById("num1");
        setRandValueToEditTextById("num2");
    }

    private void setScrollableViews() {
        TextView sumTextView = (TextView) getViewById("sumTextView");
        TextView totalSumTextView = (TextView) getViewById("totalSumTextView");
        sumTextView.setMovementMethod(new ScrollingMovementMethod());
        totalSumTextView.setMovementMethod(new ScrollingMovementMethod());
        TextView totalSumsArrayTextView = (TextView) getViewById("totalSumsArrayTextView");
        totalSumsArrayTextView.setMovementMethod(new ScrollingMovementMethod());
    }

    private Handler getNewThreadHandler(String threadName) {
        HandlerThread handlerThread = new HandlerThread(threadName);
        handlerThread.start();
        return new Handler(handlerThread.getLooper());
    }

    public void sumClick(View v) {
        mCalcThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                int sum = getEditTextAsInt("num1") + getEditTextAsInt("num2");
                finishCalc(sum);
            }
        });
    }

    private void finishCalc(int sum) {
        mSumsArrayList.add(sum);
        updateUiByView("sumTextView", Integer.toString(sum));
        mSumCalcThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                int sumOfAll = getSumOfArray();
                updateUiByView("totalSumTextView", Integer.toString(sumOfAll));
            }
        });
        mMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                setRandValueToEditTextById("num1");
                setRandValueToEditTextById("num2");
            }
        });
    }

    private int getSumOfArray() {
        synchronized(mSumsArrayList) {
            return mSumsArrayList.stream().reduce(0, (sum, next) -> sum + next);
        }
    }

    private void updateUiByView(String textViewId, String message) {
        String currentThreadName = Thread.currentThread().getName();
        String constText = getString(R.string.upatedFromThread) + currentThreadName + ": " + message;
        mMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                TextView totalSumTextView = (TextView) getViewById(textViewId);
                totalSumTextView.setText(totalSumTextView.getText() + "\n" + constText);
            }
        });
    }

    private int getEditTextAsInt(String id) {
        EditText editText = (EditText) getViewById(id);
        String temp = editText.getText().toString();
        int num = 0;
        if (!"".equals(temp)){
            num = Integer.parseInt(temp);
        }
        return num;
    }

    private void setRandValueToEditTextById(String id) {
        EditText editText = (EditText) getViewById(id);
        Random random = new Random();
        int ranNum = random.nextInt(10);
        editText.setText(Integer.toString(ranNum));
    }

    private View getViewById(String id) {
        int viewId = getResources().getIdentifier(id, "id", getPackageName());
        return findViewById(viewId);
    }

    public void updateClick(View v) {
        TextView totalSumsArrayTextView = findViewById(R.id.totalSumsArrayTextView);
        totalSumsArrayTextView.setText(TextUtils.join(", ",  mSumsArrayList));
    }
}