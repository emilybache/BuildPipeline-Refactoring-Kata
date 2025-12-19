package org.sammancoaching;

import org.sammancoaching.dependencies.Emailer;

public class CapturingEmailer implements Emailer {
    public final StringBuilder spy;

    public CapturingEmailer(StringBuilder spy) {
        this.spy = spy;
    }

    @Override
    public void send(String message) {
        spy.append("Email message: ");
        spy.append(message).append("\n");
    }
}
