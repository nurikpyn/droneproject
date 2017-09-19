package de.reekind.droneproject.model;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.graphhopper.jsprit.core.util.Coordinate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "ownLocation")
public class Location {
    private final static Logger _log = LogManager.getLogger();
    private double latitude;
    private double longitude;
    public int locationId;
    private String name;

    public Location() {
    }

    public Location(String adress) {
        getCoordinatesFromAdress(adress);
    }
    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location(String adress, double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = adress;
    }

    public Location(int locationId, String adress, double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = adress;
        this.locationId = locationId;
    }

    public boolean validate() {
        return this.name != null && !this.name.equals("") && this.locationId != 0 && this.latitude != 0 && this.longitude != 0;
    }

    private void getCoordinatesFromAdress(String adress) {
        //Map Adress
        try {
            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey("AIzaSyBgKJti8sqVUbrvQY2xWVKgXX4WF_Y4npE")
                    .build();
            GeocodingResult[] results = GeocodingApi.geocode(context, adress).await();
            this.latitude = results[0].geometry.location.lat;
            this.longitude = results[0].geometry.location.lng;
            this.name = adress;
        } catch (Exception e) {
            _log.error("Fehler beim Abruf der Koordinaten aus Adresse {}",e,adress);
        }
    }
    com.graphhopper.jsprit.core.problem.Location toJspritLocation() {
        return com.graphhopper.jsprit.core.problem.Location.newInstance(this.latitude, this.longitude);
    }


    static double distanceInKm(double lat1, double lon1, double lat2, double lon2) {
        int radius = 6371;

        double lat = Math.toRadians(lat2 - lat1);
        double lon = Math.toRadians(lon2- lon1);

        double a = Math.sin(lat / 2) * Math.sin(lat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(lon / 2) * Math.sin(lon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = radius * c;

        return Math.abs(d);
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        getCoordinatesFromAdress(name);
    }
}
