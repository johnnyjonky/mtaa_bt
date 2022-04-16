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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    
    int id = JSONSaved.getUser();
    int admin = JSONSaved.getIsadmin();
    
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_screen);
        Button review = findViewById(R.id.reviewButton);
        review.setVisibility(View.INVISIBLE);
        getJson(JSONSaved.getUrl()+"/places/data/"+JSONSaved.getPlaceid());
    }
    
    Button button1 = findViewById(R.id.button_del_place);
    button1.setOnClickListener(v -> delete_place());

    Button button2 = findViewById(R.id.button_edit_place);
    button2.setOnClickListener(v -> edit_place());

    if(JSONSaved.getUser() == 0) {
        button2.setVisibility(View.INVISIBLE); }
    if(JSONSaved.getIsadmin() == 0) {
        button1.setVisibility(View.INVISIBLE); }
    }

    public void edit_place()
    {
        Intent intent = new Intent(this, Edit_place_screen.class);
        startActivity(intent);
    }

    public void delete_place()
    {
        int userid = JSONSaved.getUser();
        int placeid = JSONSaved.getPlaceid();
        String delete_url = JSONSaved.getUrl()+"/places/delete/" + placeid + "/" + userid;

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, delete_url,
                response -> {
                    setrJson(response);
                    refresh();
                }, error -> setrJson("Something went wrong!"));
        queue.add(stringRequest);
    }

    public void refresh()
    {
        Toast.makeText(getApplicationContext(),"Successfully deleted",Toast.LENGTH_SHORT).show();
        finish();

        Intent intent = new Intent(this, Places_screen.class);
        startActivity(intent);
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
                Button review = findViewById(R.id.reviewButton);
                Transition transition = new Fade(Fade.MODE_OUT);
                transition.setDuration(300);
                transition.addTarget(load);
                transition.addTarget(review);
                TransitionManager.beginDelayedTransition(findViewById(android.R.id.content),transition);
                load.setVisibility(View.INVISIBLE);
                review.setVisibility(View.VISIBLE);
                Log.i("TEST", String.valueOf(obj3.length()));
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
