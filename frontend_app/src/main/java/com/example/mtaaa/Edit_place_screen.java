package com.example.mtaaa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;


public class Edit_place_screen extends AppCompatActivity {

    private static String rJson;
    private static String uploadedImage;

    private static final int RESULT_LOAD_IMAGE = 0;
    private static final int REQUEST_LOAD_IMAGE = 0;

    public void setrJson(String str){ Edit_place_screen.rJson = str; }
    public String getrJson(){
        return Edit_place_screen.rJson;
    }
    public static void setUploadedImage(String str) { Edit_place_screen.uploadedImage = str;}
    public String getUploadedImage(){return Edit_place_screen.uploadedImage;}

    TextView txtt;

    String name = JSONSaved.getPlace_name();
    String ldesc = JSONSaved.getPlace_longdesc();
    String sdesc = JSONSaved.getPlace_shortdesc();
    String loc = JSONSaved.getPlace_location();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_place_screen);

        Button button = findViewById(R.id.submit_place);
        button.setOnClickListener(v -> submit());

        EditText edt = (EditText) findViewById(R.id.placeTypeNameEdit);
        edt.setText(JSONSaved.getPlace_name());

        edt = findViewById(R.id.placeTypeSDestEdit);
        edt.setText(JSONSaved.getPlace_shortdesc());

        edt = findViewById(R.id.placeTypeLDestEdit);
        edt.setText(JSONSaved.getPlace_longdesc());

        edt = findViewById(R.id.placeTypeLocationEdit);
        edt.setText(JSONSaved.getPlace_location());

        get(JSONSaved.getUrl()+"/places/data/"+JSONSaved.getPlaceid());
        //get placetypes


        Button revph = findViewById(R.id.uploadPlacetypeImg);
        revph.setOnClickListener(v -> {
            Intent i = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(i, RESULT_LOAD_IMAGE);

        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri selectedImage = null;

        if (requestCode == REQUEST_LOAD_IMAGE && resultCode == RESULT_OK) {
            Log.i("upload","NOK");
            selectedImage = data.getData();
            InputStream in;
            try {
                in = getContentResolver().openInputStream(selectedImage);
                final Bitmap selected_img = BitmapFactory.decodeStream(in);
                encodeTobase64(selected_img);
                Button revph = findViewById(R.id.uploadPlacetypeImg);
                revph.setText("Photo chosen");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "An error occured!", Toast.LENGTH_LONG).show();
            }
            //Bitmap imageBitmap = (Bitmap) extras.get("data");
            Log.i("upload", String.valueOf(data));
        }
    }



    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex=Bitmap.createScaledBitmap(image,200,200,true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);

        Log.i("LOOK", String.valueOf(imageEncoded.length()));
        setUploadedImage(imageEncoded);

        return imageEncoded;
    }


    public void get(String url)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    setrJson(response);
                    update();
                }, error -> setrJson("Something went wrong!"));
        queue.add(stringRequest);
    }

    public void update()
    {
        try {
            JSONObject obj = new JSONObject(getrJson()); // not working xD
            if(obj.has("placetypes")){
                JSONObject obj3 = (JSONObject) obj.get("placetypes");

                EditText edt = (EditText) findViewById(R.id.placeTypeNameEdit);
                edt.setText("dsadasdasd",TextView.BufferType.EDITABLE);


                edt = findViewById(R.id.placeTypeSDestEdit);
                edt.setText(obj3.getString("shortDescription"));

                edt = findViewById(R.id.placeTypeLocationEdit);
                edt.setText(obj3.getString("location"));

                name = obj3.getString("name");
                ldesc = obj3.getString("longDescription");
                sdesc = obj3.getString("shortDescription");

                txtt.setText("longDescription");

                TextView ab = findViewById(R.id.editPlacetypeTop);
                ab.setText("edit" + name);



            }
        } catch (JSONException e) {
            Log.e("JSONERROR", "unexpected JSON exception", e);
        }
    }


    public void submit()
    {
        EditText edt = findViewById(R.id.placeTypeNameEdit);
        String name = edt.getText().toString();

        edt = findViewById(R.id.placeTypeSDestEdit);
        String shortdesc    = edt.getText().toString();

        edt = findViewById(R.id.placeTypeLDestEdit);
        String longdesc     = edt.getText().toString();

        edt = findViewById(R.id.placeTypeLocationEdit);
        String location     = edt.getText().toString();

        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("name", name);
            jsonBody.put("shortDescription", shortdesc);
            jsonBody.put("longDescription", longdesc);
            jsonBody.put("placeType", JSONSaved.getPlacetype());
            jsonBody.put("photo", getUploadedImage());
            jsonBody.put("location", location);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = JSONSaved.getUrl()+"/places/edit/"+JSONSaved.getPlaceid() +"/"+ JSONSaved.getUser();

        final String requestBody = jsonBody.toString();

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest sr = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log_setrJson(response);
                        refresh();
                        Log.e("HttpClient", "success! response: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        /*
                        AlertDialog.Builder builder = new AlertDialog.Builder(Log_screen.this);
                        builder.setCancelable(true);
                        builder.setTitle("Wrong name or password");
                        builder.setMessage("name or password you typed is wrong");
                        passField.setText("");

                        Log.e("HttpClient", "error: " + error.toString());
                        builder.show();
                         */
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

    public void refresh()
    {
        Toast.makeText(getApplicationContext(),"Successfully edited",Toast.LENGTH_SHORT).show();
        finish();

        Intent intent = new Intent(this, Places_screen.class);
        startActivity(intent);
    }
}