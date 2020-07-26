package org.krytonspace.carrent.controllers.model;

import org.krytonspace.carrent.controllers.exceptions.InvalidDataException;
import org.krytonspace.carrent.models.ContractModel;

import java.util.Date;
import java.util.stream.Stream;

/**
 * Interface denoting the actions of a contract controller.
 */
public interface ContractController {
    /**
     * Check for correct input values, commit the addition and notify all listeners.
     * @param clientId The client ID
     * @param vehicleId The vehicle ID
     * @param dateBegin The date on which the contract will start
     * @param dateEnd The date on which the contract will end
     * @param plannedMileage The number of kilometers planned
     * @param plannedPrice The price planned
     * @throws InvalidDataException when an input value is invalid
     */
    void addContract(
            String clientId,
            String vehicleId,
            String dateBegin,
            String dateEnd,
            String plannedMileage,
            String plannedPrice
    ) throws InvalidDataException;

    /**
     * Check for a correct input value, edit the begin date of the contract and notify all listeners.
     * @param model The model to edit
     * @param value The new begin date of the contract
     * @throws InvalidDataException when an input value is invalid
     */
    void editContractDateBegin(ContractModel model, Date value) throws InvalidDataException;

    /**
     * Check for a correct input value, edit the end date of the contract and notify all listeners.
     * @param model The model to edit
     * @param value The new end date of the contract
     * @throws InvalidDataException when an input value is invalid
     */
    void editContractDateEnd(ContractModel model, Date value) throws InvalidDataException;

    /**
     * Check for a correct input value, edit the planned number of kilometers in the contract and notify all listeners.
     * @param model The model to edit
     * @param value The new planned number of kilometers in the contract
     * @throws InvalidDataException when an input value is invalid
     */
    void editContractPlannedMileage(ContractModel model, int value) throws InvalidDataException;

    /**
     * Check for a correct input value, edit the planned price of the contract and notify all listeners.
     * @param model The model to edit
     * @param value The new planned price of the contract
     * @throws InvalidDataException when an input value is invalid
     */
    void editContractPlannedPrice(ContractModel model, int value) throws InvalidDataException;

    /**
     * Check for an existing contract, notify all listeners and remove the contract.
     * @param contractId The contract to remove
     * @throws InvalidDataException when an input value is invalid
     */
    void removeContract(String contractId) throws InvalidDataException;

    /**
     * Get the number of registered contracts in the database.
     * @return The number of contracts
     */
    int contractCount();

    /**
     * Begin a new query on the registered contracts.
     * This enables the program to use the java Stream API to manipulate data
     * in a functional programming style.
     *
     * @return A stream of contracts
     */
    Stream<ContractModel> query();

    /**
     * Check if the specified contract ID denotes a registered contract.
     * @param contractId The contract to check
     * @return true if the contract is registered, false otherwise
     */
    default boolean contractExists(String contractId) {
        return query().anyMatch(contract -> contract.getId().equals(contractId));
    }

    /**
     * Check if the specified client ID denotes a client that holds a registered contract.
     * @param clientId The client to check
     * @return true if the specified client has a contract, false otherwise
     */
    default boolean hasContract(String clientId) {
        return query().anyMatch(contract -> contract.getClientId().equals(clientId));
    }

    /**
     * Check if the specified vehicle ID denotes a vehicle that is referenced in a contract.
     * @param vehicleId The vehicle to check
     * @return true if the specified vehicle is rented, false otherwise
     */
    default boolean isRented(String vehicleId) {
        return query().anyMatch(contract -> contract.getVehicleId().equals(vehicleId));
    }
}
