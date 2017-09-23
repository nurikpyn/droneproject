package de.reekind.droneproject.model.enumeration;

public enum OrderStatus {
    Eingegangen(0), InVorbereitung(1), Bereit(2), InAuslieferung(3), Ausgeliefert(4);
    int id;

    OrderStatus(int i) {
        id = i;
    }

    public static OrderStatus GetValue(int _id) {
        OrderStatus[] As = OrderStatus.values();
        for (int i = 0; i < As.length; i++) {
            if (As[i].Compare(_id))
                return As[i];
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
