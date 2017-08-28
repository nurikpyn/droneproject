package de.reekind.droneproject.dao;

import de.reekind.droneproject.model.*;

import java.sql.*;
import java.util.*;

public class DroneDAO {

    private static final Map<Integer, Drone> droneMap = new HashMap<>();

    static {
        init();
    }

    private static void init() {

        //TODO Lade bestehende Dronen aus der Datenbank
        Depot depotSalcombe = new Depot(1, "Salcombe", new Location(5.1, 3.4));
        Drone drone1 = new Drone(1, "Capt. America", new DroneType(), 1, depotSalcombe);
        Drone drone2 = new Drone(2, "Thor", new DroneType(), 1, depotSalcombe);
        Drone drone3 = new Drone(3, "Hulk", new DroneType(), 1, depotSalcombe);
        Drone drone4 = new Drone(4, "Spider Man", new DroneType(), 1, depotSalcombe);
        Drone drone5 = new Drone(5, "Warriour", new DroneType(), 1, depotSalcombe);

        droneMap.put(drone1.getDroneId(), drone1);
        droneMap.put(drone2.getDroneId(), drone2);
        droneMap.put(drone3.getDroneId(), drone3);
        droneMap.put(drone4.getDroneId(), drone4);
        droneMap.put(drone5.getDroneId(), drone5);
    }

    public static Drone getDrone(Integer droneId) {
        return droneMap.get(droneId);
    }

    public static Drone addDrone(Drone drone) {
        droneMap.put(drone.getDroneId(), drone);
        //TODO Schreibe neue Drohne in DB
        return drone;
    }

    public static Drone updateDrone(Drone drone) {
        droneMap.put(drone.getDroneId(), drone);
        //TODO Schreibe  Drohne in DB
        return drone;
    }

    public static void deleteDrone(Integer droneId) {
        //TODO Lösche Drohne in DB
        droneMap.remove(droneId);
    }

    public static List<Drone> getAllDrones() {
        Collection<Drone> c = droneMap.values();
        List<Drone> list = new ArrayList<>();
        list.addAll(c);
        return list;
    }

    List<Drone> list;

}