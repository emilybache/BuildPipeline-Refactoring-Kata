package org.sammancoaching;

import org.sammancoaching.dependencies.Config;
import org.sammancoaching.dependencies.Logger;
import org.sammancoaching.dependencies.Project;

public record SmokeTestStep(String name, Config config, Logger log) implements PipelineStep {
    @Override
    public PipelineStepResult run(Project project, PipelineStepResult stepResult) {
        boolean stepPassed = false;
        String failureReason = stepResult.failureReason();
        if (stepResult.stepPassed()) {
            switch (project.runSmokeTests()) {
                case NO_TESTS -> {
                    failureReason = "Missing Smoke Tests";
                    log.error(failureReason);
                }
                case PASSING_TESTS -> {
                    stepPassed = true;
                    log.info(name() + " passed");
                }
                case FAILING_TESTS -> {
                    failureReason = name() + " failed";
                    log.error(failureReason);
                }
            }
        }

        return new PipelineStepResult(name(), stepPassed, failureReason);
    }
}
