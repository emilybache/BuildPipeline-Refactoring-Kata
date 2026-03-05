package org.sammancoaching;

import org.sammancoaching.dependencies.Config;

public class DefaultConfig implements Config {

    private boolean shouldSendSummary;

    public DefaultConfig(boolean sendSummary) {
        shouldSendSummary = sendSummary;
    }

    @Override
    public boolean sendNotificationSummary() {
        return shouldSendSummary;
    }
}
