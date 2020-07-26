package org.krytonspace.carrent.models;

import org.krytonspace.carrent.utils.ModelField;

import java.util.Date;

/**
 * Model class holding data for a contract.
 */
public class ContractModel extends Model {
    @ModelField(name = "Client ID")
    private String clientId;
    @ModelField(name = "Vehicle ID")
    private String vehicleId;
    @ModelField(name = "Start date")
    private Date begin;
    @ModelField(name = "End date")
    private Date end;
    @ModelField(name = "Planned mileage")
    private int plannedMileage;
    @ModelField(name = "Planned price")
    private int plannedPrice;

    public String getClientId() {
        return clientId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public Date getBeginDate() {
        return begin;
    }

    public Date getEndDate() {
        return end;
    }

    public int getPlannedMileage() {
        return plannedMileage;
    }

    public int getPlannedPrice() {
        return plannedPrice;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public void setPlannedMileage(int plannedMileage) {
        this.plannedMileage = plannedMileage;
    }

    public void setPlannedPrice(int plannedPrice) {
        this.plannedPrice = plannedPrice;
    }
}
