package com.example.mtaaa;

import android.app.DownloadManager;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class Log_screen extends AppCompatActivity {

    Button button;
    private static String rJson;
    public void Log_setrJson(String str){
        Log_screen.rJson = str;
    }
    public String Log_getrJson(){ return Log_screen.rJson;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        button = findViewById(R.id.button_logg);
        button.setOnClickListener(v -> loggin(JSONSaved.getUrl()+"/users/login"));
    }

    public void loggin(String url) {
        EditText edt = findViewById(R.id.name);
        String name = edt.getText().toString();

        EditText passField = findViewById(R.id.Password);
        String password = passField.getText().toString();

        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("username", name);
            jsonBody.put("pw", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String requestBody = jsonBody.toString();

        RequestQueue queue = Volley.newRequestQueue(this);
        //for POST requests, only the following line should be changed to

        StringRequest sr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log_setrJson(response);
                        Logg();
                        Log.e("HttpClient", "success! response: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("HttpClient", "error: " + error.toString());
                    }
                })
        {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }


        };
        queue.add(sr);


    }

    void Logg()
    {
        try {
            JSONObject obj = new JSONObject(Log_getrJson());
            if(obj.has("status")){

                String Status = obj.getString("status");
                int Admin = obj.getInt("admin");
                int UID = obj.getInt("UID");

                JSONSaved.setUser(UID,Admin);

                Intent intent = new Intent(this, Home_screen.class);
                startActivity(intent);

            }
        } catch (JSONException e) {
            Log.e("JSONERROR", "unexpected JSON exception", e);
        }
    }

}
