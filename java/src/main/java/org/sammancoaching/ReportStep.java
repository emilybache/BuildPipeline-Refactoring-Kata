package org.sammancoaching;

import org.sammancoaching.dependencies.Config;
import org.sammancoaching.dependencies.Emailer;
import org.sammancoaching.dependencies.Logger;
import org.sammancoaching.dependencies.Project;

public record ReportStep(String name, Config config, Logger log, Emailer emailer) implements PipelineStep {
    @Override
    public PipelineStepResult run(Project project, PipelineStepResult stepResult) {
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
        return new PipelineStepResult(this.name(), true, stepResult.failureReason());
    }
}
