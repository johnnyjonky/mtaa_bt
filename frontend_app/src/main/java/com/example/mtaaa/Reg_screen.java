package com.example.mtaaa;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Reg_screen extends AppCompatActivity {

    private static String rJson;
    String name;
    public void Reg_setrJson(String str){ Reg_screen.rJson = str;    }
    public String Reg_getrJson(){ return Reg_screen.rJson;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        Button button = findViewById(R.id.button_reg);
        button.setOnClickListener(v -> register(JSONSaved.getUrl()+"/users/register"));
    }

    public void register(String url) {
        EditText edt = findViewById(R.id.nameRG);
        name = edt.getText().toString();

        EditText passField = findViewById(R.id.pwRG);
        String password = passField.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(this);

        Map<String, String> params = new HashMap<String, String>();
        params.put("username", name);
        params.put("pw",password);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
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

        loggin(JSONSaved.getUrl()+"/users/login",name,password);
    }

    public void loggin(String url, String name, String password) {

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
                        Reg_setrJson(response);
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
            JSONObject obj = new JSONObject(Reg_getrJson());
            if(obj.has("status")){

                String Status = obj.getString("status");
                int Admin = obj.getInt("admin");
                int UID = obj.getInt("UID");


                JSONSaved.setUser_name(name);
                JSONSaved.setUser(UID);
                Toast.makeText(getApplicationContext(),"Registered, we have logged you in automatically",Toast.LENGTH_LONG).show();
                finish();
                Intent intent = new Intent(this, Home_screen.class);
                startActivity(intent);

            }
        } catch (JSONException e) {
            Log.e("JSONERROR", "unexpected JSON exception", e);
        }
    }
}