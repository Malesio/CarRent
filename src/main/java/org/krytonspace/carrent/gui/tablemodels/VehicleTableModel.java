package org.krytonspace.carrent.gui.tablemodels;

import org.krytonspace.carrent.controllers.event.ModelEvent;
import org.krytonspace.carrent.controllers.event.ModelListener;
import org.krytonspace.carrent.controllers.model.VehicleController;
import org.krytonspace.carrent.controllers.model.VehicleModelController;

/**
 * Base table model for vehicles.
 */
public abstract class VehicleTableModel extends BaseTableModel {

    protected final VehicleController controller;

    public VehicleTableModel(VehicleController controller) {
        this.controller = controller;
        // Update cache when the controller apply changes.
        ((VehicleModelController) controller).addModelListener(new ModelListener() {
            @Override
            public void onModelAdded(ModelEvent e) {
                updateCache();
            }

            @Override
            public void onModelRemoving(ModelEvent e) {
                updateCache();
            }

            @Override
            public void onModelEdited(ModelEvent e) {
                // Ignored: cache contents reflects internal model data.
            }
        });
        // No filter by default.
        resetFilter();
    }
}
