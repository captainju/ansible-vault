package com.company.dontcare;

import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

@State(
        name = "AnsibleVaultSettings",
        storages = {@Storage(
                file = "$APP_CONFIG$/ansiblevault_settings.xml"
        )}
)
public class AnsibleVaultSettings implements ApplicationComponent, PersistentStateComponent<AnsibleVaultSettings> {
    private String vaultPasswordFile;

    public static AnsibleVaultSettings getInstance() {
        return ServiceManager.getService(AnsibleVaultSettings.class);
    }

    @Override
    public AnsibleVaultSettings getState() {
        return this;
    }

    @Override
    public void loadState(AnsibleVaultSettings ansibleVaultSettings) {
        XmlSerializerUtil.copyBean(ansibleVaultSettings, this);
    }

    public String getVaultPasswordFile() {
        return vaultPasswordFile;
    }

    public void setVaultPasswordFile(String vaultPasswordFile) {
        this.vaultPasswordFile = vaultPasswordFile;
    }

    @Override
    public void initComponent() {

    }

    @Override
    public void disposeComponent() {

    }

    @NotNull
    @Override
    public String getComponentName() {
        return "AnsibleVaultSettings";
    }
}