package model;

/**
 * Created by dell on 14-08-2018.
 */

public class Temperature {
    private Double temp;
    private float mintemp;
    private float maxtemp;

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public float getMintemp() {
        return mintemp;
    }

    public void setMintemp(float mintemp) {
        this.mintemp = mintemp;
    }

    public float getMaxtemp() {
        return maxtemp;
    }

    public void setMaxtemp(float maxtemp) {
        this.maxtemp = maxtemp;
    }
}
