package org.krytonspace.carrent.controllers.model;

import org.krytonspace.carrent.controllers.event.ModelEvent;
import org.krytonspace.carrent.controllers.event.ModelListener;
import org.krytonspace.carrent.models.DatabaseModel;
import org.krytonspace.carrent.models.Model;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Base class for a model controller.
 */
public abstract class BaseModelController {

    /**
     * A reference to the database.
     */
    protected DatabaseModel model;

    /**
     * A list of listeners to notify when changes occur.
     */
    private final List<ModelListener> listeners = new LinkedList<>();

    /**
     * Constructor.
     * @param model A reference to the working database
     */
    public BaseModelController(DatabaseModel model) {
        this.model = model;
    }

    /**
     * Setter.
     * @param model A reference to the working database
     */
    public void setModel(DatabaseModel model) {
        this.model = model;
    }

    /**
     * Notify all listeners that a model has been added to the database.
     * @param m The model that has been added
     */
    protected void fireModelAdded(Model m) {
        SwingUtilities.invokeLater(() -> {
            for (ModelListener l : listeners) {
                l.onModelAdded(new ModelEvent(m));
            }
        });
    }

    /**
     * Notify all listeners that a model has been edited.
     * @param m The model that has been edited
     */
    protected void fireModelEdited(Model m) {
        SwingUtilities.invokeLater(() -> {
            for (ModelListener l : listeners) {
                l.onModelEdited(new ModelEvent(m));
            }
        });
    }

    /**
     * Notify all listeners that a model will be removed from the database.
     * @param m The model that will be removed
     */
    protected void fireModelRemoving(Model m) {
        SwingUtilities.invokeLater(() -> {
            for (ModelListener l : listeners) {
                l.onModelRemoving(new ModelEvent(m));
            }
        });
    }

    /**
     * Add a new listener for model events.
     * @param e The listener to add
     */
    public void addModelListener(ModelListener e) {
        listeners.add(e);
    }

    /**
     * Remove a listener from the notify list.
     * @param e The listener to remove
     */
    public void removeModelListener(ModelListener e) {
        listeners.remove(e);
    }
}
