package com.example.weathergetterincomplete2020;

import android.util.Log;

import org.json.JSONObject;



import java.util.ArrayList;
import java.util.Map;

public class WeatherEngine implements HTTPGetThread.OnRequestDoneInterface
{
    // This interface is used to report data back to UI
    public interface WeatherDataAvailableInterface
    {
        // This method is called back in background thread.
        public void weatherDataAvailable();
    }

    static final double KELVIN_CONVERT = 273.15;

    protected String temperature;
    protected String iconId;
   protected WeatherDataAvailableInterface uiCallback;

    // Constructor
    public WeatherEngine(WeatherDataAvailableInterface callbackInterface)
    {
        this.uiCallback = callbackInterface;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getIconId() {
        return iconId;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    public void getWeatherData(String city)
    {
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&APPID=65dbec3aae5e5bf9000c7a956c8b76f6";
       // String url =  "https://api.nasa.gov/planetary/apod?api_key=eDKy0MMzXpklGv8pdPedrQiluatiCgjPOpKax8Yu";

        HTTPGetThread getter = new HTTPGetThread(url, this);
       getter.setListener(this);
       getter.start();
    }



    public void onRequestDone(String data)
    {
        Log.d("LABRA dataa tulee: ", data);
        try
        {
            // No proper error handling here:
            Map<String, Object> parsed = JsonUtils.jsonToMap(new JSONObject(data));
            Map<String, Object> mainElement = (Map) parsed.get("main");
            double temp = (double)mainElement.get("temp");
            double tempInC = temp - KELVIN_CONVERT;
            this.temperature = String.format("%.1f", tempInC);

            ArrayList<Map<String, Object>> array = (ArrayList<Map<String, Object>>)parsed.get("weather");
            Map<String, Object> weatherElement = array.get(0);
            iconId = (String)weatherElement.get("icon");

            uiCallback.weatherDataAvailable();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(String error) {

    }

}
