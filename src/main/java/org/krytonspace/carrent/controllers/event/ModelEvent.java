package org.krytonspace.carrent.controllers.event;

import org.krytonspace.carrent.models.Model;

/**
 * A generic event class, representing actions on models.
 */
public class ModelEvent {

    /**
     * The model on which an action has taken place.
     */
    private final Model model;

    /**
     * Create a new event.
     * @param m The model involved
     */
    public ModelEvent(Model m) {
        this.model = m;
    }

    /**
     * Getter on the model.
     * @return The model involved
     */
    public Model getModel() {
        return model;
    }
}
