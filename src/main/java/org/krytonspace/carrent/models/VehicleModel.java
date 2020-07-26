package org.krytonspace.carrent.models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.krytonspace.carrent.utils.ModelField;

/**
 * Model class holding data for a vehicle.
 *
 * On serialization, Jackson will add a "type" property
 * to the Vehicle object in order to know, when deserializing,
 * which vehicle type it was.
 *
 * If a new type of vehicle is added, it must be registered here.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CarModel.class, name = "car"),
        @JsonSubTypes.Type(value = BikeModel.class, name = "bike"),
        @JsonSubTypes.Type(value = PlaneModel.class, name = "plane")
})
public abstract class VehicleModel extends Model {
    @ModelField(name = "Brand")
    private String brand;

    @ModelField(name = "Model")
    private String model;

    @ModelField(name = "Condition")
    private Condition condition;

    @ModelField(name = "Rent price per day")
    private int rentPricePerDay;

    @ModelField(name = "Max speed")
    private int maxSpeed;

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public Condition getCondition() {
        return condition;
    }

    public int getRentPricePerDay() {
        return rentPricePerDay;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public void setRentPricePerDay(int rentPricePerDay) {
        this.rentPricePerDay = rentPricePerDay;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public enum Condition {
        New,
        VeryGood,
        Good,
        Passable,
        Bad,
        Unusable
    }
}
