package de.reekind.droneproject.domain;

/**
 * Created by timbe on 07.08.2017.
 */
public class Depot {
    private int depotID;
    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getDepotID() {
        return depotID;
    }

    public void setDepotID(int ID) {
        this.depotID = ID;
    }
}