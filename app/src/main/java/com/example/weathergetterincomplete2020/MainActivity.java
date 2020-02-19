package com.example.weathergetterincomplete2020;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, WeatherEngine.WeatherDataAvailableInterface {

    WeatherEngine engine = new WeatherEngine(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(this);
    }


    public void onClick(View v) {
        EditText editor = (EditText) findViewById(R.id.editText);
        engine.getWeatherData(editor.getText().toString());
    }

    protected void updateUI()
    {
        TextView temperatureTextView = (TextView) findViewById(R.id.textView);
        String formatted = String.format(getString(R.string.temp), engine.getTemperature());

        temperatureTextView.setText(formatted);
        ImageView img = (ImageView) findViewById(R.id.imageView);
        Picasso.with(this).load("http://openweathermap.org/img/w/" + engine.getIconId() + ".png").into(img);
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
}