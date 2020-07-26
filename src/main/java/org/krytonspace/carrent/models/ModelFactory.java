package org.krytonspace.carrent.models;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory class, holding counts for models and ensuring uniqueness of IDs (up to Integer.MAX_VALUE).
 */
public final class ModelFactory {

    private static final Map<Class<? extends Model>, Integer> MODEL_COUNTERS = new HashMap<>();

    static {
        // Register default models on static initialization.
        registerNewModel(ClientModel.class);
        registerNewModel(ContractModel.class);
        registerNewModel(PlaneModel.class);
        registerNewModel(BikeModel.class);
        registerNewModel(CarModel.class);
    }

    /**
     * Constructor.
     */
    private ModelFactory() {
        // Can't create this class.
    }

    /**
     * Add a new model dynamically.
     * @param modelClass The model to register
     * @param <T> The model type, must inherit Model
     */
    public static <T extends Model> void registerNewModel(Class<T> modelClass) {
        if (!Modifier.isAbstract(modelClass.getModifiers())) {
            MODEL_COUNTERS.put(modelClass, 0);
        }
    }

    /**
     * Generate a unique ID for a given Model class.
     * @param modelClass The model to retrieve an ID for
     * @param <T> The model type, must inherit Model
     * @return A unique ID for the model
     */
    public static <T extends Model> int generateIdForModel(Class<T> modelClass) {
        int id = MODEL_COUNTERS.get(modelClass);
        MODEL_COUNTERS.put(modelClass, id + 1);

        return id;
    }

}
