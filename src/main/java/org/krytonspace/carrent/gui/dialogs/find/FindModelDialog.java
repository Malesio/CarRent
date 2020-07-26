package org.krytonspace.carrent.gui.dialogs.find;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;

public class FindModelDialog {
    private static final String[] MODEL_NAMES = new String[] {"Client", "Contract", "Car", "Bike", "Plane"};

    private final JDialog dialog;
    private final JPanel globalPanel;
    private final JPanel[] panels;

    private Runnable action;

    public FindModelDialog(JFrame owner) {
        this.dialog = new JDialog(owner, "Find");
        this.globalPanel = new JPanel();
        this.panels = new JPanel[MODEL_NAMES.length];

        for (int i = 0; i < panels.length; i++) {
            panels[i] = new JPanel();
        }

        setupDialog();
        setupComboBox();
        setupPanels();
        setupActionButtons();
    }

    private void setupPanels() {
        panels[0].setLayout(new GridLayout(5, 2));
        panels[1].setLayout(new GridLayout(3, 2));
        panels[2].setLayout(new GridLayout(1, 2));
        panels[3].setLayout(new GridLayout(1, 2));
        panels[4].setLayout(new GridLayout(1, 2));

        for (JPanel p : panels) {
            JCheckBox idCriteria = new JCheckBox("ID");
            JTextField idTextField = new JTextField();
            idTextField.setEnabled(false);

            idCriteria.addActionListener(e -> idTextField.setEnabled(idCriteria.isSelected()));

            p.add(idCriteria);
            p.add(idTextField);
        }

        try {
            setupClientPanel();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setupContractPanel();
        setupCarPanel();
        setupBikePanel();
        setupPlanePanel();

        globalPanel.setLayout(new CardLayout());

        for (int i = 0; i < MODEL_NAMES.length; i++) {
            globalPanel.add(panels[i], MODEL_NAMES[i]);
        }

        dialog.add(globalPanel, BorderLayout.CENTER);
    }

    private void setupPlanePanel() {
    }

    private void setupBikePanel() {
    }

    private void setupCarPanel() {
    }

    private void setupClientPanel() throws ParseException {
        JCheckBox lastNameCriteria = new JCheckBox("Last name");
        JTextField lastNameField = new JTextField();
        lastNameField.setEnabled(false);

        JCheckBox firstNameCriteria = new JCheckBox("First name");
        JTextField firstNameField = new JTextField();
        firstNameField.setEnabled(false);

        JCheckBox postalCodeCriteria = new JCheckBox("Postal code");
        JFormattedTextField postalCodeField = new JFormattedTextField(new MaskFormatter("#####"));
        postalCodeField.setEnabled(false);

        JCheckBox cityCriteria = new JCheckBox("City");
        JTextField cityField = new JTextField();
        cityField.setEnabled(false);

        lastNameCriteria.addActionListener(e -> lastNameField.setEnabled(lastNameCriteria.isSelected()));
        firstNameCriteria.addActionListener(e -> firstNameField.setEnabled(firstNameCriteria.isSelected()));
        postalCodeCriteria.addActionListener(e -> postalCodeField.setEnabled(postalCodeCriteria.isSelected()));
        cityCriteria.addActionListener(e -> cityField.setEnabled(cityCriteria.isSelected()));

        panels[0].add(lastNameCriteria);
        panels[0].add(lastNameField);
        panels[0].add(firstNameCriteria);
        panels[0].add(firstNameField);
        panels[0].add(postalCodeCriteria);
        panels[0].add(postalCodeField);
        panels[0].add(cityCriteria);
        panels[0].add(cityField);
    }

    private void setupContractPanel() {
        JCheckBox clientFirstNameCriteria = new JCheckBox("Client first name");
        JTextField clientFirstNameField = new JTextField();
        clientFirstNameField.setEnabled(false);

        clientFirstNameCriteria.addActionListener(e ->
                clientFirstNameField.setEnabled(clientFirstNameCriteria.isSelected()));

        panels[1].add(clientFirstNameCriteria);
        panels[1].add(clientFirstNameField);
    }

    private void setupComboBox() {
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new FlowLayout());

        JComboBox<String> list = new JComboBox<>(MODEL_NAMES);
        list.setEditable(false);
        list.addItemListener(e -> {
            CardLayout layout = (CardLayout) globalPanel.getLayout();
            layout.show(globalPanel, (String) e.getItem());
        });
        listPanel.add(new JLabel("Data to find: "));
        listPanel.add(list);

        dialog.add(listPanel, BorderLayout.PAGE_START);
    }

    private void setupActionButtons() {
        JPanel actionPanel = new JPanel();

        JButton doFind = new JButton("Find");

        actionPanel.add(doFind);
        dialog.add(actionPanel, BorderLayout.SOUTH);
    }

    private void setupDialog() {
        dialog.setSize(600, 350);
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLayout(new BorderLayout());
    }

    public void show() {
        dialog.setVisible(true);
    }
}
