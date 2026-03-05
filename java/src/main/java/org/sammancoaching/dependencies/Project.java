package org.sammancoaching.dependencies;

import static org.sammancoaching.dependencies.TestStatus.NO_TESTS;

public class Project {
    private final boolean deploysSuccessfully;
    private final TestStatus testStatus;
    private final boolean deploysSuccessfullyToStaging;
    private final TestStatus smokeTestStatus;

    public static ProjectBuilder builder() {
        return new ProjectBuilder();
    }

    private Project(boolean deploysSuccessfully, TestStatus unitTestStatus, boolean deploysSuccessfullyToStaging, TestStatus smokeTestStatus) {
        this.deploysSuccessfully = deploysSuccessfully;
        this.testStatus = unitTestStatus;
        this.deploysSuccessfullyToStaging = deploysSuccessfullyToStaging;
        this.smokeTestStatus = smokeTestStatus;
    }

    public String runTests() { return testStatus.getStatus(); }

    public String deploy() {
        return deploy(DeploymentEnvironment.PRODUCTION);
    }
    public String deploy(DeploymentEnvironment environment) {
        switch (environment) {
            case STAGING:
                return deploysSuccessfullyToStaging ? "success" : "failure";
            case PRODUCTION:
                return deploysSuccessfully ? "success" : "failure";
            default:
                return "failure";
        }
    }

    public TestStatus runSmokeTests() {
        return smokeTestStatus;
    }

    public static class ProjectBuilder {
        private boolean deploysSuccessfully;
        private TestStatus testStatus;
        private boolean deploysSuccessfullyToStaging = false;
        private TestStatus smokeTestStatus = NO_TESTS;

        public ProjectBuilder setTestStatus(TestStatus testStatus) {
            this.testStatus = testStatus;
            return this;
        }

        public ProjectBuilder setSmokeTestStatus(TestStatus smokeTestStatus) {
            this.smokeTestStatus = smokeTestStatus;
            return this;
        }

        public ProjectBuilder setDeploysSuccessfully(boolean deploysSuccessfully) {
            this.deploysSuccessfully = deploysSuccessfully;
            return this;
        }

        public ProjectBuilder setDeploysSuccessfullyToStaging(boolean deploysSuccessfully) {
            this.deploysSuccessfullyToStaging = deploysSuccessfully;
            return this;
        }

        public Project build() {
            return new Project(deploysSuccessfully, testStatus, deploysSuccessfullyToStaging, smokeTestStatus);
        }
    }
}
