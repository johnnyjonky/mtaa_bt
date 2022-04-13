package com.example.mtaaa;

import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Home_screen extends AppCompatActivity {

    private static String rJson;
    public void setrJson(String str){
        Home_screen.rJson = str;
    }
    public String getrJson(){
        return Home_screen.rJson;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Button rtc = findViewById(R.id.RTCbutton);
        rtc.setVisibility(View.INVISIBLE);
        getJson("https://6159-147-175-190-171.ngrok.io/placetypes");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void update() {
        LinearLayout ln = findViewById(R.id.linear);
        try {
            JSONObject obj = new JSONObject(getrJson());
            if(obj.has("placetypes")){
                JSONArray obj3 = (JSONArray) obj.get("placetypes");
                Log.i("TEST", String.valueOf(obj3.length()));
                for (int i = 0; i < obj3.length(); i++) {
                    JSONObject obj4 = obj3.getJSONObject(i);
                    if(obj4.has("placeName")){
                        View child = getLayoutInflater().inflate(R.layout.placetype_button,null);
                        TextView label = child.findViewById(R.id.placeTypeName);
                        label.setText(obj4.getString("placeName"));
                        ln.addView(child);
                        ConstraintLayout load = findViewById(R.id.loading);
                        Button rtc = findViewById(R.id.RTCbutton);
                        Transition transition = new Slide(Gravity.TOP);
                        transition.setDuration(300);
                        transition.addTarget(load);
                        transition.addTarget(rtc);
                        TransitionManager.beginDelayedTransition(findViewById(android.R.id.content),transition);
                        load.setVisibility(View.INVISIBLE);
                        rtc.setVisibility(View.VISIBLE);
                    }
                }
            }
        } catch (JSONException e) {
            Log.e("JSONERROR", "unexpected JSON exception", e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void getJson(String url){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    setrJson(response);
                    update();
                }, error -> setrJson("Something went wrong!"));
        queue.add(stringRequest);
    }
}