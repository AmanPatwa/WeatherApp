package patwa.aman.com.weatherapp;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;

import data.CityPrefer;
import data.JSONWeather;
import data.WeatherHttpClient;
import model.Weather;
import util.Utils;

public class MainActivity extends AppCompatActivity {
    TextView cityname,wind,cloud,humidity,pressure,sunrise,sunset,lastupdated,temperature;
    ImageView iconview;

    Weather weather=new Weather();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        cityname=(TextView)findViewById(R.id.cityname);
        wind=(TextView)findViewById(R.id.windtext);
        cloud=(TextView)findViewById(R.id.cloudtext);
        humidity=(TextView)findViewById(R.id.humidtext);
        pressure=(TextView)findViewById(R.id.pressuretext);
        sunrise=(TextView)findViewById(R.id.Sunrisetext);
        sunset=(TextView)findViewById(R.id.Sunsettext);
        lastupdated=(TextView)findViewById(R.id.lasttext);
        temperature=(TextView)findViewById(R.id.temptext);

        CityPrefer cityPrefer=new CityPrefer(MainActivity.this);

        finalWeatherData("Mumbai");



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Loading Weather", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void finalWeatherData(String city){
        WeatherTask weatherTask=new WeatherTask();
        weatherTask.execute(new String[]{city+"&units=metric"});

    }

    private class WeatherTask extends AsyncTask<String,Void,Weather> {

        @Override
        protected Weather doInBackground(String... strings) {
            String data = ((new WeatherHttpClient()).getWeatherData(strings[0]));
            weather = JSONWeather.getWeather(data);

           // weather.iconData=weather.currentCondition.getIcon();

            //Log.v("Data:", weather.place.getCity());

          //  new ImageTask().execute(weather.iconData);

            return weather;
        }


            protected void onPostExecute(Weather weather) {


                DateFormat df= DateFormat.getTimeInstance();
                String sunrisedate=df.format(new Time(weather.place.getSunrise()));
                String sunsetdate=df.format(new Time(weather.place.getSunset()));
                String update=df.format(new Time(weather.place.getLastupdate()));

                DecimalFormat decimalFormat=new DecimalFormat("#.#");
                String tempFormat=decimalFormat.format(weather.currentCondition.getTemperature());

            cityname.setText(weather.place.getCity());
            temperature.setText(tempFormat+"°C");
            humidity.setText("Humidity: "+weather.currentCondition.getHumidity()+"%");
            pressure.setText("Pressure: "+weather.currentCondition.getPressure()+"hpa");
            wind.setText("Wind: "+weather.wind.getSpeed()+"m/s");
            sunrise.setText("Sunrise: "+sunrisedate);
            sunset.setText("Sunset: "+sunsetdate);
            lastupdated.setText("Last Updated: "+update);
            cloud.setText("Description: "+weather.currentCondition.getCondition());

            super.onPostExecute(weather);
        }
    }

    /*private class ImageTask extends AsyncTask<String,Void,Bitmap>
    {

        @Override
        protected Bitmap doInBackground(String... strings) {
            return downloadImage(strings[0]);

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            iconview.setImageBitmap(bitmap);
        }

       /** private Bitmap downloadImage(String code){
            final DefaultHttpClient client=new DefaultHttpClient();
           // final HttpGet getRequest=new HttpGet(Utils.icon_url+code+".png"+Utils.api_key);

            final HttpGet getRequest=new HttpGet("https://www.google.co.in/search?q=weather&rlz=1C1CHBF_enIN781IN784&tbm=isch&source=lnt&tbs=isz:ex,iszw:100,iszh:100#imgrc=MO1oL04wDB4b7M:");

            HttpResponse response= null;
            try {
                response = client.execute(getRequest);
            } catch (IOException e) {
                e.printStackTrace();
            }
            final int statusCode=response.getStatusLine().getStatusCode();

            if(statusCode != HttpStatus.SC_OK){
                Log.e("Download","Error"+statusCode);
                return null;
            }
            final HttpEntity entity=response.getEntity();
            if(entity!=null){
                InputStream inputStream=null;
                try {
                    inputStream=entity.getContent();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                final Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
            System.out.println("null");
            return null;
        }

    }
        */


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void doInput(){
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Change City");

        final EditText cityInput=new EditText(MainActivity.this);
        cityInput.setInputType(InputType.TYPE_CLASS_TEXT);
        cityInput.setHint("Eg:Delhi");
        builder.setView(cityInput);
        builder.setCancelable(true)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CityPrefer cityPrefer=new CityPrefer(MainActivity.this);
                        cityPrefer.setCity(cityInput.getText().toString());

                        String newcity=cityPrefer.getCity();

                        finalWeatherData(newcity);
                    }
                });
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.change_cityId) {
            doInput();
        }

        return super.onOptionsItemSelected(item);
    }
}
