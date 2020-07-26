package org.krytonspace.carrent.gui.dialogs.create;

import org.krytonspace.carrent.controllers.exceptions.InvalidDataException;
import org.krytonspace.carrent.controllers.model.VehicleController;
import org.krytonspace.carrent.gui.tablemodels.BaseTableModel;
import org.krytonspace.carrent.models.CarModel;
import org.krytonspace.carrent.utils.ModelFieldPair;

import javax.swing.*;

/**
 * Dialog asking information to create a new bike.
 */
public class CreateCarDialog extends CreateModelDialog {

    private static final String[] FIELDS;

    static {
        FIELDS = BaseTableModel.getModelFieldsInfo(CarModel.class) // Retrieve fields info
                .stream()
                .map(ModelFieldPair::getName) // Get a stream of display names
                .filter(field -> !field.equalsIgnoreCase("id")) // Skip the ID field
                .toArray(String[]::new); // Store them into an array
    }

    private final VehicleController controller;

    /**
     * Constructor.
     * @param owner The owner of this dialog
     * @param controller The controller to manage vehicles
     */
    public CreateCarDialog(JFrame owner, VehicleController controller) {
        super(owner, "Car", FIELDS);
        this.controller = controller;
    }

    @Override
    protected void processValues(String[] values) throws InvalidDataException {
        // Checks will be performed by the controller.
        controller.addCar(
                values[0], // Brand
                values[1], // Model
                values[2], // Condition
                values[3], // Rent price
                values[4], // Max speed
                values[5], // Mileage
                values[6], // Power
                values[7]  // Seat count
        );
        clearFields();
    }
}
