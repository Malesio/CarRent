package org.krytonspace.carrent.gui.dialogs.create;

import org.krytonspace.carrent.controllers.exceptions.InvalidDataException;
import org.krytonspace.carrent.controllers.model.VehicleController;
import org.krytonspace.carrent.gui.tablemodels.BaseTableModel;
import org.krytonspace.carrent.models.PlaneModel;
import org.krytonspace.carrent.utils.ModelFieldPair;

import javax.swing.*;

public class CreatePlaneDialog extends CreateModelDialog {
    private static final String[] FIELDS;

    static {
        FIELDS = BaseTableModel.getModelFieldsInfo(PlaneModel.class)
                .stream()
                .map(ModelFieldPair::getName)
                .filter(field -> !field.equalsIgnoreCase("id"))
                .toArray(String[]::new);
    }

    private final VehicleController controller;

    public CreatePlaneDialog(JFrame owner, VehicleController controller) {
        super(owner, "Plane", FIELDS);
        this.controller = controller;
    }

    @Override
    protected void processValues(String[] values) throws InvalidDataException {
        controller.addPlane(
                values[0],
                values[1],
                values[2],
                values[3],
                values[4],
                values[5],
                values[6]
        );
        clearFields();
    }
}
