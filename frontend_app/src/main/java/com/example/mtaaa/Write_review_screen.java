package com.example.mtaaa;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class Write_review_screen extends AppCompatActivity {

    private static String rJson;
    public void setrJson(String str){
        Write_review_screen.rJson = str;
    }
    public String getrJson(){
        return Write_review_screen.rJson;
    }

    RatingBar rtb;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

        getJson(JSONSaved.getUrl()+"/places/data/"+JSONSaved.getPlaceid());

        rtb = findViewById(R.id.ratingBar);
        rtb.setStepSize(1); //set color
        Button button2 = findViewById(R.id.uploadReview);
        button2.setOnClickListener(v -> upload());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void upload() {

        TextView txt = findViewById(R.id.reviewedPlace);
        EditText editText = findViewById(R.id.reviewTextInput);


        int rating = (int) rtb.getRating();
        String review = editText.getText().toString();
        String name = JSONSaved.getUser_name();


        SendReview(name,rating,review,JSONSaved.getUrl()+"/places/reviews/create/"+JSONSaved.getPlaceid()+"/"+JSONSaved.getUser());

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void update() {
        LinearLayout ln = findViewById(R.id.linear);
        try {
            JSONObject obj = new JSONObject(getrJson());
            if(obj.has("place")) {

                JSONObject obj3 = (JSONObject) obj.get("place");

                String name = obj3.getString("name");
                TextView txt = findViewById(R.id.reviewedPlace);
                txt.setText(name);
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void SendReview(String name, int raiting, String text, String url){

        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("userUsername", name);
            jsonBody.put("reviewText", text);
            jsonBody.put("revPhoto", "");
            jsonBody.put("rating", raiting);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String requestBody = jsonBody.toString();

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest sr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        setrJson(response);
                        //Logg();
                        Log.e("HttpClient", "success! response: " + response.toString());
                        change();


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(Write_review_screen.this);
                        builder.setCancelable(true);
                        builder.setTitle("Something went wrong");
                        //builder.setMessage("name or password you typed is wrong");
                        //passField.setText("");

                        Log.e("HttpClient", "error: " + error.toString());
                        builder.show();
                    }
                })
        {

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                int StatusCode = response.statusCode;
                return super.parseNetworkResponse(response);

            }

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

    public void change()
    {
        Toast.makeText(getApplicationContext(),"Review succesfully send in",Toast.LENGTH_SHORT).show();
        finish();
        Intent intent = new Intent(this, Reviews_screen.class);
        startActivity(intent);
    }
}