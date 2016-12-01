package webservice.credentials;

import java.io.Serializable;

/**
 * Created by MSI on 2016-12-01.
 */
public class EventCredentials implements Serializable {

    String cityName;
    String latitude;
    String longitude;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public EventCredentials() {}

}
