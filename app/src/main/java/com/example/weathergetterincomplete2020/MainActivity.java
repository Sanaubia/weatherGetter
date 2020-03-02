package com.example.weathergetterincomplete2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, WeatherEngine.WeatherDataAvailableInterface {

    WeatherEngine engine = new WeatherEngine(this);
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(this);

        Loadshared();
        updateView();
    }


    public void onClick(View v) {
        EditText editor = (EditText) findViewById(R.id.editText);
        engine.getWeatherData(editor.getText().toString());
    }

    protected void updateUI()
    {
        EditText edit = (EditText) findViewById(R.id.editText);
        TextView temperatureTextView = (TextView) findViewById(R.id.textView);
        String formatted = String.format(getString(R.string.temp), engine.getTemperature());

        temperatureTextView.setText(formatted);
        ImageView img = (ImageView) findViewById(R.id.imageView);
        Picasso.with(this).load("http://openweathermap.org/img/w/" + engine.getIconId() + ".png").into(img);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(TEXT, edit.getText().toString());

        editor.apply();

        Toast.makeText(this, "data saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void weatherDataAvailable() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateUI();
            }
        });
    }

    public void Loadshared(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        text = sharedPreferences.getString(TEXT, "");

    }

    public void updateView(){
        EditText editor = (EditText) findViewById(R.id.editText);
        editor.setText(text);
    }
}

