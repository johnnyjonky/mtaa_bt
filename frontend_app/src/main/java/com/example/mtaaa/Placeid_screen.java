package com.example.mtaaa;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class Placeid_screen extends AppCompatActivity {

    private static String rJson;
    public void setrJson(String str){
        Placeid_screen.rJson = str;
    }
    public String getrJson(){
        return Placeid_screen.rJson;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_screen);
        getJson(JSONSaved.getUrl()+"/places/data/"+JSONSaved.getPlaceid());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void update() {
        try {
            JSONObject obj = new JSONObject(getrJson());
            if(obj.has("place")){
                JSONObject obj3 = (JSONObject) obj.get("place");
                TextView name = findViewById(R.id.placeNameBig);
                TextView shortDesc = findViewById(R.id.placeShortDesc);
                TextView longDesc = findViewById(R.id.placeLongDesc);
                TextView location = findViewById(R.id.placeLocation);
                ImageView image = findViewById(R.id.placeImg);
                image.setBackgroundResource(R.drawable.shape);
                image.setImageResource(R.drawable.mtaa_logo);
                if(obj3.has("name")) name.setText(obj3.getString("name"));
                if(obj3.has("shortDescription")) shortDesc.setText(obj3.getString("shortDescription"));
                if(obj3.has("longDescription")) longDesc.setText(obj3.getString("longDescription"));
                if(obj3.has("location")) location.setText("Location: " + obj3.getString("location"));
                ConstraintLayout load = findViewById(R.id.loadingPT);
                Transition transition = new Slide(Gravity.TOP);
                transition.setDuration(300);
                transition.addTarget(load);
                TransitionManager.beginDelayedTransition(findViewById(android.R.id.content),transition);
                load.setVisibility(View.INVISIBLE);
                Log.i("TEST", String.valueOf(obj3.length()));
                Button review = findViewById(R.id.reviewButton);
                review.setOnClickListener(v -> {
                        Intent intent = new Intent(this, Reviews_screen.class);
                        startActivity(intent);
                });
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