package org.krytonspace.carrent.controllers.model;

import org.krytonspace.carrent.controllers.exceptions.InvalidDataException;
import org.krytonspace.carrent.models.BikeModel;
import org.krytonspace.carrent.models.CarModel;
import org.krytonspace.carrent.models.PlaneModel;
import org.krytonspace.carrent.models.VehicleModel;

import java.util.stream.Stream;

/**
 * Interface denoting the actions of a vehicle controller.
 */
public interface VehicleController {

    /**
     * Check for correct input values, commit the addition and notify all listeners.
     * @param brand The car brand
     * @param model The car model
     * @param condition The car condition
     * @param rentPrice The car rent price per day
     * @param maxSpeed The car maximal speed
     * @param mileage The car mileage
     * @param power The car power
     * @param seats The car seat count
     * @throws InvalidDataException when an input value is invalid
     */
    void addCar(
            String brand,
            String model,
            String condition,
            String rentPrice,
            String maxSpeed,
            String mileage,
            String power,
            String seats
    ) throws InvalidDataException;

    /**
     * Check for correct input values, commit the addition and notify all listeners.
     * @param brand The bike brand
     * @param model The bike model
     * @param condition The bike condition
     * @param rentPrice The bike rent price per day
     * @param maxSpeed The bike maximal speed
     * @param mileage The bike mileage
     * @param power The bike power
     * @throws InvalidDataException when an input value is invalid
     */
    void addBike(
            String brand,
            String model,
            String condition,
            String rentPrice,
            String maxSpeed,
            String mileage,
            String power
    ) throws InvalidDataException;

    /**
     * Check for correct input values, commit the addition and notify all listeners.
     * @param brand The plane brand
     * @param model The plane model
     * @param condition The plane condition
     * @param rentPrice The plane rent price per day
     * @param maxSpeed The plane maximal speed
     * @param hoursFlown The number of hours the plane has flown
     * @param engines The plane engine count
     * @throws InvalidDataException when an input value is invalid
     */
    void addPlane(
            String brand,
            String model,
            String condition,
            String rentPrice,
            String maxSpeed,
            String hoursFlown,
            String engines
    ) throws InvalidDataException;

    /**
     * Checks for a correct input value, edit the vehicle brand and notify all listeners.
     * @param model The model to edit
     * @param value The new vehicle brand
     * @throws InvalidDataException when an input value is invalid
     */
    void editVehicleBrand(VehicleModel model, String value) throws InvalidDataException;

    /**
     * Checks for a correct input value, edit the vehicle model and notify all listeners.
     * @param model The model to edit
     * @param value The new vehicle model
     * @throws InvalidDataException when an input value is invalid
     */
    void editVehicleModel(VehicleModel model, String value) throws InvalidDataException;

    /**
     * Checks for a correct input value, edit the vehicle condition and notify all listeners.
     * @param model The model to edit
     * @param value The new vehicle condition
     * @throws InvalidDataException when an input value is invalid
     */
    void editVehicleCondition(VehicleModel model, VehicleModel.Condition value) throws InvalidDataException;

    /**
     * Checks for a correct input value, edit the vehicle rent price and notify all listeners.
     * @param model The model to edit
     * @param value The new vehicle rent price
     * @throws InvalidDataException when an input value is invalid
     */
    void editVehicleRentPrice(VehicleModel model, int value) throws InvalidDataException;

    /**
     * Checks for a correct input value, edit the vehicle maximal speed and notify all listeners.
     * @param model The model to edit
     * @param value The new vehicle maximal speed
     * @throws InvalidDataException when an input value is invalid
     */
    void editVehicleMaxSpeed(VehicleModel model, int value) throws InvalidDataException;

    /**
     * Checks for a correct input value, edit the car mileage and notify all listeners.
     * @param model The model to edit
     * @param value The new car mileage
     * @throws InvalidDataException when an input value is invalid
     */
    void editCarMileage(CarModel model, int value) throws InvalidDataException;

    /**
     * Checks for a correct input value, edit the car power and notify all listeners.
     * @param model The model to edit
     * @param value The new car power
     * @throws InvalidDataException when an input value is invalid
     */
    void editCarPower(CarModel model, int value) throws InvalidDataException;

    /**
     * Checks for a correct input value, edit the car seat count and notify all listeners.
     * @param model The model to edit
     * @param value The new car seat count
     * @throws InvalidDataException when an input value is invalid
     */
    void editCarSeatCount(CarModel model, int value) throws InvalidDataException;

    /**
     * Checks for a correct input value, edit the bike mileage and notify all listeners.
     * @param model The model to edit
     * @param value The new bike mileage
     * @throws InvalidDataException when an input value is invalid
     */
    void editBikeMileage(BikeModel model, int value) throws InvalidDataException;

    /**
     * Checks for a correct input value, edit the bike power and notify all listeners.
     * @param model The model to edit
     * @param value The new bike power
     * @throws InvalidDataException when an input value is invalid
     */
    void editBikePower(BikeModel model, int value) throws InvalidDataException;

    /**
     * Checks for a correct input value, edit the number of hours flown by the plane and notify all listeners.
     * @param model The model to edit
     * @param value The new number of hours that the plane has flown
     * @throws InvalidDataException when an input value is invalid
     */
    void editPlaneHoursFlown(PlaneModel model, int value) throws InvalidDataException;

    /**
     * Checks for a correct input value, edit the plane engine count and notify all listeners.
     * @param model The model to edit
     * @param value The new plane engine count
     * @throws InvalidDataException when an input value is invalid
     */
    void editPlaneEngineCount(PlaneModel model, int value) throws InvalidDataException;

    /**
     * Check for an existing vehicle, notify all listeners and remove the vehicle from the database.
     * @param vehicleId The vehicle to remove
     * @throws InvalidDataException when the vehicle ID is invalid
     */
    void removeVehicle(String vehicleId) throws InvalidDataException;

    /**
     * Check if the specified vehicle ID denotes a registered vehicle.
     * @param vehicleId The vehicle ID to check
     * @return true if the vehicle exists, false otherwise
     */
    default boolean vehicleExists(String vehicleId) {
        return query().anyMatch(vehicle -> vehicle.getId().equals(vehicleId));
    }

    /**
     * Get the number of registered vehicles in the database.
     * @return The vehicle count
     */
    int vehicleCount();

    /**
     * Begin a new query on the registered vehicles.
     * This enables the program to use the java Stream API to manipulate data
     * in a functional programming style.
     *
     * @return A stream of vehicles
     */
    Stream<VehicleModel> query();
}
