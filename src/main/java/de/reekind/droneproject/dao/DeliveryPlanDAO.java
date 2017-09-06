package de.reekind.droneproject.dao;

import de.reekind.droneproject.DbUtil;
import de.reekind.droneproject.model.routeplanning.DeliveryPlan;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class DeliveryPlanDAO {

    private static final Map<Integer, DeliveryPlan> droneMap = new HashMap<>();
    private static Connection dbConnection;

    static {
        dbConnection = DbUtil.getConnection();
        init();
    }

    private static void init() {
    }
}
