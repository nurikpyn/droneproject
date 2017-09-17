package de.reekind.droneproject.model.enumeration;

public enum RouteStatus {
    Geplant(0), InAuslieferung(1), Beendet(2);
    int id;

    RouteStatus(int i) {
        id = i;
    }

    public static RouteStatus GetValue(int _id) {
        RouteStatus[] As = RouteStatus.values();
        for (int i = 0; i < As.length; i++) {
            if (As[i].Compare(_id))
                return As[i];
        }
        return RouteStatus.Geplant;
    }

    public int GetID() {
        return id;
    }

    public boolean IsEmpty() {
        return this.equals(RouteStatus.Geplant);
    }

    public boolean Compare(int i) {
        return id == i;
    }
}
