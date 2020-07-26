package org.krytonspace.carrent.gui.tablemodels;

import org.krytonspace.carrent.utils.ModelField;
import org.krytonspace.carrent.utils.ModelFieldPair;

import javax.swing.table.AbstractTableModel;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Base table model providing cache updating and resetting for derived classes.
 */
public abstract class BaseTableModel extends AbstractTableModel {

    /**
     * Flag indicating if this model is editable.
     */
    protected boolean editable;

    /**
     * Constructor.
     * The table model is by default immutable.
     */
    public BaseTableModel() {
        this.editable = false;
    }

    /**
     * Setter
     * @param editable The new edition capability
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    /**
     * Getter
     * @return true if this table model is editable, false otherwise
     */
    public boolean isEditable() {
        return editable;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // Deny ID modification.
        return columnIndex != 0 && editable;
    }

    /**
     * Update model cache.
     */
    public abstract void updateCache();

    /**
     * Reset cache filtering, useful to revert tables to show unfiltered data.
     */
    public abstract void resetFilter();

    /**
     * Utility static method gathering display names and types of fields annotated
     * with the ModelField annotation.
     * This method looks for fields in class hierarchy, populating fields from
     * root class to target class.
     *
     * @param modelClass The class to inspect
     * @return The list of (displayName, type) couples corresponding to each field in the specified class
     */
    public static List<ModelFieldPair> getModelFieldsInfo(Class<?> modelClass) {
        if (modelClass != null) {

            List<ModelFieldPair> cols = getModelFieldsInfo(modelClass.getSuperclass());

            for (Field f : modelClass.getDeclaredFields()) {
                if (f.isAnnotationPresent(ModelField.class)) {
                    ModelField modelField = f.getAnnotation(ModelField.class);
                    String displayName = modelField.name();
                    Class<?> type = f.getType();

                    cols.add(new ModelFieldPair(displayName, type));
                }
            }

            return cols;
        }

        return new ArrayList<>();
    }

}
