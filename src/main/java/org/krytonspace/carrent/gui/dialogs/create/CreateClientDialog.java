package org.krytonspace.carrent.gui.dialogs.create;

import org.krytonspace.carrent.controllers.model.ClientController;
import org.krytonspace.carrent.controllers.exceptions.InvalidDataException;
import org.krytonspace.carrent.gui.tablemodels.BaseTableModel;
import org.krytonspace.carrent.models.ClientModel;
import org.krytonspace.carrent.utils.ModelFieldPair;

import javax.swing.*;

/**
 * Dialog asking information to create a new client.
 */
public class CreateClientDialog extends CreateModelDialog {

    private static final String[] FIELDS;

    static {
        FIELDS = BaseTableModel.getModelFieldsInfo(ClientModel.class) // Retrieve fields info
                .stream()
                .map(ModelFieldPair::getName) // Get a stream of display names
                .filter(name -> !name.equalsIgnoreCase("id")) // Skip the ID field
                .toArray(String[]::new); // Store them into an array
    }

    private final ClientController controller;

    /**
     * Constructor.
     * @param owner The owner of this dialog
     * @param controller The controller to manage clients
     */
    public CreateClientDialog(JFrame owner, ClientController controller) {
        super(owner, "Client", FIELDS);
        this.controller = controller;
    }

    @Override
    protected void processValues(String[] values) throws InvalidDataException {
        // Checks will be performed by the controller.
        controller.addClient(
                values[0], // Last name
                values[1], // First name
                values[2], // Birth date
                values[3], // Address
                values[4], // Postal code
                values[5], // City
                values[6], // Licenses
                values[7], // Email address
                values[8]  // Phone number
        );
        clearFields();
    }
}
