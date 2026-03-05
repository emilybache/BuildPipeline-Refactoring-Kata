package org.sammancoaching;

import org.sammancoaching.dependencies.Config;
import org.sammancoaching.dependencies.Emailer;
import org.sammancoaching.dependencies.Logger;
import org.sammancoaching.dependencies.Project;

public class Pipeline {
    private final Config config;
    private final Emailer emailer;
    private final Logger log;

    public Pipeline(Config config, Emailer emailer, Logger log) {
        this.config = config;
        this.emailer = emailer;
        this.log = log;
    }

    public void run(Project project) {
        boolean testsPassed = isTestsPassed(project);
        boolean deploySuccessful = isDeploySuccessful(project, testsPassed);
        sendEmail(testsPassed, deploySuccessful);
    }

    private boolean isTestsPassed(Project project) {
        if ("success".equals(project.runTests())) {
            log.info("Tests passed");
            return true;
        }

        if ("failure".equals(project.runTests())) {
            log.error("Tests failed");
            return false;
        }

        log.info("No tests");
        return true;
    }

    private boolean isDeploySuccessful(Project project, boolean testsPassed) {
        if (!testsPassed) {
            return false;
        }

        if ("success".equals(project.deploy())) {
            log.info("Deployment successful");
            return true;
        }

        log.error("Deployment failed");
        return false;
    }

    private void sendEmail(boolean testsPassed, boolean deploySuccessful) {
        if (!config.sendEmailSummary()) {
            log.info("Email disabled");
            return;
        }

        log.info("Sending email");

        if (!testsPassed) {
            emailer.send("Tests failed");
            return;
        }

        if (deploySuccessful) {
            emailer.send("Deployment completed successfully");
            return;
        }

        emailer.send("Deployment failed");
    }
}
