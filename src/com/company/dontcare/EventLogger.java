package com.company.dontcare;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;


public class EventLogger {
    private static final String GROUP_DISPLAY_ID = "AnsibleVault plugin";
    private static final String TITLE = "AnsibleVault Event Log";

    public static void logError(String msg) {
        Notification notification = new Notification(GROUP_DISPLAY_ID, TITLE, msg, NotificationType.ERROR);
        Notifications.Bus.notify(notification);
    }
}