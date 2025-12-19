package org.sammancoaching;

import org.sammancoaching.dependencies.Config;
import org.sammancoaching.dependencies.DeploymentEnvironment;
import org.sammancoaching.dependencies.Logger;
import org.sammancoaching.dependencies.Project;

public record StagingDeployStep(String name, Config config, Logger log) implements PipelineStep {
    @Override
    public PipelineStepResult run(Project project, PipelineStepResult previousStepResult) {
        boolean stepPassed = false;
        String failureReason = previousStepResult.failureReason();
        if (previousStepResult.stepPassed()) {
            if ("success".equals(project.deploy(DeploymentEnvironment.STAGING))) {
                log.info(name() + " successful");
                stepPassed = true;
            } else {
                String reason = name() + " failed";
                log.error(reason);
                failureReason = reason;
            }
        }
        return new PipelineStepResult(this.name(), stepPassed, failureReason);
    }
}
