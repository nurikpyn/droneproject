package de.reekind.droneproject.model;

public enum OrderStatus {
    EINGEGANGEN(0), BEREIT(1), INAUSLIEFERUNG(2), FERTIG(3);
    int id;
    OrderStatus(int i){id = i;}
    public int GetID(){return id;}
    public boolean IsEmpty(){return this.equals(OrderStatus.EINGEGANGEN);}
    public boolean Compare(int i){return id == i;}

    public static OrderStatus GetValue(int _id)
    {
        OrderStatus[] As = OrderStatus.values();
        for(int i = 0; i < As.length; i++)
        {
            if(As[i].Compare(_id))
                return As[i];
        }
        return OrderStatus.EINGEGANGEN;
    }
}
