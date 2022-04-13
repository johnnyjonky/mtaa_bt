package com.example.mtaaa;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Log_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        Button button = findViewById(R.id.button_logg);
        button.setOnClickListener(v -> loggin());
    }

    public void loggin() {
        EditText edt = findViewById(R.id.name);
        String name = edt.getText().toString();

        EditText passField = findViewById(R.id.Password);
        String password = passField.getText().toString();

        JSONObject user = new JSONObject();
        try {
            user.put("name", name);
            user.put("pw",password);

        } catch (JSONException e)
        {
            e.printStackTrace();
        }


        /*
        try {
            URL url = new URL("http//localhost:8080/users/login");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int respons = conn.getResponseCode();
            button.setText(respons);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

         */

        String API = "http://10.0.2.2:3000";



        SQLiteDatabase db;


        //Intent intent = new Intent(this, Home_screen.class);
        //startActivity(intent);
    }
}