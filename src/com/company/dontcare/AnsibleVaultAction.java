package com.company.dontcare;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AnsibleVaultAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        PsiFile psiFile = e.getData(PlatformDataKeys.PSI_FILE);
        if (psiFile != null) {
            if (isEncrypted(psiFile)) {
                decrypt(psiFile.getVirtualFile().getPath());
            } else {
                encrypt(psiFile.getVirtualFile().getPath());
            }
            psiFile.getVirtualFile().refresh(false, false);
        }
    }

    @Override
    public void update(AnActionEvent e) {
        PsiFile psiFile = e.getData(PlatformDataKeys.PSI_FILE);
        if (psiFile != null && !psiFile.isDirectory()) {
            e.getPresentation().setVisible(true);
            if (isEncrypted(psiFile)) {
                e.getPresentation().setText("Decrypt");
            } else {
                e.getPresentation().setText("Encrypt");
            }
        } else {
            e.getPresentation().setVisible(false);
        }
    }

    private static boolean isEncrypted(PsiFile psiFile) {
        return psiFile.getText().startsWith("$ANSIBLE_VAULT");
    }

    private static void decrypt(String filePath) {
        ansibleVault(filePath, "decrypt");
    }

    private static void encrypt(String filePath) {
        ansibleVault(filePath, "encrypt");
    }

    private static void ansibleVault(String filePath, String cryptCommand) {
        String pwdFile = AnsibleVaultSettings.getInstance().getVaultPasswordFile();
        if (pwdFile == null || "".equalsIgnoreCase(pwdFile) || !new File(pwdFile).exists()) {
            Messages.showMessageDialog("Please setup the ansible vault password file in settings.",
                    "Error", Messages.getErrorIcon());
            return;
        }

        List<String> command = new ArrayList<>();
        command.add("ansible-vault");
        command.add(cryptCommand);
        command.add("--vault-password");
        command.add(pwdFile);
        command.add(filePath);

        Process p = null;
        try {
            p = new ProcessBuilder()
                    .command(command)
                    .start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            p.waitFor(10L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
