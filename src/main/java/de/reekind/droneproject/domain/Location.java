package de.reekind.droneproject.domain;

/**
 * Created by timbe on 07.08.2017.
 */
public class Location {
    public double longitude;
    public double latitude;

    public Location(double _latitude, double _longitude) {
        longitude = _longitude;
        latitude = _latitude;
    }
}
