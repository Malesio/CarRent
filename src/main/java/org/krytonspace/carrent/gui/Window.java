package org.krytonspace.carrent.gui;

import org.krytonspace.carrent.controllers.DatabaseController;
import org.krytonspace.carrent.controllers.event.DatabaseListener;
import org.krytonspace.carrent.controllers.exceptions.InvalidDataException;
import org.krytonspace.carrent.database.exceptions.LoaderNotFoundException;
import org.krytonspace.carrent.database.exceptions.LoadingFailedException;
import org.krytonspace.carrent.database.exceptions.WritingFailedException;
import org.krytonspace.carrent.gui.dialogs.create.*;
import org.krytonspace.carrent.gui.dialogs.find.FindModelDialog;
import org.krytonspace.carrent.gui.tablemodels.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * The application main window.
 */
public class Window {

    private final JFrame frame;
    private final DatabaseController controller;

    private final JPanel clientsPanel;
    private final JPanel contractsPanel;
    private final JPanel vehiclePanel;
    private final JPanel carPanel;
    private final JPanel bikePanel;
    private final JPanel planePanel;

    private final ClientTableModel clientTableModel;
    private final ContractTableModel contractTableModel;
    private final CarTableModel carTableModel;
    private final BikeTableModel bikeTableModel;
    private final PlaneTableModel planeTableModel;

    /**
     * Create a new window with the specific controller.
     * This permits the creation of two windows displaying the same data.
     *
     * @param controller The controller to extract information from
     */
    public Window(DatabaseController controller) {
        this.controller = controller;
        this.frame = new JFrame();
        this.clientsPanel = new JPanel();
        this.contractsPanel = new JPanel();
        this.vehiclePanel = new JPanel();
        this.carPanel = new JPanel();
        this.bikePanel = new JPanel();
        this.planePanel = new JPanel();

        // Create table models for custom display

        this.clientTableModel = new ClientTableModel(controller.getClientController());
        this.contractTableModel = new ContractTableModel(controller.getContractController());
        this.carTableModel = new CarTableModel(controller.getVehicleController());
        this.bikeTableModel = new BikeTableModel(controller.getVehicleController());
        this.planeTableModel = new PlaneTableModel(controller.getVehicleController());

        // Build the GUI

        setupFrame();
        setupTabs();
        setupMenuBar();

        setupDatabaseListener();
    }

    /**
     * Make this window appear on screen.
     */
    public void show() {
        frame.setVisible(true);
    }

