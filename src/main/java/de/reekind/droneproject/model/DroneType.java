package de.reekind.droneproject.model;

import com.graphhopper.jsprit.core.problem.vehicle.*;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DroneType {
    private int droneTypeId;
    private String droneTypeName;
    private float maxWeight;
    private int maxPackageCount;
    private float maxRange;
    private float maxSpeed;
    public static final int WEIGHT_INDEX = 0;
    public static final int RANGE_INDEX = 1;

    public DroneType(){}

    public DroneType(int _droneTypeId) {
        this.droneTypeId = _droneTypeId;
    }

    public DroneType(int _droneTypeId, float _maxWeight, int _maxPackageCount, float _maxRange) {
        this.droneTypeId = _droneTypeId;
        this.maxWeight = _maxWeight;
        this.maxPackageCount = _maxPackageCount;
        this.maxRange = _maxRange;

        // TODO: create vehicleTypes here, edit dimensions

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
        return obj.toString().equals(Integer.toString(this.droneTypeId)) || super.equals(obj);
    }
    @Override
    public String toString() {
        return Integer.toString(this.getDroneTypeId());
    }

    public VehicleType toJspritVehicleType() {
        VehicleTypeImpl.Builder vehicleTypeBuilder = VehicleTypeImpl.Builder.newInstance(Integer.toString(this.droneTypeId));
        vehicleTypeBuilder.addCapacityDimension(WEIGHT_INDEX, (int) this.maxWeight);
        vehicleTypeBuilder.addCapacityDimension(RANGE_INDEX, (int) this.maxRange);
        return vehicleTypeBuilder.build();
    }


    public String getDroneTypeName() {
        return droneTypeName;
    }

    public void setDroneTypeName(String droneTypeName) {
        this.droneTypeName = droneTypeName;
    }
}
