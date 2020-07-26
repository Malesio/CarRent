package org.krytonspace.carrent.models;

import org.krytonspace.carrent.utils.ModelField;

/**
 * Model class holding data for a bike.
 */
public class BikeModel extends VehicleModel {
    @ModelField(name = "Mileage")
    private int mileage;
    @ModelField(name = "Power")
    private int power;

    public int getMileage() {
        return mileage;
    }

    public int getPower() {
        return power;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public void setPower(int power) {
        this.power = power;
    }
}
