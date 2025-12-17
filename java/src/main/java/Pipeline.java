import dependencies.Config;
import dependencies.Emailer;
import dependencies.Logger;
import dependencies.Project;

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
        boolean testsPassed;
        boolean deploySuccessful;
        boolean previousStepPassed = false;
        String failureReason = "";

        if (project.hasTests()) {
            if ("success".equals(project.runTests())) {
                log.info("Tests passed");
                testsPassed = true;
                previousStepPassed = true;
            } else {
                String reason = "Tests failed";
                log.error(reason);
                testsPassed = false;
                previousStepPassed = false;
                failureReason = reason;
            }
        } else {
            log.info("No tests");
            testsPassed = true;
            previousStepPassed = true;
        }

        if (previousStepPassed) {
            if ("success".equals(project.deploy())) {
                log.info("Deployment successful");
                deploySuccessful = true;
                previousStepPassed = true;
            } else {
                String reason = "Deployment failed";
                log.error(reason);
                deploySuccessful = false;
                previousStepPassed = false;
                failureReason = reason;
            }
        } else {
            deploySuccessful = false;
        }

        if (config.sendEmailSummary()) {
            log.info("Sending email");
            if (deploySuccessful) {
                emailer.send("Deployment completed successfully");
            }

            if (!previousStepPassed)
                emailer.send(failureReason);
        } else {
            log.info("Email disabled");
        }
    }
}
