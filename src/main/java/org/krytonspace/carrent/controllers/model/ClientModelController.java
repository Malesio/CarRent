package org.krytonspace.carrent.controllers.model;

import org.krytonspace.carrent.controllers.exceptions.InvalidDataException;
import org.krytonspace.carrent.controllers.utils.Requirements;
import org.krytonspace.carrent.models.ClientModel;
import org.krytonspace.carrent.models.DatabaseModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

/**
 * An actual implementation of a client controller class.
 */
public class ClientModelController extends BaseModelController implements ClientController {

    /**
     * Constructor.
     * @param model A reference to the working database
     */
    public ClientModelController(DatabaseModel model) {
        super(model);
    }

    @Override
    public void addClient(String lastName, String firstName, String birthDate, String address, String postalCode,
                          String city, String licenses, String emailAddress, String phoneNumber)
            throws InvalidDataException {
        // Checks
        Requirements.nonEmpty(lastName, firstName, address, city, licenses);
        Requirements.validPostalCode(postalCode);
        Requirements.mailAddress(emailAddress);
        Requirements.phoneNumber(phoneNumber);

        Date birth = Requirements.validateDate(birthDate);

        // Model creation

        ClientModel newModel = new ClientModel();

        newModel.setLastName(lastName.trim());
        newModel.setFirstName(firstName.trim());
        newModel.setAddress(address.trim());
        newModel.setBirthDate(birth);
        newModel.setPostalCode(postalCode.trim());
        newModel.setCity(city.trim());
        newModel.setLicenses(licenses.trim());
        newModel.setEmailAddress(emailAddress.trim());
        newModel.setPhoneNumber(phoneNumber.trim());

        generateId(newModel);

        // Actual registration
        model.registerClient(newModel);

        // Notify listeners
        fireModelAdded(newModel);
    }

    @Override
    public void editClientLastName(ClientModel model, String value) throws InvalidDataException {
        Requirements.nonEmpty(value);
        model.setLastName(value);

        fireModelEdited(model);
    }

    @Override
    public void editClientFirstName(ClientModel model, String value) throws InvalidDataException {
        Requirements.nonEmpty(value);
        model.setFirstName(value);

        fireModelEdited(model);
    }

    @Override
    public void editClientBirthDate(ClientModel model, Date value) throws InvalidDataException {
        Requirements.nonNull(value);
        model.setBirthDate(value);

        fireModelEdited(model);
    }

    @Override
    public void editClientAddress(ClientModel model, String value) throws InvalidDataException {
        Requirements.nonEmpty(value);
        model.setAddress(value);

        fireModelEdited(model);
    }

    @Override
    public void editClientPostalCode(ClientModel model, String value) throws InvalidDataException {
        Requirements.validPostalCode(value);
        model.setPostalCode(value);

        fireModelEdited(model);
    }

    @Override
    public void editClientCity(ClientModel model, String value) throws InvalidDataException {
        Requirements.nonEmpty(value);
        model.setCity(value);

        fireModelEdited(model);
    }

    @Override
    public void editClientLicenses(ClientModel model, String value) throws InvalidDataException {
        Requirements.nonEmpty(value);
        model.setLicenses(value);

        fireModelEdited(model);
    }

    @Override
    public void editClientEmailAddress(ClientModel model, String value) throws InvalidDataException {
        Requirements.mailAddress(value);
        model.setEmailAddress(value);

        fireModelEdited(model);
    }

    @Override
    public void editClientPhoneNumber(ClientModel model, String value) throws InvalidDataException {
        Requirements.phoneNumber(value);
        model.setPhoneNumber(value);

        fireModelEdited(model);
    }

    @Override
    public void removeClient(String clientId) throws InvalidDataException {
        assertExistence(clientId);

        // Functional style: call listeners.
        query().filter(client -> client.getId().equals(clientId))
                .findFirst() // IDs are unique: this holds the correct Client
                .ifPresent(this::fireModelRemoving);

        model.unregisterClient(clientId);
    }

    @Override
    public int clientCount() {
        return model.getRegisteredClients().size();
    }

    @Override
    public Stream<ClientModel> query() {
        return model.getRegisteredClients().stream();
    }

    private void assertExistence(String clientId) throws InvalidDataException {
        if (!clientExists(clientId)) {
            throw new InvalidDataException("Client '" + clientId + "' does not exist in database.");
        }
    }

    /**
     * Generate a unique string ID for a Client.
     * @param model The model
     * @return
     */
    private void generateId(ClientModel model) {
        String nameTrunc = model.getLastName();
        if (nameTrunc.length() > 3) {
            nameTrunc = nameTrunc.substring(0, 3);
        }

        // Generate a unique string ID for this model
        model.setId("CLI-" +
                new SimpleDateFormat("yyMMdd").format(new Date()) +
                "-" +
                nameTrunc.toUpperCase() +
                "-" +
                model.getInternalId());
    }
}
