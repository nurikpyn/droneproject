package de.reekind.droneproject.model.enumeration;

public enum OrderStatus {
    Eingegangen(0), InVorbereitung(1), Bereit(2), InAuslieferung(3), Ausgeliefert(4), Fehler(5), Geplant(6);
    int id;

    OrderStatus(int i) {
        id = i;
    }

    public static OrderStatus GetValue(int _id) {
        OrderStatus[] As = OrderStatus.values();
        for (OrderStatus A : As) {
            if (A.Compare(_id))
                return A;
        }
        return OrderStatus.Eingegangen;
    }

    public int GetID() {
        return id;
    }

    public boolean IsEmpty() {
        return this.equals(OrderStatus.Eingegangen);
    }

    public boolean Compare(int i) {
        return id == i;
    }
}
