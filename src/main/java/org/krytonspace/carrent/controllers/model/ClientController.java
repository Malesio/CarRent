package org.krytonspace.carrent.controllers.model;

import org.krytonspace.carrent.controllers.exceptions.InvalidDataException;
import org.krytonspace.carrent.models.ClientModel;

import java.util.Date;
import java.util.stream.Stream;

/**
 * Interface denoting the actions of a client controller.
 */
public interface ClientController {
    /**
     * Check for correct input values, commit the addition and notify all listeners.
     * @param lastName The client's last name
     * @param firstName The client's first name
     * @param birthDate The client's birth date
     * @param address The client's address
     * @param postalCode The client's postal code
     * @param city The client's city
     * @param licenses The client's licenses
     * @param emailAddress The client's email address
     * @param phoneNumber The client's phone number
     * @throws InvalidDataException when an input value is invalid
     */
    void addClient(
            String lastName,
            String firstName,
            String birthDate,
            String address,
            String postalCode,
            String city,
            String licenses,
            String emailAddress,
            String phoneNumber
    ) throws InvalidDataException;

    /**
     * Check for a correct input value, edit the client's last name and notify all listeners.
     * @param model The model to edit
     * @param value The client's new last name
     * @throws InvalidDataException when an input value is invalid
     */
    void editClientLastName(ClientModel model, String value) throws InvalidDataException;

    /**
     * Check for a correct input value, edit the client's first name and notify all listeners.
     * @param model The model to edit
     * @param value The client's new first name
     * @throws InvalidDataException when an input value is invalid
     */
    void editClientFirstName(ClientModel model, String value) throws InvalidDataException;

    /**
     * Check for a correct input value, edit the client's birth date and notify all listeners.
     * @param model The model to edit
     * @param value The client's new birth date
     * @throws InvalidDataException when an input value is invalid
     */
    void editClientBirthDate(ClientModel model, Date value) throws InvalidDataException;

    /**
     * Check for a correct input value, edit the client's address and notify all listeners.
     * @param model The model to edit
     * @param value The client's new address
     * @throws InvalidDataException when an input value is invalid
     */
    void editClientAddress(ClientModel model, String value) throws InvalidDataException;

    /**
     * Check for a correct input value, edit the client's postal code and notify all listeners.
     * @param model The model to edit
     * @param value The client's new postal code
     * @throws InvalidDataException when an input value is invalid
     */
    void editClientPostalCode(ClientModel model, String value) throws InvalidDataException;

    /**
     * Check for a correct input value, edit the client's city and notify all listeners.
     * @param model The model to edit
     * @param value The client's new city
     * @throws InvalidDataException when an input value is invalid
     */
    void editClientCity(ClientModel model, String value) throws InvalidDataException;

    /**
     * Check for a correct input value, edit the client's licenses and notify all listeners.
     * @param model The model to edit
     * @param value The client's new licenses
     * @throws InvalidDataException when an input value is invalid
     */
    void editClientLicenses(ClientModel model, String value) throws InvalidDataException;

    /**
     * Check for a correct input value, edit the client's email address and notify all listeners.
     * @param model The model to edit
     * @param value The client's new email address
     * @throws InvalidDataException when an input value is invalid
     */
    void editClientEmailAddress(ClientModel model, String value) throws InvalidDataException;

    /**
     * Check for a correct input value, edit the client's phone number and notify all listeners.
     * @param model The model to edit
     * @param value The client's new phone number
     * @throws InvalidDataException when an input value is invalid
     */
    void editClientPhoneNumber(ClientModel model, String value) throws InvalidDataException;

    /**
     * Check for an existing client, notify all listeners and remove it from the database.
     * @param clientId The client to remove
     * @throws InvalidDataException when the client ID is invalid
     */
    void removeClient(String clientId) throws InvalidDataException;

    /**
     * Check if the specified ID exists in the database.
     * @param clientId The client ID to test
     * @return true if a client with this ID is registered, false otherwise
     */
    default boolean clientExists(String clientId) {
        return query().anyMatch(client -> client.getId().equals(clientId));
    }

    /**
     * Return the number of clients that are registered in the database.
     * @return The number of clients registered
     */
    int clientCount();

    /**
     * Begin a new query on the registered clients.
     * This enables the program to use the java Stream API to manipulate data
     * in a functional programming style.
     *
     * @return A stream of clients
     */
    Stream<ClientModel> query();
}
