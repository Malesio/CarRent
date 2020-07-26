package org.krytonspace.carrent.gui.dialogs.create;

import org.krytonspace.carrent.gui.dialogs.ModelDialog;

import javax.swing.*;

/**
 * Base dialog for model creation.
 */
public abstract class CreateModelDialog extends ModelDialog {

    public CreateModelDialog(JFrame owner, String modelName, String[] fields) {
        super(owner, "Create", modelName, fields);
    }
}
