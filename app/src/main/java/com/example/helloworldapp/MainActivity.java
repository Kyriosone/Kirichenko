package com.example.helloworldapp;



import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;


import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private TextView result;
    private ImageView image;
    private String currentGIfUrl=null;
    private String currentDescription=null;
    private Button next;
    private Button previous;
    private Map<Integer, String[]> map = new HashMap<Integer, String[]>();
    private String[] mem = new String[2];
    private int count=0;

    class GifQuery extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            String response = null;
            try {
                response = Request.getResponseFromURL(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            String gifDescription=null;
            String gifURL=null;
            try {
                JSONObject jsonResponse = new JSONObject(response);
                gifDescription = jsonResponse.getString("description");
                gifURL = jsonResponse.getString("gifURL");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            currentDescription = gifDescription;
            assert gifURL != null;
            currentGIfUrl = gifURL.replace("http","https");
            String[] temp = new String[2];
            mem[0] = currentDescription;
            temp[0] = currentDescription;
            temp[1] = currentGIfUrl;
            map.put(count, temp);

            Glide.with(getApplicationContext()).asGif().load(currentGIfUrl).placeholder(R.drawable.temp_gif).error(new ColorDrawable(Color.BLACK)).into(image);
            result.setText(currentDescription);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = findViewById(R.id.textView);
        image = findViewById(R.id.leftGifWindow);
        next = findViewById(R.id.Next);
        previous = findViewById(R.id.Previous);

        URL url = Request.generateRandomRequest("temp");
        new GifQuery().execute(url);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(getApplicationContext()).asGif().load(R.drawable.temp_gif).error(new ColorDrawable(Color.BLACK)).into(image);
                URL url = Request.generateRandomRequest("");
                new GifQuery().execute(url);

                count++;
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count > 0) {
                    count--;
                    mem = map.get(count);
                    Glide.with(getApplicationContext()).asGif().load(mem[1]).placeholder(R.drawable.temp_gif).error(new ColorDrawable(Color.BLACK)).into(image);
                    assert mem != null;
                    result.setText(mem[0]);
                }

            }
        });

        result.setText(currentDescription);

    }
}