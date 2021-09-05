package com.example.helloworldapp;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Request {

        public static final String SITE_API = "https://developerslife.ru/";
        public static final String RANDOM = "random";
        public static final String PARAM = "json";
        public static final String Random = "/latest";

    public static URL generateRandomRequest(String type) {
        Uri builtUri = Uri.parse(SITE_API + RANDOM)
                .buildUpon()
                .appendQueryParameter(PARAM, "true")
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromURL(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {


            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else return null;
        }finally {
            urlConnection.disconnect();
        }

    }
}
