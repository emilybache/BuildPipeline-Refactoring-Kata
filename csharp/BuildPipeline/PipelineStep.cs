namespace UntangledConditionals
{
    public interface PipelineStep
    {
        PipelineStepResult Run(Project project, PipelineStepResult stepResult);
    }
}

