package com.zarra.lyricsfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    EditText edtArtist, edtSong;
    Button btnSearch;
    TextView txtLyrics;

    //https://api.lyrics.ovh/v1/Rihanna/Diamonds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtArtist=findViewById(R.id.edtArtistName);
        edtSong=findViewById(R.id.edtSongName);
        btnSearch=findViewById(R.id.btnSearch);
        txtLyrics=findViewById(R.id.txtLyrics);


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url="https://api.lyrics.ovh/v1/"+edtArtist.getText().toString()+"/"+edtSong.getText().toString();
                url.replace(" ","%20");

                RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
                JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            txtLyrics.setText(response.getString("lyrics"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                requestQueue.add(jsonObjectRequest);
            }
        });


    }
}
