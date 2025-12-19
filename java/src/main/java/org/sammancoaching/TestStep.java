package org.sammancoaching;

import org.sammancoaching.dependencies.Config;
import org.sammancoaching.dependencies.Logger;
import org.sammancoaching.dependencies.Project;

public record TestStep(String name, Config config, Logger log) implements  PipelineStep {
    public PipelineStepResult run(Project project, PipelineStepResult previousStepResult) {
        boolean stepPassed;
        String failureReason = "";
        if (project.hasTests()) {
            if ("success".equals(project.runTests())) {
                log.info(name() + " passed");
                stepPassed = true;
            } else {
                String reason = name() + " failed";
                log.error(reason);
                stepPassed = false;
                failureReason = reason;
            }
        } else {
            log.info("No tests");
            stepPassed = true;
        }
        return new PipelineStepResult(name(), stepPassed, failureReason);
    }
}
