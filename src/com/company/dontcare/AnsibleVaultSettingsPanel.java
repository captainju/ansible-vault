package com.company.dontcare;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.fileChooser.FileTextField;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;

import static com.intellij.openapi.ui.TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT;

public class AnsibleVaultSettingsPanel {

    private JPanel contentPane;
    private TextFieldWithBrowseButton passwordFileField;
    private AnsibleVaultSettings ansibleVaultSettings;

    public AnsibleVaultSettingsPanel() {
        ansibleVaultSettings = AnsibleVaultSettings.getInstance();
    }

    public void apply() {
        if (passwordFileField != null) {
            ansibleVaultSettings.setVaultPasswordFile(passwordFileField.getText());
        }
    }

    public void reset() {
        if (passwordFileField != null) {
            passwordFileField.setText(ansibleVaultSettings.getVaultPasswordFile());
        }
    }

    public boolean isModified() {
        if (passwordFileField != null) {
            return !passwordFileField.getText().equals(ansibleVaultSettings.getVaultPasswordFile());
        }
        return false;
    }

    public JPanel getContentPane() {
        if (contentPane == null) {
            contentPane = new JPanel();
            contentPane.setLayout(new GridLayoutManager(3, 2, JBUI.emptyInsets(), -1, -1));
            JLabel passwordFileLabel = new JLabel();
            passwordFileLabel.setText("Password file");
            passwordFileLabel.setToolTipText("Password file");
            contentPane.add(passwordFileLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
            FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFileDescriptor();
            FileTextField field = FileChooserFactory.getInstance().createFileTextField(descriptor, null);
            field.getField().setEnabled(true);
            field.getField().setText("");
            passwordFileField = new TextFieldWithBrowseButton(field.getField());
            passwordFileField.addBrowseFolderListener(null, null, null, descriptor, TEXT_FIELD_WHOLE_TEXT);
            passwordFileField.setAlignmentX(0.5f);
            passwordFileField.setPreferredSize(new Dimension(400, 28));
            contentPane.add(passwordFileField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
            final Spacer spacer1 = new Spacer();
            contentPane.add(spacer1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        }
        return contentPane;
    }

}