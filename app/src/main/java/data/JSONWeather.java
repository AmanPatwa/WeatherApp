package data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.CurrentCondition;
import model.Place;
import model.Snow;
import model.Weather;
import model.Wind;
import util.Utils;

/**
 * Created by dell on 14-08-2018.
 */

public class JSONWeather {
    public static Weather getWeather(String data){
        Weather weather=new Weather();
        try {
            JSONObject jsonObject=new JSONObject(data);
            Place place=new Place();
            JSONObject coorobj= Utils.getObject("coord",jsonObject);
            place.setLat(Utils.getFloat("lat",coorobj));
            place.setLon(Utils.getFloat("lon",coorobj));

            JSONObject sysobj=Utils.getObject("sys",jsonObject);
            place.setCountry(Utils.getString("country",sysobj));
            place.setLastupdate(Utils.getInt("dt",jsonObject));
            place.setSunrise(Utils.getInt("sunrise",sysobj));
            place.setSunset(Utils.getInt("sunset",sysobj));
            place.setCity(Utils.getString("name",jsonObject));
            weather.place=place;

            JSONArray jsonArray=jsonObject.getJSONArray("weather");
            JSONObject jsonweather=jsonArray.getJSONObject(0);
            CurrentCondition currentCondition=new CurrentCondition();
            currentCondition.setWeatherId(Utils.getInt("id",jsonweather));
            currentCondition.setDescription(Utils.getString("description",jsonweather));
            currentCondition.setCondition(Utils.getString("main",jsonweather));
            currentCondition.setIcon(Utils.getString("icon",jsonweather));
            weather.currentCondition=currentCondition;

            JSONObject mainobj=Utils.getObject("main",jsonObject);
            weather.currentCondition.setHumidity(Utils.getInt("humidity",mainobj));
            weather.currentCondition.setPressure(Utils.getInt("pressure",mainobj));
            weather.currentCondition.setMintemp(Utils.getFloat("temp_min",mainobj));
            weather.currentCondition.setMaxtemp(Utils.getFloat("temp_max",mainobj));
            weather.currentCondition.setTemperature(Utils.getDouble("temp",mainobj));

            JSONObject windobj=Utils.getObject("wind",jsonObject);
            Wind wind=new Wind();
            wind.setSpeed(Utils.getFloat("speed",windobj));
            wind.setDeg(Utils.getFloat("deg",windobj));
            weather.wind=wind;

            JSONObject cloudobj=Utils.getObject("clouds",jsonObject);
            weather.clouds.setClouds(Utils.getInt("all",cloudobj));

            return weather;



        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
