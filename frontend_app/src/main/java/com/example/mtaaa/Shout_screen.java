package com.example.mtaaa;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Timer;
import java.util.TimerTask;

public class Shout_screen extends AppCompatActivity {
    private static String rJson;
    public void setrJson(String str){
        Shout_screen.rJson = str;
    }
    public String getrJson(){
        return Shout_screen.rJson;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoutbox);
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getJson(JSONSaved.getUrl()+"/shoutbox/data");
            }
        }, 0, 5000);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void update() {
        LinearLayout ln = findViewById(R.id.shouts);
        ln.removeAllViews();
        try {
            JSONObject obj = new JSONObject(getrJson());
            if(obj.has("shoutbox")){
                JSONArray obj3 = (JSONArray) obj.get("shoutbox");
                Log.i("TEST", String.valueOf(obj3.length()));
                for (int i = obj3.length()-1; i >= 0; i--) {
                    Log.i("FOR", String.valueOf(i));
                    JSONObject obj4 = obj3.getJSONObject(i);
                    if(obj4.has("user")){
                        View child = getLayoutInflater().inflate(R.layout.shout_instance,null);
                        TextView label = child.findViewById(R.id.shoutUsername);
                        label.setText(obj4.getString("user"));
                        TextView desc = child.findViewById(R.id.shoutTextView);
                        TextView timestamp = child.findViewById(R.id.shoutTimestamp);
                        if(obj4.has("text")){
                            desc.setText(obj4.getString("text"));
                        }
                        else{
                            desc.setText("No message");
                        }
                        if(obj4.has("timestamp")){
                            String time = String.valueOf(java.time.Instant.parse(obj4.getString("timestamp")).atZone(ZoneId.systemDefault()).toLocalTime());
                            String date = String.valueOf(java.time.Instant.parse(obj4.getString("timestamp")).atZone(ZoneId.systemDefault()).toLocalDate());
                            timestamp.setText(time + ", " + date);
                        }
                        ln.addView(child);
                    }
                }
            }
        } catch (JSONException e) {
            Log.e("JSONERROR", "unexpected JSON exception", e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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
