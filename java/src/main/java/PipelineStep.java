import dependencies.Project;

public interface PipelineStep {
    PipelineStepResult run(Project project, PipelineStepResult stepResult);
}
