package com.example.mtaaa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button1 = findViewById(R.id.button_login);
        button1.setOnClickListener(v -> openLog());

        Button button2 = findViewById(R.id.button_register);
        button2.setOnClickListener(v -> openReg());

        Button button3 = findViewById(R.id.button_guest);
        button3.setOnClickListener(v -> openHome());
    }

    public void openLog() {
        Intent intent = new Intent(this, Log_screen.class);
        startActivity(intent);
    }

    public void openReg() {
        Intent intent = new Intent(this, Reg_screen.class);
        startActivity(intent);
    }

    public void openHome() {
        Intent intent = new Intent(this, Home_screen.class);
        startActivity(intent);
    }
}