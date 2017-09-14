package de.reekind.droneproject.model;

import com.graphhopper.jsprit.core.problem.vehicle.VehicleType;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleTypeImpl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DroneType {
    private int droneTypeId;
    private String droneTypeName;
    private int maxWeight;
    private int maxPackageCount;
    private float maxRange;
    private float maxSpeed;
    static int WEIGHT_INDEX = 0; //static final
    static int RANGE_INDEX = 1;
    static int PACKAGE_INDEX = 2;

    public DroneType(){}

    public DroneType(int _droneTypeId) {
        this.droneTypeId = _droneTypeId;
    }

    public DroneType(int _droneTypeId, String _droneTypeName, int _maxWeight, int _maxPackageCount, float _maxRange) {
        this.droneTypeId = _droneTypeId;
        this.droneTypeName = _droneTypeName;
        this.maxWeight = _maxWeight;
        this.maxPackageCount = _maxPackageCount;
        this.maxRange = _maxRange;
    }
    public DroneType(int _droneTypeId, String _droneTypeName, int _maxWeight, int _maxPackageCount, float _maxRange, int _maxSpeed) {
        this.droneTypeId = _droneTypeId;
        this.droneTypeName = _droneTypeName;
        this.maxWeight = _maxWeight;
        this.maxPackageCount = _maxPackageCount;
        this.maxRange = _maxRange;
        this.maxSpeed = _maxSpeed;
    }

    public int getDroneTypeId() {
        return droneTypeId;
    }

    public void setDroneTypeId(int droneTypeId) {
        this.droneTypeId = droneTypeId;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(int maxWeight) {
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
        vehicleTypeBuilder.addCapacityDimension(WEIGHT_INDEX, this.maxWeight);
        vehicleTypeBuilder.addCapacityDimension(RANGE_INDEX, (int) this.maxRange);
        vehicleTypeBuilder.addCapacityDimension(PACKAGE_INDEX, this.maxPackageCount);

        vehicleTypeBuilder.setCostPerDistance(1000);
        vehicleTypeBuilder.setFixedCost(0);
        vehicleTypeBuilder.setMaxVelocity(kmhToMs(maxSpeed));
        return vehicleTypeBuilder.build();
    }

    private float kmhToMs(float speedInKmh)
    {
        return (speedInKmh * 3600 / 1000);
    }

    public String getDroneTypeName() {
        return droneTypeName;
    }

    public void setDroneTypeName(String droneTypeName) {
        this.droneTypeName = droneTypeName;
    }
}