    /**
     * Utility function displaying an exception message to the user.
     * Must only be used with exception that may be caused by the user.
     * Internal errors are to be handled accordingly.
     *
     * @param e The exception to log
     */
    public static void notifyException(Exception e) {
        JOptionPane.showMessageDialog(null,
                e.getClass().getSimpleName() + ": " + e.getMessage(),
                "Exception caught",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Set various properties on the frame.
     */
    private void setupFrame() {
        frame.setTitle("Car Rental v1");
        frame.setSize(1024, 760);
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            frame.setIconImage(ImageIO.read(getClass().getResourceAsStream("/icon.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (controller.hasUnsavedChanges()) {
                    int res = JOptionPane.showConfirmDialog(null,
                            "The current database has been modified.\n" +
                                    "Do you want to save your modifications ?",
                            "Save",
                            JOptionPane.YES_NO_OPTION);
                    if (res == JOptionPane.YES_OPTION) {
                        doSaveDatabase();
                    }
                }
            }
        });

        try {
            // GUI will look like typical native GUIs.
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException |
                InstantiationException |
                IllegalAccessException |
                UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the different tabs for each data type.
     */
    private void setupTabs() {
        setupClientTab();
        setupVehicleTab();
        setupContractTab();

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Clients", clientsPanel);
        tabs.addTab("Contracts", contractsPanel);
        tabs.addTab("Vehicles", vehiclePanel);
        frame.add(tabs);
    }

    /**
     * Create the 'Client' tab layout.
     */
    private void setupClientTab() {
        clientsPanel.setLayout(new BorderLayout());
        JTable clients = new JTable(clientTableModel);

        // Create a right-click menu.
        JPopupMenu popupMenu = new JPopupMenu("Client menu");

        // The action run when clicking on 'Add'.
        Runnable addAction = () -> new CreateClientDialog(frame, controller.getClientController()).show();

        // The action run when clicking on 'Remove'.
        Consumer<String> removeAction = id -> {
            try {
                controller.getClientController().removeClient(id);
            } catch (InvalidDataException e) {
                notifyException(e);
            }
        };

        // Create the popup menu for the client tab.
        setupPopupMenu(clients, popupMenu, addAction, removeAction);

        // If there are too much rows in the table, let the user scroll.
        JScrollPane scrollPane = new JScrollPane(clients);

        clients.setComponentPopupMenu(popupMenu);
        scrollPane.setComponentPopupMenu(popupMenu);

        // Redraw the JTable at every change of the underlying model.
        clientTableModel.addTableModelListener(e -> clients.revalidate());
        clientsPanel.add(scrollPane, BorderLayout.CENTER);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> new CreateClientDialog(frame, controller.getClientController()).show());

        clientsPanel.add(addButton, BorderLayout.SOUTH);
    }

    /**
     * Create the layout of the vehicle tab.
     * Similar to @see setupClientTab().
     */
    private void setupVehicleTab() {
        vehiclePanel.setLayout(new BorderLayout());

        // Create new tabs for each type of vehicle.
        JTabbedPane vehiclesTab = new JTabbedPane();
        carPanel.setLayout(new BorderLayout());

        JTable carTable = new JTable(carTableModel);
        JScrollPane carScrollPane = new JScrollPane(carTable);
        JPopupMenu carPopupMenu = new JPopupMenu("Car menu");
        Runnable carAddAction = () -> new CreateCarDialog(frame, controller.getVehicleController()).show();
        Consumer<String> vehicleRemoveAction = id -> {
            try {
                controller.getVehicleController().removeVehicle(id);
            } catch (InvalidDataException e) {
                notifyException(e);
            }
        };

        setupPopupMenu(carTable, carPopupMenu, carAddAction, vehicleRemoveAction);

        carTable.setComponentPopupMenu(carPopupMenu);
        carScrollPane.setComponentPopupMenu(carPopupMenu);

        JTable bikeTable = new JTable(bikeTableModel);
        JScrollPane bikeScrollPane = new JScrollPane(bikeTable);
        JPopupMenu bikePopupMenu = new JPopupMenu("Bike menu");
        Runnable bikeAddAction = () -> new CreateBikeDialog(frame, controller.getVehicleController()).show();

        setupPopupMenu(bikeTable, bikePopupMenu, bikeAddAction, vehicleRemoveAction);

        bikeTable.setComponentPopupMenu(bikePopupMenu);
        bikeScrollPane.setComponentPopupMenu(bikePopupMenu);

        JTable planeTable = new JTable(planeTableModel);
        JScrollPane planeScrollPane = new JScrollPane(planeTable);
        JPopupMenu planePopupMenu = new JPopupMenu("Plane menu");
        Runnable planeAddAction = () -> new CreatePlaneDialog(frame, controller.getVehicleController()).show();

        setupPopupMenu(planeTable, planePopupMenu, planeAddAction, vehicleRemoveAction);

        planeTable.setComponentPopupMenu(planePopupMenu);
        planeScrollPane.setComponentPopupMenu(planePopupMenu);

        carTableModel.addTableModelListener(e -> carTable.revalidate());
        bikeTableModel.addTableModelListener(e -> bikeTable.revalidate());
        planeTableModel.addTableModelListener(e -> planeTable.revalidate());

        carPanel.add(carScrollPane);
        bikePanel.setLayout(new BorderLayout());
        bikePanel.add(bikeScrollPane);
        planePanel.setLayout(new BorderLayout());
        planePanel.add(planeScrollPane);

        vehiclesTab.addTab("Cars", carPanel);
        vehiclesTab.addTab("Bikes", bikePanel);
        vehiclesTab.addTab("Planes", planePanel);

        vehiclePanel.add(vehiclesTab, BorderLayout.CENTER);
    }

    /**
     * Create the layout of the contract tab.
     * Similar to @see setupClientTab().
     */
    private void setupContractTab() {
        contractsPanel.setLayout(new BorderLayout());
        JTable contractsTable = new JTable(contractTableModel);

        JPopupMenu popupMenu = new JPopupMenu("Contract menu");
        Runnable addAction = () -> new CreateContractDialog(frame,
                controller.getContractController(),
                controller.getVehicleController()).show();
        Consumer<String> removeAction = id -> {
            try {
                controller.getContractController().removeContract(id);
            } catch (InvalidDataException e) {
                notifyException(e);
            }
        };

        setupPopupMenu(contractsTable, popupMenu, addAction, removeAction);

        JScrollPane scrollPane = new JScrollPane(contractsTable);
        contractsTable.setComponentPopupMenu(popupMenu);
        scrollPane.setComponentPopupMenu(popupMenu);

        contractTableModel.addTableModelListener(e -> contractsTable.revalidate());

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> new CreateContractDialog(frame,
                controller.getContractController(),
                controller.getVehicleController()).show());

        contractsPanel.add(scrollPane, BorderLayout.CENTER);
        contractsPanel.add(addButton, BorderLayout.SOUTH);
    }

    /**
     * Create the window menu bar.
     */
    private void setupMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem open = new JMenuItem("Open");
        JMenuItem quit = new JMenuItem("Exit");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem saveAs = new JMenuItem("Save As");

        // Set the usual shortcuts.
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        open.setMnemonic(KeyEvent.VK_O);
        quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
        quit.setMnemonic(KeyEvent.VK_X);
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        save.setMnemonic(KeyEvent.VK_S);
        saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK));
        saveAs.setMnemonic(KeyEvent.VK_A);
        try {
            open.setIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/open.png"))));
            save.setIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/save-as.png"))));
            saveAs.setIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/save-as.png"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        open.addActionListener(e -> {
            JFileChooser chooser = DatabaseController.prepareFileChooser();

            int result = chooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    controller.loadModelFromFile(chooser.getSelectedFile());
                } catch (LoaderNotFoundException | LoadingFailedException ex) {
                    notifyException(ex);
                }
            }
        });
        save.addActionListener(e -> doSaveDatabase());
        saveAs.addActionListener(e -> doSaveToNewDatabase());
        quit.addActionListener(e -> {
            // Ensure the user did not exit by mistake and prevent from data loss.
            if (controller.hasUnsavedChanges()) {
                int res = JOptionPane.showConfirmDialog(null,
                        "The current database has been modified.\n" +
                        "Do you want to save your modifications ?",
                        "Save",
                        JOptionPane.YES_NO_CANCEL_OPTION);
                switch (res) {
                    case JOptionPane.YES_OPTION:
                        doSaveDatabase();
                    case JOptionPane.CANCEL_OPTION:
                        return;
                }
            }
            frame.dispose();
        });

        JMenu edit = new JMenu("Edit");
        JMenuItem find = new JMenuItem("Find");
        JMenuItem lock = new JMenuItem("Lock/Unlock");

        find.setMnemonic(KeyEvent.VK_F);
        find.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));
        lock.setMnemonic(KeyEvent.VK_L);
        lock.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK));

        try {
            find.setIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/find.png"))));
            lock.setIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/lock.png"))));
        } catch (IOException e) {
            e.printStackTrace();
        }

        find.addActionListener(e -> {
            FindModelDialog findDialog = new FindModelDialog(frame);
            findDialog.show();
        });
        lock.addActionListener(e -> {
            // Toggle data edition.
            clientTableModel.setEditable(!clientTableModel.isEditable());
            contractTableModel.setEditable(!contractTableModel.isEditable());
            carTableModel.setEditable(!carTableModel.isEditable());
            bikeTableModel.setEditable(!bikeTableModel.isEditable());
            planeTableModel.setEditable(!planeTableModel.isEditable());

        });

        file.add(open);
        file.add(save);
        file.add(saveAs);
        file.addSeparator();
        file.add(quit);

        edit.add(find);
        edit.add(lock);

        bar.add(file);
        bar.add(edit);
        frame.setJMenuBar(bar);
    }

    /**
     * Create a popup menu for a tab.
     * @param table The table on which the popup menu must operate
     * @param popupMenu The popup menu
     * @param addAction The action that will be run when clicking on 'Add'
     * @param removeAction The action that will be run when clicking on 'Remove'
     */
    private void setupPopupMenu(JTable table,
                                JPopupMenu popupMenu,
                                Runnable addAction,
                                Consumer<String> removeAction) {
        JMenuItem addPopup = new JMenuItem("Add");
        JMenuItem copyPopup = new JMenuItem("Copy");
        JMenuItem removePopup = new JMenuItem("Remove");

        try {
            addPopup.setIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/add.png"))));
            copyPopup.setIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/copy.png"))));
            removePopup.setIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/remove.png"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        popupMenu.add(addPopup);
        popupMenu.add(copyPopup);
        popupMenu.add(removePopup);

        // Select the cell on which the user and right-clicked.
        popupMenu.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(() -> {
                    int hoveredRow = table.rowAtPoint(
                            SwingUtilities.convertPoint(popupMenu, new Point(0, 0), table));
                    int hoveredColumn = table.columnAtPoint(
                            SwingUtilities.convertPoint(popupMenu, new Point(0, 0), table));
                    if (hoveredRow != -1 && hoveredColumn != -1) {
                        table.setRowSelectionInterval(hoveredRow, hoveredRow);
                        table.setColumnSelectionInterval(hoveredColumn, hoveredColumn);
                        removePopup.setEnabled(true);
                        copyPopup.setEnabled(true);
                    } else {
                        removePopup.setEnabled(false);
                        copyPopup.setEnabled(false);
                    }
                });
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) { }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) { }
        });

        addPopup.addActionListener(e -> addAction.run());

        copyPopup.addActionListener(e -> {
            int row = table.getSelectedRow();
            int column = table.getSelectedColumn();
            String value = table.getValueAt(row, column).toString();

            // Put the clicked cell data into the clipboard.
            Toolkit.getDefaultToolkit()
                    .getSystemClipboard()
                    .setContents(new StringSelection(value), null);
        });

        removePopup.addActionListener(e -> {
            int row = table.getSelectedRow();
            String id = (String) table.getModel().getValueAt(row, 0);
            removeAction.accept(id);
        });
    }

    /**
     * Register this window as a receiver of database updates.
     */
    private void setupDatabaseListener() {
        controller.addDatabaseEventListener(new DatabaseListener() {
            @Override
            public void onDatabaseChanged() {
                // The database has changed: enable the save button.
                frame.getJMenuBar().getMenu(0).getItem(1).setEnabled(true);
            }

            @Override
            public void onDatabaseLoading() {
                // The user wants to load a new database, and there are unsaved changes in the working one.
                if (controller.hasUnsavedChanges()) {
                    int res = JOptionPane.showConfirmDialog(null,
                            "The current database has been modified.\n" +
                                    "Do you want to save your modifications ?",
                            "Save",
                            JOptionPane.YES_NO_OPTION);
                    if (res == JOptionPane.YES_OPTION) {
                        doSaveDatabase();
                    }
                }
            }

            @Override
            public void onDatabaseLoaded() {
                // Update model caches. This will consequently update the display too.

                clientTableModel.updateCache();
                contractTableModel.updateCache();
                carTableModel.updateCache();
                bikeTableModel.updateCache();
                planeTableModel.updateCache();

                // Disable the save button.
                frame.getJMenuBar().getMenu(0).getItem(1).setEnabled(false);
            }

            @Override
            public void onDatabaseSaved() {
                JOptionPane.showMessageDialog(null,
                        "Database saved successfully.",
                        "Information",
                        JOptionPane.INFORMATION_MESSAGE);

                // Disable the save button.
                frame.getJMenuBar().getMenu(0).getItem(1).setEnabled(false);
            }
        });
    }

    /**
     * Trigger the 'Save As' action.
     * This method will always ask the user for a location in which to save the database.
     */
    private void doSaveToNewDatabase() {
        JFileChooser chooser = DatabaseController.prepareFileChooser();

        int result = chooser.showSaveDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                controller.saveModelToFile(chooser.getSelectedFile());
            } catch (LoaderNotFoundException | WritingFailedException ex) {
                notifyException(ex);
            }
        }
    }

    /**
     * Trigger the 'Save' action.
     * This method will keep track of the last loaded file, and write to it if it exists.
     * Otherwise, it will trigger the 'Save As' action.
     */
    private void doSaveDatabase() {
        if (controller.getLastLoadedFile() == null) {
            doSaveToNewDatabase();
        } else {
            try {
                controller.saveModelToFile(controller.getLastLoadedFile());
            } catch (LoaderNotFoundException | WritingFailedException ex) {
                notifyException(ex);
            }
        }
    }
}
