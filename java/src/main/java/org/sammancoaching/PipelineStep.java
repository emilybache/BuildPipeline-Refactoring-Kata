package org.sammancoaching;

import org.sammancoaching.dependencies.Project;

public interface PipelineStep {
    PipelineStepResult run(Project project, PipelineStepResult stepResult);
}
