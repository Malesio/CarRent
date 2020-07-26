package org.krytonspace.carrent.controllers.model;

import org.krytonspace.carrent.controllers.exceptions.InvalidDataException;
import org.krytonspace.carrent.controllers.utils.Requirements;
import org.krytonspace.carrent.models.ContractModel;
import org.krytonspace.carrent.models.DatabaseModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

public class ContractModelController extends BaseModelController implements ContractController {

    public ContractModelController(DatabaseModel model) {
        super(model);
    }

    @Override
    public void addContract(String clientId, String vehicleId, String dateBegin,
                            String dateEnd, String plannedMileage, String plannedPrice)
            throws InvalidDataException {
        assertValidClient(clientId);
        assertValidVehicle(vehicleId);

        Date begin = Requirements.validateDate(dateBegin);
        Date end = Requirements.validateDate(dateEnd);
        int validPlannedMileage = Requirements.validatePositiveNumber(plannedMileage);
        int validPlannedPrice = Requirements.validatePositiveNumber(plannedPrice);

        ContractModel newContract = new ContractModel();
        newContract.setClientId(clientId);
        newContract.setVehicleId(vehicleId);
        newContract.setBegin(begin);
        newContract.setEnd(end);
        newContract.setPlannedMileage(validPlannedMileage);
        newContract.setPlannedPrice(validPlannedPrice);

        generateId(newContract);

        model.registerContract(newContract);

        fireModelAdded(newContract);
    }

    @Override
    public void editContractDateBegin(ContractModel model, Date value) throws InvalidDataException {
        model.setBegin(value);

        fireModelEdited(model);
    }

    @Override
    public void editContractDateEnd(ContractModel model, Date value) throws InvalidDataException {
        model.setEnd(value);

        fireModelEdited(model);
    }

    @Override
    public void editContractPlannedMileage(ContractModel model, int value) throws InvalidDataException {
        Requirements.positive(value);
        model.setPlannedMileage(value);

        fireModelEdited(model);
    }

    @Override
    public void editContractPlannedPrice(ContractModel model, int value) throws InvalidDataException {
        Requirements.positive(value);
        model.setPlannedPrice(value);

        fireModelEdited(model);
    }

    @Override
    public void removeContract(String contractId) throws InvalidDataException {
        assertExistence(contractId);

        query().filter(contract -> contract.getId().equals(contractId))
                .findFirst()
                .ifPresent(this::fireModelRemoving);

        model.unregisterContract(contractId);
    }

    @Override
    public int contractCount() {
        return model.getRegisteredContracts().size();
    }

    @Override
    public Stream<ContractModel> query() {
        return model.getRegisteredContracts().stream();
    }

    private void assertExistence(String contractId) throws InvalidDataException {
        if (!contractExists(contractId)) {
            throw new InvalidDataException("Contract '" + contractId + "' could not be found in database.");
        }
    }

    private void assertValidClient(String clientId) throws InvalidDataException {
        if (model.getRegisteredClients().stream().noneMatch(client -> client.getId().equals(clientId))) {
            throw new InvalidDataException("Client '" + clientId + "' does not exist.");
        }
    }

    private void assertValidVehicle(String vehicleId) throws InvalidDataException {
        if (model.getRegisteredVehicles().stream().noneMatch(vehicle -> vehicle.getId().equals(vehicleId))) {
            throw new InvalidDataException("Client '" + vehicleId + "' does not exist.");
        }
    }

    private void generateId(ContractModel model) {
        model.setId("CTR-" +
                new SimpleDateFormat("yyMMdd").format(new Date()) +
                "-" +
                model.getInternalId());
    }
}
