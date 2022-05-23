package com.example.multithreadingexercise;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sumClick(View v) {
        int num1 = getEditTextNum("num1");
        int num2 = getEditTextNum("num2");
        NickRunnable runnable = new NickRunnable(this, num1, num2);
        Thread thread1 = new Thread(runnable);
        thread1.start();
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