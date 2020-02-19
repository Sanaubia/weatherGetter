package com.example.weathergetterincomplete2020;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HTTPGetThread extends Thread {

    public String url = "";
    public HTTPGetThread(String url, WeatherEngine weatherEngine) {
        this.url = url;

    }

    public interface OnRequestDoneInterface {
        void onRequestDone(String data);
        void onError(String error);
    }

    private OnRequestDoneInterface listener;

    public OnRequestDoneInterface getListener(){
        return listener;
    }

    public void setListener(OnRequestDoneInterface newListener){
        this.listener = newListener;

    }



    @Override
    public void run() {
        try {
            String osoite = url;
            URL url = new URL(osoite);
            URLConnection urlConnection = url.openConnection();
            if (urlConnection instanceof HttpURLConnection) {
                HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;
                InputStream stream = urlConnection.getInputStream();
                String result = fromStream(stream);

                if (listener != null) {
                    listener.onRequestDone(result);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (listener != null) {
                listener.onError(e.toString());
            }
        }
    }

    public static String fromStream(InputStream in) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder out = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
            out.append(newLine);
        }
        return out.toString();
    }



}
