package org.krytonspace.carrent.gui.tablemodels;

import org.krytonspace.carrent.controllers.event.ModelEvent;
import org.krytonspace.carrent.controllers.exceptions.InvalidDataException;
import org.krytonspace.carrent.controllers.model.ClientController;
import org.krytonspace.carrent.controllers.event.ModelListener;
import org.krytonspace.carrent.controllers.model.ClientModelController;
import org.krytonspace.carrent.gui.Window;
import org.krytonspace.carrent.models.ClientModel;
import org.krytonspace.carrent.models.Model;
import org.krytonspace.carrent.utils.ModelFieldPair;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Table model handling display for clients.
 */
public class ClientTableModel extends BaseTableModel {

    private static final List<ModelFieldPair> COLUMNS = getModelFieldsInfo(ClientModel.class);
    /**
     * The controller managing clients.
     */
    private final ClientController controller;
    /**
     * A filter to apply to the cache.
     */
    private Predicate<ClientModel> filter;
    /**
     * Client cache.
     */
    private List<ClientModel> cache;

    /**
     * Constructor.
     * @param controller The controller managing clients.
     */
    public ClientTableModel(ClientController controller) {
        this.controller = controller;
        // Update cache when the controller apply changes.
        ((ClientModelController) controller).addModelListener(new ModelListener() {
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
    public void applyFilter(Predicate<ClientModel> filter) {
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

    public void resetFilter() {
        applyFilter(client -> true);
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
            ClientModel client = cache.get(rowIndex);

            switch (columnIndex) {
                case 0:
                    return client.getId();
                case 1:
                    return client.getLastName();
                case 2:
                    return client.getFirstName();
                case 3:
                    return client.getBirthDate();
                case 4:
                    return client.getAddress();
                case 5:
                    return client.getPostalCode();
                case 6:
                    return client.getCity();
                case 7:
                    return client.getLicenses();
                case 8:
                    return client.getEmailAddress();
                case 9:
                    return client.getPhoneNumber();
            }
        }

        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (!cache.isEmpty()) {
            ClientModel client = cache.get(rowIndex);

            // The controller must be used to edit models.

            try {
                switch (columnIndex) {
                    case 1:
                        controller.editClientLastName(client, (String) aValue);
                        break;
                    case 2:
                        controller.editClientFirstName(client, (String) aValue);
                        break;
                    case 3:
                        controller.editClientBirthDate(client, (Date) aValue);
                        break;
                    case 4:
                        controller.editClientAddress(client, (String) aValue);
                        break;
                    case 5:
                        controller.editClientPostalCode(client, (String) aValue);
                        break;
                    case 6:
                        controller.editClientCity(client, (String) aValue);
                        break;
                    case 7:
                        controller.editClientLicenses(client, (String) aValue);
                        break;
                    case 8:
                        controller.editClientEmailAddress(client, (String) aValue);
                        break;
                    case 9:
                        controller.editClientPhoneNumber(client, (String) aValue);
                        break;
                }
            } catch (InvalidDataException e) {
                Window.notifyException(e);
            }
        }
    }
}
