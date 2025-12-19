package org.sammancoaching;

import org.sammancoaching.dependencies.Config;
import org.sammancoaching.dependencies.Logger;
import org.sammancoaching.dependencies.Project;

public record DeployStep(String name, Config config, Logger log) implements PipelineStep {
    public PipelineStepResult run(Project project, PipelineStepResult previousStepResult) {
        boolean stepPassed = false;
        String failureReason = previousStepResult.failureReason();
        if (previousStepResult.stepPassed()) {
            if ("success".equals(project.deploy())) {
                log.info(name() + " successful");
                stepPassed = true;
            } else {
                String reason = name() + " failed";
                log.error(reason);
                stepPassed = false;
                failureReason = reason;
            }
        }
        return new PipelineStepResult(this.name(), stepPassed, failureReason);
    }
}
