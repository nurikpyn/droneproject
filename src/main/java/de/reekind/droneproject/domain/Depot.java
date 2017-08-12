package de.reekind.droneproject.domain;

/**
 * Created by timbe on 07.08.2017.
 */
public class Depot {
    private int ID;
    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}