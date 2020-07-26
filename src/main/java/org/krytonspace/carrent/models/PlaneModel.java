package org.krytonspace.carrent.models;

import org.krytonspace.carrent.utils.ModelField;

/**
 * Model class holding data for a plane.
 */
public class PlaneModel extends VehicleModel {
    @ModelField(name = "Hours flown")
    private int hoursFlown;

    @ModelField(name = "Engine count")
    private int engineCount;

    public int getHoursFlown() {
        return hoursFlown;
    }

    public int getEngineCount() {
        return engineCount;
    }

    public void setHoursFlown(int hoursFlown) {
        this.hoursFlown = hoursFlown;
    }

    public void setEngineCount(int engines) {
        this.engineCount = engines;
    }
}
