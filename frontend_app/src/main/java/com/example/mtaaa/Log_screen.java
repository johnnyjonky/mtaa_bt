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

        //getJson(JSONSaved.getUrl()+"/placetypes");


        /*
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, "http://10.0.2.2:8080/users/login", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("loggin_status");

                            for(int i = 0; i < array.length(); i++)
                            {
                                //JSONObject log_user = jsonArray.getJSON;
                                JSONObject log_user = array.getJSONObject(i);
                                String status = log_user.getString("status");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }
        );


         */

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
        */

        String url = "https://e190-95-102-14-246.eu.ngrok.io/users/login";
        RequestQueue queue = Volley.newRequestQueue(this);

        Map<String, String> params = new HashMap<String, String>();
        params.put("username", "admin");
        params.put("pw", "heslo");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, new JSONObject(params),
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


        //Intent intent = new Intent(this, Home_screen.class);
        //startActivity(intent);
    }


}