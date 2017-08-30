package de.reekind.droneproject.model.enumeration;

public enum DroneStatus {
    Bereit(0), InAuslieferung(1);
    int id;
    DroneStatus(int i){id = i;}
    public int GetID(){return id;}
    public boolean IsEmpty(){return this.equals(DroneStatus.Bereit);}
    public boolean Compare(int i){return id == i;}

    public static DroneStatus GetValue(int _id)
    {
        DroneStatus[] As = DroneStatus.values();
        for(int i = 0; i < As.length; i++)
        {
            if(As[i].Compare(_id))
                return As[i];
        }
        return DroneStatus.Bereit;
    }
}
