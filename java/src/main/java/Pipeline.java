import dependencies.Config;
import dependencies.Emailer;
import dependencies.Logger;
import dependencies.Project;

public class Pipeline {
    private final Config config;
    private final Emailer emailer;
    private final Logger log;



    public Pipeline(Config config, Emailer emailer, Logger log) {
        this.config = config;
        this.emailer = emailer;
        this.log = log;

    }

    public void run(Project project) {
        var emptyStepResult = new PipelineStepResult("", true, "");
        var testStep = new TestStep(config, log);
        var deployStep = new DeployStep(config, log);
        var reportStep = new ReportStep(config, log, emailer);
        var testResult = testStep.doTestStep(project, emptyStepResult);
        var deployResult = deployStep.doDeployStep(project, testResult);
        var endResult = reportStep.doReportStep(project, deployResult);
    }

}
