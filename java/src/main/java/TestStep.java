import dependencies.Config;
import dependencies.Logger;
import dependencies.Project;

public record TestStep(Config config, Logger log) {
    PipelineStepResult doTestStep(Project project, PipelineStepResult previousStepResult) {
        boolean stepPassed;
        String failureReason = "";
        if (project.hasTests()) {
            if ("success".equals(project.runTests())) {
                log.info("Tests passed");
                stepPassed = true;
            } else {
                String reason = "Tests failed";
                log.error(reason);
                stepPassed = false;
                failureReason = reason;
            }
        } else {
            log.info("No tests");
            stepPassed = true;
        }
        return new PipelineStepResult("Tests", stepPassed, failureReason);
    }
}
