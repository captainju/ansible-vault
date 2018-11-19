package com.company.dontcare;

import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class AnsibleVaultConfigurable implements SearchableConfigurable {
    private AnsibleVaultSettingsPanel ansibleVaultSettingsPanel;


    @NotNull
    @Override
    public String getId() {
        return "ansiblevault";
    }

    @Override
    public Runnable enableSearch(String s) {
        return null;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "Ansible vault";
    }

    @Override
    public JComponent createComponent() {
        if (ansibleVaultSettingsPanel == null) {
            ansibleVaultSettingsPanel = new AnsibleVaultSettingsPanel();
        }
        reset();
        return ansibleVaultSettingsPanel.getContentPane();
    }

    @Override
    public boolean isModified() {
        if (ansibleVaultSettingsPanel != null) {
            return ansibleVaultSettingsPanel.isModified();
        }
        return false;
    }

    @Override
    public void apply() {
        if (ansibleVaultSettingsPanel != null) {
            ansibleVaultSettingsPanel.apply();
        }
    }

    @Override
    public void reset() {
        if (ansibleVaultSettingsPanel != null) {
            ansibleVaultSettingsPanel.reset();
        }
    }

    @Override
    public void disposeUIResources() {
        ansibleVaultSettingsPanel = null;
    }
}