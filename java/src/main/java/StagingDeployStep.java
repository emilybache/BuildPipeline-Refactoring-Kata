import dependencies.Config;
import dependencies.DeploymentEnvironment;
import dependencies.Logger;
import dependencies.Project;

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
