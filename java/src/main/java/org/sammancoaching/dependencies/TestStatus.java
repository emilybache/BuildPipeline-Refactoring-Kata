package org.sammancoaching.dependencies;

public enum TestStatus {
    NO_TESTS("No tests were run"), //
    PASSING_TESTS("success"), //
    FAILING_TESTS("failure");

    private final String status;

    TestStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}