package org.krytonspace.carrent.gui.dialogs.create;

import org.krytonspace.carrent.controllers.exceptions.InvalidDataException;
import org.krytonspace.carrent.controllers.model.ContractController;
import org.krytonspace.carrent.controllers.model.VehicleController;
import org.krytonspace.carrent.gui.tablemodels.BaseTableModel;
import org.krytonspace.carrent.models.ContractModel;
import org.krytonspace.carrent.models.VehicleModel;
import org.krytonspace.carrent.utils.ModelFieldPair;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

/**
 * Dialog asking information to create a new contract.
 */
public class CreateContractDialog extends CreateModelDialog {

    private static final String[] FIELDS;

    static {
        FIELDS = BaseTableModel.getModelFieldsInfo(ContractModel.class) // Retrieve fields info
                .stream()
                .map(ModelFieldPair::getName) // Get a stream of display names
                .filter(field -> !field.equalsIgnoreCase("id")) // Skip the ID field
                .filter(field -> !field.equalsIgnoreCase("plannedPrice")) // Skip the planned price field
                .toArray(String[]::new); // Store them into an array
    }

    private final ContractController controller;
    private final VehicleController vehicleController;

    /**
     * Constructor.
     * @param owner The owner of this dialog
     * @param controller The controller to manage contracts
     * @param vehicleController The controller to manage vehicles
     */
    public CreateContractDialog(JFrame owner, ContractController controller, VehicleController vehicleController) {
        super(owner, "Contract", FIELDS);
        this.controller = controller;
        this.vehicleController = vehicleController;
    }

    @Override
    protected void processValues(String[] values) throws InvalidDataException {
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date beg = fmt.parse(values[2]);
            Date end = fmt.parse(values[3]);
            int plannedKilometers = Integer.parseInt(values[4]);

            if (controller.isRented(values[1])) {
                throw new InvalidDataException("This vehicle is already rented.");
            }

            // Retrieve the desired vehicle.
            Optional<VehicleModel> opt = vehicleController.query()
                    .filter(v -> v.getId().equalsIgnoreCase(values[1]))
                    .findFirst();

            if (opt.isPresent()) {
                // Compute the contract duration in days.
                long diffDays = (end.getTime() - beg.getTime()) / (1000 * 60 * 60 * 24);

                if (diffDays <= 0) {
                    throw new InvalidDataException("The contract duration must be greater than 0 days.");
                }

                int rentPricePerDay = opt.get().getRentPricePerDay();

                long plannedPrice = rentPricePerDay * diffDays;

                // Apply price scale.
                if (plannedKilometers > 50 && plannedKilometers <= 100) {
                    plannedPrice += Math.round(plannedKilometers * 0.5);
                } else if (plannedKilometers > 100 && plannedKilometers <= 200) {
                    plannedPrice += Math.round(plannedKilometers * 0.3);
                } else if (plannedKilometers > 200 && plannedKilometers <= 300) {
                    plannedPrice += Math.round(plannedKilometers * 0.2);
                } else if (plannedKilometers > 300) {
                    plannedPrice += Math.round(plannedKilometers * 0.1);
                }

                if (diffDays > 7) {
                    // Apply special sale.
                    int r = JOptionPane.showConfirmDialog(null,
                            "A special 10% sale is available for this " + diffDays + " days long rent.\n" +
                                    "Apply it? (" + Math.round(plannedPrice * 0.9) + "€ instead of " + plannedPrice + "€)",
                            "Sale",
                            JOptionPane.YES_NO_CANCEL_OPTION);
                    if (r == JOptionPane.YES_OPTION) {
                        plannedPrice = Math.round(plannedPrice * 0.9);
                    } else if (r == JOptionPane.CANCEL_OPTION) {
                        return;
                    }
                }

                JOptionPane.showMessageDialog(null,
                        "The contract planned price is set to " + plannedPrice + "€.");

                controller.addContract(values[0], values[1], values[2], values[3], values[4], String.valueOf(plannedPrice));
                clearFields();
            } else {
                // The vehicle does not exist.
                throw new InvalidDataException("The vehicle ID '" + values[1] + "' does not match any registered vehicle.");
            }
        } catch (ParseException e) {
            throw new InvalidDataException("Invalid data provided. A start and end date, and a planned mileage " +
                    "must be specified. (Date formatting is " + fmt.toPattern() + ")");
        }
    }
}
