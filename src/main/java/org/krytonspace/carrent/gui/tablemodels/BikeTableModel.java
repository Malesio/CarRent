package org.krytonspace.carrent.gui.tablemodels;

import org.krytonspace.carrent.controllers.exceptions.InvalidDataException;
import org.krytonspace.carrent.controllers.model.VehicleController;
import org.krytonspace.carrent.gui.Window;
import org.krytonspace.carrent.models.BikeModel;
import org.krytonspace.carrent.models.Model;
import org.krytonspace.carrent.models.VehicleModel;
import org.krytonspace.carrent.utils.ModelFieldPair;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Table model handling display for bikes.
 */
public class BikeTableModel extends VehicleTableModel {

    private static final List<ModelFieldPair> COLUMNS = getModelFieldsInfo(BikeModel.class);

    /**
     * A filter to apply to the cache.
     */
    private Predicate<BikeModel> filter;
    /**
     * Bike cache.
     */
    private List<BikeModel> cache;

    /**
     * Constructor.
     * @param controller The controller managing vehicles.
     */
    public BikeTableModel(VehicleController controller) {
        super(controller);
    }

    /**
     * Apply a filter to this table model. This will trigger a cache update.
     * @param filter The filter to apply.
     */
    public void applyFilter(Predicate<BikeModel> filter) {
        this.filter = filter;
        updateCache();
    }

    @Override
    public void updateCache() {
        cache = controller
                .query() // Start a new query
                .filter(vehicle -> vehicle instanceof BikeModel) // Only retrieve bikes
                .map(vehicle -> (BikeModel) vehicle) // map a Vehicle stream to a Bike stream
                .filter(filter) // Apply custom filter
                .sorted(Comparator.comparingInt(Model::getInternalId)) // Sort by internal ID
                .collect(Collectors.toList());
        // Trigger visual update.
        fireTableDataChanged();
    }

    @Override
    public void resetFilter() {
        applyFilter(bike -> true);
    }

    @Override
    public int getRowCount() {
        return cache.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.size();
    }

    @Override
    public String getColumnName(int column) {
        return COLUMNS.get(column).getName();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return COLUMNS.get(columnIndex).getType();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        // Values are stored in the cache.
        if (!cache.isEmpty()) {
            BikeModel bike = cache.get(rowIndex);

            switch (columnIndex) {
                case 0:
                    return bike.getId();
                case 1:
                    return bike.getBrand();
                case 2:
                    return bike.getModel();
                case 3:
                    return bike.getCondition();
                case 4:
                    return bike.getRentPricePerDay();
                case 5:
                    return bike.getMaxSpeed();
                case 6:
                    return bike.getMileage();
                case 7:
                    return bike.getPower();
            }
        }

        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (!cache.isEmpty()) {
            BikeModel bike = cache.get(rowIndex);

            // The controller must be used to edit models.

            try {
                switch (columnIndex) {
                    case 1:
                        controller.editVehicleBrand(bike, (String) aValue);
                        break;
                    case 2:
                        controller.editVehicleModel(bike, (String) aValue);
                        break;
                    case 3:
                        controller.editVehicleCondition(bike, (VehicleModel.Condition) aValue);
                        break;
                    case 4:
                        controller.editVehicleRentPrice(bike, (Integer) aValue);
                        break;
                    case 5:
                        controller.editVehicleMaxSpeed(bike, (Integer) aValue);
                        break;
                    case 6:
                        controller.editBikeMileage(bike, (Integer) aValue);
                        break;
                    case 7:
                        controller.editBikePower(bike, (Integer) aValue);
                        break;
                }
            } catch (InvalidDataException e) {
                Window.notifyException(e);
            }
        }
    }
}
