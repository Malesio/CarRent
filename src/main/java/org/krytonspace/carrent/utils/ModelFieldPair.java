package org.krytonspace.carrent.utils;

/**
 * Simple bean class holding a field display name and its type,
 * useful to table models to know what type of data they are displaying.
 */
public class ModelFieldPair {
    private final String name;
    private final Class<?> type;

    /**
     * Constructor
     * @param name The field display name
     * @param type The field type
     */
    public ModelFieldPair(String name, Class<?> type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Getter
     * @return The field display name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter
     * @return The field type
     */
    public Class<?> getType() {
        return type;
    }
}
