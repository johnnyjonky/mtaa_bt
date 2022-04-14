package com.example.mtaaa;

import android.app.DownloadManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ResourceCursorAdapter;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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

import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class Log_screen extends AppCompatActivity {

    Button button;
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

        JSONObject user = new JSONObject();
        try {
            user.put("name", name);
            user.put("pw",password);

        } catch (JSONException e)
        {
            e.printStackTrace();
        }


        /*

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest str = new StringRequest(Request.Method.POST, "https://e190-95-102-14-246.eu.ngrok.io/users/register",
                response -> {
                    button.setText("a");
                }, error -> {
                    button.setText("b");
                }){
            protected Map<String,String> getParams() throws AuthFailureError{
                Map<String,String> params = new HashMap<>();
                params.put("pw","heslo");
                params.put("username", "adminko");
                return params;
            }
        };
        queue.add(str);


        String url = "https://e190-95-102-14-246.eu.ngrok.io/users/login";
        RequestQueue queue = Volley.newRequestQueue(this);

        Map<String, String> params = new HashMap<String, String>();
        params.put("username", "admin");
        params.put("pw", "heslo");

        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("username", "admin");
            jsonBody.put("pw", "heslo");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, jsonBody,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JSONPost", response.toString());
                        //pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("JSONPost", "Error: " + error.getMessage());
                //pDialog.hide();
            }
        });
        queue.add(jsonObjReq);


        SQLiteDatabase db;


         */


        RequestQueue queue = Volley.newRequestQueue(this);
        //for POST requests, only the following line should be changed to

        StringRequest sr = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("HttpClient", "success! response: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("HttpClient", "error: " + error.toString());
                        button.setText(error.toString());
                    }
                })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("username","admin");
                params.put("pw","heslo");
                return params;
            }
            /*
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","raw");
                return params;
            }

             */
        };
        queue.add(sr);

        //Intent intent = new Intent(this, Home_screen.class);
        //startActivity(intent);
    }


}