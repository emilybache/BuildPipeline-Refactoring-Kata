import dependencies.Config;
import dependencies.Emailer;
import dependencies.Logger;
import dependencies.Project;

public record ReportStep(Config config, Logger log, Emailer emailer) {
    PipelineStepResult doReportStep(Project project, PipelineStepResult stepResult) {
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
        return new PipelineStepResult("Report", true, "");
    }
}
