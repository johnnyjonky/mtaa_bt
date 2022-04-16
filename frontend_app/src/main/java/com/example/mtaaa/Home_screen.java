package com.example.mtaaa;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
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
        Button sb = findViewById(R.id.SBbutton);
        sb.setVisibility(View.INVISIBLE);
        rtc.setOnClickListener(v -> {
            Intent intent = new Intent(this, RTCConnScreen.class);
            startActivity(intent);
        });
        sb.setOnClickListener(v -> {
            Intent intent = new Intent(this, Shout_screen.class);
            startActivity(intent);
        });
        getJson(JSONSaved.getUrl()+"/placetypes");
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
                        child.setOnClickListener(v -> {
                            try {
                                openByPlacetype(obj4.getInt("placeID"),obj4.getString("placeName"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                        label.setText(obj4.getString("placeName"));
                        ln.addView(child);
                        ConstraintLayout load = findViewById(R.id.loading);
                        Button rtc = findViewById(R.id.RTCbutton);
                        Button sb = findViewById(R.id.SBbutton);
                        Transition transition = new Fade(Fade.MODE_OUT);
                        transition.setDuration(300);
                        transition.addTarget(load);
                        transition.addTarget(rtc);
                        transition.addTarget(sb);
                        TransitionManager.beginDelayedTransition(findViewById(android.R.id.content),transition);
                        load.setVisibility(View.INVISIBLE);
                        rtc.setVisibility(View.VISIBLE);
                        sb.setVisibility(View.VISIBLE);
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

    public void openByPlacetype(int placetype, String placetypeName) {
        JSONSaved.setPlacetype(placetype);
        JSONSaved.setPlacetypeName(placetypeName);
        Intent intent = new Intent(this, Places_screen.class);
        startActivity(intent);
    }
}