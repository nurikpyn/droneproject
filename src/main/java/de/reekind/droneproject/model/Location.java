package de.reekind.droneproject.model;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

import javax.xml.bind.annotation.XmlType;

@XmlType(name="ownLocation")
public class Location {
    public double latitude;
    public double longitude;
    public String name;

    public Location() {}
    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public Location(double latitude, double longitude, String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }
    public Location(String adress) {
        //Map Adress
        try {
            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey("AIzaSyBgKJti8sqVUbrvQY2xWVKgXX4WF_Y4npE")
                    .build();
            GeocodingResult[] results =  GeocodingApi.geocode(context,adress).await();
            this.latitude = results[0].geometry.location.lat;
            this.longitude = results[0].geometry.location.lng;
            this.name = adress;
        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }



    public com.graphhopper.jsprit.core.problem.Location toJspritLocation() {
        return com.graphhopper.jsprit.core.problem.Location.newInstance(this.latitude,this.longitude);
    }
}
