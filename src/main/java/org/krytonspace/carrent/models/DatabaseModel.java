package org.krytonspace.carrent.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JsonRootName(value = "database")
public class DatabaseModel {
    private List<ClientModel> clients = new ArrayList<>();
    private List<VehicleModel> vehicles = new ArrayList<>();
    private List<ContractModel> contracts = new ArrayList<>();

    @JsonGetter("clients")
    public List<ClientModel> getRegisteredClients() {
        return Collections.unmodifiableList(clients);
    }

    @JsonGetter("vehicles")
    public List<VehicleModel> getRegisteredVehicles() {
        return Collections.unmodifiableList(vehicles);
    }

    @JsonGetter("contracts")
    public List<ContractModel> getRegisteredContracts() {
        return Collections.unmodifiableList(contracts);
    }

    public void registerClient(ClientModel model) {
        clients.add(model);
    }

    public void registerContract(ContractModel model) {
        contracts.add(model);
    }

    public void registerVehicle(VehicleModel model) {
        vehicles.add(model);
    }

    public void unregisterClient(String clientId) {
        clients.removeIf(client -> client.getId().equals(clientId));
    }

    public void unregisterContract(String contractId) {
        contracts.removeIf(contract -> contract.getId().equals(contractId));
    }

    public void unregisterVehicle(String vehicleId) {
        vehicles.removeIf(vehicle -> vehicle.getId().equals(vehicleId));
    }

    private void setClients(List<ClientModel> clients) {
        this.clients = clients;
    }

    private void setVehicles(List<VehicleModel> vehicles) {
        this.vehicles = vehicles;
    }

    private void setContracts(List<ContractModel> contracts) {
        this.contracts = contracts;
    }
}
