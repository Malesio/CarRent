package org.krytonspace.carrent.gui.tablemodels;

import org.krytonspace.carrent.controllers.exceptions.InvalidDataException;
import org.krytonspace.carrent.controllers.model.VehicleController;
import org.krytonspace.carrent.gui.Window;
import org.krytonspace.carrent.models.Model;
import org.krytonspace.carrent.models.PlaneModel;
import org.krytonspace.carrent.models.VehicleModel;
import org.krytonspace.carrent.utils.ModelFieldPair;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PlaneTableModel extends VehicleTableModel {

    private static final List<ModelFieldPair> COLUMNS = getModelFieldsInfo(PlaneModel.class);

    /**
     * A filter to apply to the cache.
     */
    private Predicate<PlaneModel> filter;
    /**
     * Plane cache.
     */
    private List<PlaneModel> cache;

    /**
     * Constructor.
     * @param controller The controller managing planes.
     */
    public PlaneTableModel(VehicleController controller) {
        super(controller);
    }

    /**
     * Apply a filter to this table model. This will trigger a cache update.
     * @param filter The filter to apply.
     */
    public void applyFilter(Predicate<PlaneModel> filter) {
        this.filter = filter;
        updateCache();
    }

    @Override
    public void updateCache() {
        cache = controller
                .query()
                .filter(vehicle -> vehicle instanceof PlaneModel) // Only retrieve planes
                .map(vehicle -> (PlaneModel) vehicle) // map Vehicle stream to Plane stream
                .filter(filter) // Apply custom filter
                .sorted(Comparator.comparingInt(Model::getInternalId)) // Sort by internal ID
                .collect(Collectors.toList());
        // Trigger visual update.
        fireTableDataChanged();
    }

    @Override
    public void resetFilter() {
        applyFilter(plane -> true);
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
        if (!cache.isEmpty()) {
            PlaneModel plane = cache.get(rowIndex);

            switch (columnIndex) {
                case 0:
                    return plane.getId();
                case 1:
                    return plane.getBrand();
                case 2:
                    return plane.getModel();
                case 3:
                    return plane.getCondition();
                case 4:
                    return plane.getRentPricePerDay();
                case 5:
                    return plane.getMaxSpeed();
                case 6:
                    return plane.getHoursFlown();
                case 7:
                    return plane.getEngineCount();
            }
        }

        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (!cache.isEmpty()) {
            PlaneModel plane = cache.get(rowIndex);

            try {
                switch (columnIndex) {
                    case 1:
                        controller.editVehicleBrand(plane, (String) aValue);
                        break;
                    case 2:
                        controller.editVehicleModel(plane, (String) aValue);
                        break;
                    case 3:
                        controller.editVehicleCondition(plane, (VehicleModel.Condition) aValue);
                        break;
                    case 4:
                        controller.editVehicleRentPrice(plane, (Integer) aValue);
                        break;
                    case 5:
                        controller.editVehicleMaxSpeed(plane, (Integer) aValue);
                        break;
                    case 6:
                        controller.editPlaneHoursFlown(plane, (Integer) aValue);
                        break;
                    case 7:
                        controller.editPlaneEngineCount(plane, (Integer) aValue);
                        break;
                }
            } catch (InvalidDataException e) {
                Window.notifyException(e);
            }
        }
    }
}
