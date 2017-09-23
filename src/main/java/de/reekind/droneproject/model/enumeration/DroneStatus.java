package de.reekind.droneproject.model.enumeration;

public enum DroneStatus {
    Bereit(0), InAuslieferung(1);
    int id;

    DroneStatus(int i) {
        id = i;
    }

    public static DroneStatus GetValue(int _id) {
        DroneStatus[] As = DroneStatus.values();
        for (DroneStatus A : As) {
            if (A.Compare(_id))
                return A;
        }
        return DroneStatus.Bereit;
    }

    public int GetID() {
        return id;
    }

    public boolean IsEmpty() {
        return this.equals(DroneStatus.Bereit);
    }

    public boolean Compare(int i) {
        return id == i;
    }
}
