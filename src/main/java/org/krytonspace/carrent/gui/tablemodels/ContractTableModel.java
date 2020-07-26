package org.krytonspace.carrent.gui.tablemodels;

import org.krytonspace.carrent.controllers.event.ModelEvent;
import org.krytonspace.carrent.controllers.event.ModelListener;
import org.krytonspace.carrent.controllers.exceptions.InvalidDataException;
import org.krytonspace.carrent.controllers.model.ContractController;
import org.krytonspace.carrent.controllers.model.ContractModelController;
import org.krytonspace.carrent.gui.Window;
import org.krytonspace.carrent.models.ContractModel;
import org.krytonspace.carrent.models.Model;
import org.krytonspace.carrent.utils.ModelFieldPair;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Table model handling display for contracts.
 */
public class ContractTableModel extends BaseTableModel {

    private static final List<ModelFieldPair> COLUMNS = getModelFieldsInfo(ContractModel.class);

    /**
     * The controller managing contracts.
     */
    private final ContractController controller;
    /**
     * A filter to apply to the cache.
     */
    private Predicate<ContractModel> filter;
    /**
     * Contract cache.
     */
    private List<ContractModel> cache;

    /**
     * Constructor.
     * @param controller The controller managing contracts.
     */
    public ContractTableModel(ContractController controller) {
        this.controller = controller;
        // Update cache when the controller apply changes.
        ((ContractModelController) controller).addModelListener(new ModelListener() {
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

    /**
     * Apply a filter to this table model. This will trigger a cache update.
     * @param filter The filter to apply.
     */
    public void applyFilter(Predicate<ContractModel> filter) {
        this.filter = filter;
        updateCache();
    }

    @Override
    public void updateCache() {
        cache = controller
                .query()
                .filter(filter) // Apply custom filter
                .sorted(Comparator.comparingInt(Model::getInternalId)) // Sort by internal ID
                .collect(Collectors.toList());
        // Trigger visual update.
        fireTableDataChanged();
    }

    @Override
    public void resetFilter() {
        applyFilter(contract -> true);
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
    public String getColumnName(int columnIndex) {
        return COLUMNS.get(columnIndex).getName();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return COLUMNS.get(columnIndex).getType();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        // Values are stored in the cache.
        if (!cache.isEmpty()) {
            ContractModel contract = cache.get(rowIndex);

            switch (columnIndex) {
                case 0:
                    return contract.getId();
                case 1:
                    return contract.getClientId();
                case 2:
                    return contract.getVehicleId();
                case 3:
                    return contract.getBeginDate();
                case 4:
                    return contract.getEndDate();
                case 5:
                    return contract.getPlannedMileage();
                case 6:
                    return contract.getPlannedPrice();
            }
        }

        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (!cache.isEmpty()) {
            ContractModel contract = cache.get(rowIndex);

            // The controller must be used to edit models.

            try {
                switch (columnIndex) {
                    case 3:
                        controller.editContractDateBegin(contract, (Date) aValue);
                        break;
                    case 4:
                        controller.editContractDateEnd(contract, (Date) aValue);
                        break;
                    case 5:
                        controller.editContractPlannedMileage(contract, (Integer) aValue);
                        break;
                    case 6:
                        controller.editContractPlannedPrice(contract, (Integer) aValue);
                        break;
                }
            } catch (InvalidDataException e) {
                Window.notifyException(e);
            }
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // Prohibit client and vehicle ID change. A contract is bound to a client and a vehicle.
        if (columnIndex == 1 || columnIndex == 2) {
            return false;
        }

        return super.isCellEditable(rowIndex, columnIndex);
    }
}
