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

        boolean step1Passed = false;
        String failureReason = "";
        if (project.hasTests()) {
            if ("success".equals(project.runTests())) {
                log.info("Tests passed");
                step1Passed = true;
            } else {
                String reason = "Tests failed";
                log.error(reason);
                step1Passed = false;
                failureReason = reason;
            }
        } else {
            log.info("No tests");
            step1Passed = true;
        }
        var stepResult = new PipelineStepResult("Tests", step1Passed, failureReason);

        boolean step2Passed = false;
        String failure2Reason = "";
        if (stepResult.stepPassed()) {
            if ("success".equals(project.deploy())) {
                log.info("Deployment successful");
                step2Passed = true;
            } else {
                String reason = "Deployment failed";
                log.error(reason);
                step2Passed = false;
                failure2Reason = reason;
            }
        } else {
            failure2Reason = stepResult.failureReason();
        }
        stepResult = new PipelineStepResult("Deployment", step2Passed, failure2Reason);

        if (config.sendEmailSummary()) {
            log.info("Sending email");
            if (stepResult.stepPassed()) {
                emailer.send("Deployment completed successfully");
            } else {
                emailer.send(stepResult.failureReason());
            }
        } else {
            log.info("Email disabled");
        }
        stepResult = new PipelineStepResult("Report", true, "");

    }
}
