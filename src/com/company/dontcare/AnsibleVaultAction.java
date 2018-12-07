package com.company.dontcare;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.psi.PsiFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            EventLogger.logError("Please setup the ansible vault password file in settings.");
            return;
        }

        List<String> command = new ArrayList<>();
        command.add("ansible-vault");
        command.add(cryptCommand);
        command.add("--vault-password");
        command.add(pwdFile);
        command.add(filePath);

        File commandOutput = null;
        Process p;
        int exitValue = -1;
        try {
            Path tempFile = Files.createTempFile("", "");
            commandOutput = tempFile.toFile();
            p = new ProcessBuilder()
                    .command(command)
                    .redirectErrorStream(true)
                    .redirectOutput(commandOutput)
                    .start();
            p.waitFor(10L, TimeUnit.SECONDS);
            exitValue = p.exitValue();
        } catch (IOException | InterruptedException e) {
            EventLogger.logError(e.getMessage());
            e.printStackTrace();
        }

        if (exitValue != 0) {
            EventLogger.logError("ansible-vault execution error, exit value: " + exitValue);
            EventLogger.logError(Arrays.toString(command.toArray()));
            try (Stream<String> stream = Files.lines(commandOutput.toPath())) {
                String output = stream.collect(Collectors.joining("\n"));
                EventLogger.logError(output);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
