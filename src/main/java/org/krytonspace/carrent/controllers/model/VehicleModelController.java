package org.krytonspace.carrent.controllers.model;

import org.krytonspace.carrent.controllers.exceptions.InvalidDataException;
import org.krytonspace.carrent.controllers.utils.Requirements;
import org.krytonspace.carrent.models.*;

import java.util.stream.Stream;

public class VehicleModelController extends BaseModelController implements VehicleController {

    public VehicleModelController(DatabaseModel model) {
        super(model);
    }

    @Override
    public void addCar(String brand, String model, String condition, String rentPrice,
                       String maxSpeed, String mileage, String power, String seats) throws InvalidDataException {
        CarModel newCar = new CarModel();
        populateVehicleData(newCar, brand, model, condition, rentPrice, maxSpeed);

        int validMileage = Requirements.validatePositiveNumber(mileage);
        int validPower = Requirements.validatePositiveNumber(power);
        int validSeats = Requirements.validatePositiveNumber(seats);

        newCar.setMileage(validMileage);
        newCar.setPower(validPower);
        newCar.setSeatCount(validSeats);

        generateId(newCar);

        this.model.registerVehicle(newCar);

        fireModelAdded(newCar);
    }

    @Override
    public void addBike(String brand, String model, String condition, String rentPrice,
                        String maxSpeed, String mileage, String power) throws InvalidDataException {
        BikeModel newBike = new BikeModel();
        populateVehicleData(newBike, brand, model, condition, rentPrice, maxSpeed);

        int validMileage = Requirements.validatePositiveNumber(mileage);
        int validPower = Requirements.validatePositiveNumber(power);

        newBike.setMileage(validMileage);
        newBike.setPower(validPower);

        generateId(newBike);

        this.model.registerVehicle(newBike);

        fireModelAdded(newBike);
    }

    @Override
    public void addPlane(String brand, String model, String condition, String rentPrice,
                         String maxSpeed, String hoursFlown, String engines) throws InvalidDataException {
        PlaneModel newPlane = new PlaneModel();
        populateVehicleData(newPlane, brand, model, condition, rentPrice, maxSpeed);

        int validHoursFlown = Requirements.validatePositiveNumber(hoursFlown);
        int validEngineCount = Requirements.validatePositiveNumber(engines);

        newPlane.setHoursFlown(validHoursFlown);
        newPlane.setEngineCount(validEngineCount);

        generateId(newPlane);

        this.model.registerVehicle(newPlane);

        fireModelAdded(newPlane);
    }

    @Override
    public void editVehicleBrand(VehicleModel model, String value) throws InvalidDataException {
        Requirements.nonEmpty(value);
        model.setBrand(value);
        fireModelEdited(model);
    }

    @Override
    public void editVehicleModel(VehicleModel model, String value) throws InvalidDataException {
        Requirements.nonEmpty(value);
        model.setModel(value);
        fireModelEdited(model);
    }

    @Override
    public void editVehicleCondition(VehicleModel model, VehicleModel.Condition value) throws InvalidDataException {
        Requirements.nonNull(value);
        model.setCondition(value);

        fireModelEdited(model);
    }

    @Override
    public void editVehicleRentPrice(VehicleModel model, int value) throws InvalidDataException {
        Requirements.positive(value);
        model.setRentPricePerDay(value);

        fireModelEdited(model);
    }

    @Override
    public void editVehicleMaxSpeed(VehicleModel model, int value) throws InvalidDataException {
        Requirements.positive(value);
        model.setMaxSpeed(value);

        fireModelEdited(model);
    }

    @Override
    public void editCarMileage(CarModel model, int value) throws InvalidDataException {
        Requirements.positive(value);
        model.setMileage(value);

        fireModelEdited(model);
    }

    @Override
    public void editCarPower(CarModel model, int value) throws InvalidDataException {
        Requirements.positive(value);
        model.setPower(value);
        fireModelEdited(model);
    }

    @Override
    public void editCarSeatCount(CarModel model, int value) throws InvalidDataException {
        Requirements.positive(value);
        model.setSeatCount(value);

        fireModelEdited(model);
    }

    @Override
    public void editBikeMileage(BikeModel model, int value) throws InvalidDataException {
        Requirements.positive(value);
        model.setMileage(value);

        fireModelEdited(model);
    }

    @Override
    public void editBikePower(BikeModel model, int value) throws InvalidDataException {
        Requirements.positive(value);
        model.setPower(value);

        fireModelEdited(model);
    }

    @Override
    public void editPlaneHoursFlown(PlaneModel model, int value) throws InvalidDataException {
        Requirements.positive(value);
        model.setHoursFlown(value);

        fireModelEdited(model);
    }

    @Override
    public void editPlaneEngineCount(PlaneModel model, int value) throws InvalidDataException {
        Requirements.positive(value);
        model.setEngineCount(value);

        fireModelEdited(model);
    }

    @Override
    public void removeVehicle(String vehicleId) throws InvalidDataException {
        assertExistence(vehicleId);

        query().filter(vehicle -> vehicle.getId().equals(vehicleId))
                .findFirst()
                .ifPresent(this::fireModelRemoving);

        model.unregisterVehicle(vehicleId);
    }

    @Override
    public int vehicleCount() {
        return model.getRegisteredVehicles().size();
    }

    @Override
    public Stream<VehicleModel> query() {
        return model.getRegisteredVehicles().stream();
    }


    private void assertExistence(String vehicleId) throws InvalidDataException {
        if (!vehicleExists(vehicleId)) {
            throw new InvalidDataException("Vehicle '" + vehicleId + "' could not be found in database.");
        }
    }

    /**
     * Helper method to fill in common data between all vehicle types.
     * @param newVehicle The vehicle to populate
     * @param brand The new brand
     * @param model The new model
     * @param condition The new condition
     * @param rentPrice The new rent price
     * @param maxSpeed The new maximal speed
     * @throws InvalidDataException when an input value is invalid
     */
    private void populateVehicleData(VehicleModel newVehicle, String brand, String model,
                                     String condition, String rentPrice, String maxSpeed)
            throws InvalidDataException {
        Requirements.nonEmpty(brand, model, condition);
        VehicleModel.Condition validCondition = VehicleModel.Condition.valueOf(condition);
        int validRentPrice = Requirements.validatePositiveNumber(rentPrice);
        int validMaxSpeed = Requirements.validatePositiveNumber(maxSpeed);

        newVehicle.setBrand(brand);
        newVehicle.setModel(model);
        newVehicle.setCondition(validCondition);
        newVehicle.setRentPricePerDay(validRentPrice);
        newVehicle.setMaxSpeed(validMaxSpeed);
    }

    private void generateId(VehicleModel model) {
        model.setId("V-" + model.getBrand().toUpperCase().split(" ")[0] + "-" + model.getInternalId());
    }
}
