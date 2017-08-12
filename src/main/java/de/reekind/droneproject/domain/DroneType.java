package de.reekind.droneproject.domain;

public class DroneType {
    private int droneTypeId;
    private float maxWeight;
    private int maxPackageCount;
    private float maxRange;
    private float maxSpeed;

    public DroneType(int _droneTypeId, float _maxWeight, int _maxPackageCount, float _maxRange, float _maxSpeed) {
        this.droneTypeId = _droneTypeId;
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
}
