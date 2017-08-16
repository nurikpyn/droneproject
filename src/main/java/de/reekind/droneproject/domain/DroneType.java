package de.reekind.droneproject.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleType;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleTypeImpl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonFormat(shape= JsonFormat.Shape.ARRAY)
public class DroneType {
    private int droneTypeId;
    private float maxWeight;
    private int maxPackageCount;
    private float maxRange;
    private float maxSpeed;
    private VehicleType vehicleType;
    public static final int WEIGHT_INDEX = 0;
    public static final int RANGE_INDEX = 1;

    public DroneType(int _droneTypeId) {
        this.droneTypeId = _droneTypeId;
    }

    public DroneType(int _droneTypeId, float _maxWeight, int _maxPackageCount, float _maxRange) {
        this.droneTypeId = _droneTypeId;
        this.maxWeight = _maxWeight;
        this.maxPackageCount = _maxPackageCount;
        this.maxRange = _maxRange;

        // TODO: create vehicleTypes here, edit dimensions

        //TODO float to INT possible?
        VehicleTypeImpl.Builder vehicleTypeBuilder = VehicleTypeImpl.Builder.newInstance(Integer.toString(this.droneTypeId));
        vehicleTypeBuilder.addCapacityDimension(WEIGHT_INDEX, (int) this.maxWeight);
        vehicleTypeBuilder.addCapacityDimension(RANGE_INDEX, (int) this.maxRange);
        this.vehicleType = vehicleTypeBuilder.build();
    }

    public int getDroneTypeId() {
        return droneTypeId;
    }

    public void setDroneTypeId(int droneTypeId) {
        this.droneTypeId = droneTypeId;
    }

    public float getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(float maxWeight) {
        this.maxWeight = maxWeight;
    }

    public int getMaxPackageCount() {
        return maxPackageCount;
    }

    public void setMaxPackageCount(int maxPackageCount) {
        this.maxPackageCount = maxPackageCount;
    }

    public float getMaxRange() {
        return maxRange;
    }

    public void setMaxRange(float maxRange) {
        this.maxRange = maxRange;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.toString().equals(Integer.toString(this.droneTypeId))) {
            return true;
        } else {
            return super.equals(obj);
        }
    }
    @Override
    public String toString() {
        return Integer.toString(this.getDroneTypeId());
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

}
