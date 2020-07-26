package org.krytonspace.carrent.models;

import org.krytonspace.carrent.utils.ModelField;

/**
 * Model class holding data for a car.
 */
public class CarModel extends VehicleModel {
    @ModelField(name = "Mileage")
    private int mileage;
    @ModelField(name = "Power")
    private int power;
    @ModelField(name = "Seat count")
    private int seatCount;

    public int getMileage() {
        return mileage;
    }

    public int getPower() {
        return power;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }
}
