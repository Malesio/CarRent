package org.krytonspace.carrent.gui.tablemodels;

import org.krytonspace.carrent.controllers.exceptions.InvalidDataException;
import org.krytonspace.carrent.controllers.model.VehicleController;
import org.krytonspace.carrent.gui.Window;
import org.krytonspace.carrent.models.CarModel;
import org.krytonspace.carrent.models.Model;
import org.krytonspace.carrent.models.VehicleModel;
import org.krytonspace.carrent.utils.ModelFieldPair;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Table model handling display for cars.
 */
public class CarTableModel extends VehicleTableModel {

    private static final List<ModelFieldPair> COLUMNS = getModelFieldsInfo(CarModel.class);

    /**
     * A filter to apply to the cache.
     */
    private Predicate<CarModel> filter;
    /**
     * Car cache.
     */
    private List<CarModel> cache;

    /**
     * Constructor.
     * @param controller The controller managing vehicles.
     */
    public CarTableModel(VehicleController controller) {
        super(controller);
    }

    /**
     * Apply a filter to this table model. This will trigger a cache update.
     * @param filter The filter to apply.
     */
    public void applyFilter(Predicate<CarModel> filter) {
        this.filter = filter;
        updateCache();
    }

    @Override
    public void updateCache() {
        cache = controller
                .query()
                .filter(vehicle -> vehicle instanceof CarModel) // Only retrieve cars
                .map(vehicle -> (CarModel) vehicle) // map a Vehicle stream to a Car stream
                .filter(filter) // Apply custom filter
                .sorted(Comparator.comparingInt(Model::getInternalId)) // Sort by internal ID
                .collect(Collectors.toList());
        // Trigger visual update.
        fireTableDataChanged();
    }

    public void resetFilter() {
        applyFilter(car -> true);
    }

    @Override
    public int getRowCount() {
        return cache.size();
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
    public int getColumnCount() {
        return COLUMNS.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        // Values are stored in the cache.
        if (!cache.isEmpty()) {
            CarModel car = cache.get(rowIndex);

            switch (columnIndex) {
                case 0:
                    return car.getId();
                case 1:
                    return car.getBrand();
                case 2:
                    return car.getModel();
                case 3:
                    return car.getCondition();
                case 4:
                    return car.getRentPricePerDay();
                case 5:
                    return car.getMaxSpeed();
                case 6:
                    return car.getMileage();
                case 7:
                    return car.getPower();
                case 8:
                    return car.getSeatCount();
            }
        }

        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (!cache.isEmpty()) {
            CarModel car = cache.get(rowIndex);

            // The controller must be used to edit models.

            try {
                switch (columnIndex) {
                    case 1:
                        controller.editVehicleBrand(car, (String) aValue);
                        break;
                    case 2:
                        controller.editVehicleModel(car, (String) aValue);
                        break;
                    case 3:
                        controller.editVehicleCondition(car, (VehicleModel.Condition) aValue);
                        break;
                    case 4:
                        controller.editVehicleRentPrice(car, (Integer) aValue);
                        break;
                    case 5:
                        controller.editVehicleMaxSpeed(car, (Integer) aValue);
                        break;
                    case 6:
                        controller.editCarMileage(car, (Integer) aValue);
                        break;
                    case 7:
                        controller.editCarPower(car, (Integer) aValue);
                        break;
                    case 8:
                        controller.editCarSeatCount(car, (Integer) aValue);
                        break;
                }
            } catch (InvalidDataException e) {
                Window.notifyException(e);
            }
        }
    }
}
