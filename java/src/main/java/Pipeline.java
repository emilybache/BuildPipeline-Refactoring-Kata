import dependencies.Config;
import dependencies.Emailer;
import dependencies.Logger;
import dependencies.Project;

import java.util.List;

public class Pipeline {
    private final Config config;
    private final Emailer emailer;
    private final Logger log;
    private final List<PipelineStep> pipeline;

    public Pipeline(Config config, Emailer emailer, Logger log) {
        this.config = config;
        this.emailer = emailer;
        this.log = log;
        this.pipeline = List.of(
                new TestStep(config, log),
                new DeployStep(config, log),
                new ReportStep(config, log, emailer)
        );
    }

    public void run(Project project) {
        var previousStepResult = new PipelineStepResult("", true, "");
        for (var step : pipeline) {
            previousStepResult = step.run(project, previousStepResult);
        }
    }

}
