package org.krytonspace.carrent.gui.dialogs;

import org.krytonspace.carrent.controllers.exceptions.InvalidDataException;
import org.krytonspace.carrent.gui.Window;

import javax.swing.*;
import java.awt.*;

/**
 * Base dialog for interactions with models.
 * The layout is set to a grid of two columns and a specified number of rows,
 * with a button at the bottom of the dialog.
 *
 * Each row is composed of a label and a text field for user input.
 */
public abstract class ModelDialog {

    private final JDialog dialog;
    private final String[] fields;
    private final JLabel[] labels;
    protected final JTextField[] textFields;
    private final JButton actionButton;

    /**
     * Constructor.
     * @param owner The owner of this dialog
     * @param action The action denoted by this dialog
     * @param modelName The type of data this dialog will be managing
     * @param fields The labels contents
     */
    public ModelDialog(JFrame owner, String action, String modelName, String[] fields) {
        this.dialog = new JDialog(owner, action + " " + modelName);
        this.fields = fields;
        this.labels = new JLabel[fields.length];
        this.textFields = new JTextField[fields.length];
        this.actionButton = new JButton(action);

        setupDialog();
        setupFields();
        setupActionButton();
    }

    public void show() {
        dialog.setVisible(true);
    }

    private void setupActionButton() {
        actionButton.addActionListener(e -> {
            String[] fieldValues = new String[fields.length];

            // Retrieve data from the text fields
            for (int i = 0; i < fields.length; i++) {
                String value = textFields[i].getText().trim();
                fieldValues[i] = value;
            }

            try {
                // Derived class know what to do with it.
                processValues(fieldValues);
            } catch (InvalidDataException invalidDataException) {
                Window.notifyException(invalidDataException);
            }
        });

        dialog.add(actionButton, BorderLayout.SOUTH);
    }

    private void setupFields() {
        JPanel mainPanel = new JPanel(new GridLayout(fields.length, 2, 20, 5));

        for (int i = 0; i < fields.length; i++) {
            labels[i] = new JLabel(fields[i]);
            labels[i].setHorizontalAlignment(JLabel.CENTER);
            textFields[i] = new JTextField();
            textFields[i].setPreferredSize(new Dimension(200, 30));
            mainPanel.add(labels[i]);
            mainPanel.add(textFields[i]);
        }

        dialog.add(mainPanel, BorderLayout.CENTER);
    }

    private void setupDialog() {
        dialog.setLayout(new BorderLayout());
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    protected void clearFields() {
        for (JTextField f : textFields) {
            f.setText("");
        }
    }

    /**
     * Process text fields contents.
     * The order of elements in the values array matches the field order, as in:
     * values[i] is the value for the field field[i].
     *
     * @param values content from text fields
     * @throws InvalidDataException if user input from text fields is not valid
     */
    protected abstract void processValues(String[] values) throws InvalidDataException;
}
