package org.sammancoaching;

import org.sammancoaching.dependencies.NotificationService;

public class EmailService implements NotificationService {
    public final StringBuilder spy;

    public EmailService(StringBuilder spy) {
        this.spy = spy;
    }

    @Override
    public void send(String message) {
        spy.append("Email message: ");
        spy.append(message).append("\n");
    }
}
