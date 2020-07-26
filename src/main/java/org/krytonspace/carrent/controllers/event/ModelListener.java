package org.krytonspace.carrent.controllers.event;

/**
 * Callback interface for actions that has to do with models.
 */
public interface ModelListener {
    /**
     * This method is called after a model is created and added to the database.
     * @param e The model event
     */
    void onModelAdded(ModelEvent e);

    /**
     * This method is called before a model is removed from the database.
     * @param e The model event
     */
    void onModelRemoving(ModelEvent e);

    /**
     * This method is called after a model is edited.
     * @param e The model event
     */
    void onModelEdited(ModelEvent e);
}
