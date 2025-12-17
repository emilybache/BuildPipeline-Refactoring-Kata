import dependencies.Config;
import dependencies.Emailer;
import dependencies.Logger;
import dependencies.Project;

import java.util.List;
import java.util.Objects;

public class Pipeline {
    private final Config config;
    private final Emailer emailer;
    private final Logger log;
    private final List<PipelineStep> pipeline;

    public Pipeline(Config config, Emailer emailer, Logger log, List<PipelineStep> pipelineSteps) {
        this.config = config;
        this.emailer = emailer;
        this.log = log;
        this.pipeline = Objects.requireNonNullElseGet(pipelineSteps, () -> List.of(
                new TestStep("Tests", config, log),
                new DeployStep("Deployment", config, log),
                new ReportStep("Report", config, log, emailer)
        ));
    }


    public void run(Project project) {
        var previousStepResult = new PipelineStepResult("", true, "");
        for (var step : pipeline) {
            previousStepResult = step.run(project, previousStepResult);
        }
    }

}
