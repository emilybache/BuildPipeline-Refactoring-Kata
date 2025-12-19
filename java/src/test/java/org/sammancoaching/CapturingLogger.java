package org.sammancoaching;

import org.sammancoaching.dependencies.Logger;

import java.util.ArrayList;
import java.util.List;
public class CapturingLogger implements Logger {
    private final List<String> lines = new ArrayList<>();
    public final StringBuilder spy;

    public CapturingLogger() {
        this.spy = new StringBuilder();
    }
    public CapturingLogger(StringBuilder spy) {
        this.spy = spy;
    }

    @Override
    public void info(String message) {
        String log = "INFO: " + message;
        spy.append(log).append("\n");
        lines.add(log);
    }

    @Override
    public void error(String message) {
        String log = "ERROR: " + message;
        spy.append(log).append("\n");
        lines.add(log);
    }

    public List<String> getLoggedLines() {
        return lines;
    }
}