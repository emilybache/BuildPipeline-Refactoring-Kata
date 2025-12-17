import dependencies.Project;
import dependencies.TestStatus;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;

import java.util.List;

public class FiveStepPipelineApprovalTest {
    StringBuilder spy = new StringBuilder("\n");
    DefaultConfig config = new DefaultConfig(true);
    CapturingEmailer emailer = new CapturingEmailer(spy);
    CapturingLogger log = new CapturingLogger(spy);

    List<PipelineStep> pipelineSteps = List.of(
            new TestStep("Unit Tests", config, log),
            new StagingDeployStep("Staging Deployment", config, log),
            new SmokeTestStep("Smoke Tests", config, log),
            new DeployStep("Deployment", config, log),
            new ReportStep("Report", config, log, emailer)
    );
    Pipeline pipeline = new Pipeline(config, emailer, log, pipelineSteps);

    @Test
    void five_step_pipeline_succeeds() {
        var project = Project.builder()
                .setTestStatus(TestStatus.PASSING_TESTS)
                .setDeploysSuccessfullyToStaging(true)
                .setSmokeTestStatus(TestStatus.PASSING_TESTS)
                .setDeploysSuccessfully(true)
                .build();

        pipeline.run(project);

        Approvals.verify(spy);
    }
    @Test
    void missing_smoke_tests() {
        var project = Project.builder()
                .setTestStatus(TestStatus.PASSING_TESTS)
                .setDeploysSuccessfullyToStaging(true)
                .setSmokeTestStatus(TestStatus.NO_TESTS)
                .setDeploysSuccessfully(true)
                .build();

        pipeline.run(project);

        Approvals.verify(spy);
    }

    @Test
    void deploy_staging_fails() {
        var project = Project.builder()
                .setTestStatus(TestStatus.PASSING_TESTS)
                .setDeploysSuccessfullyToStaging(false)
                .setSmokeTestStatus(TestStatus.NO_TESTS)
                .setDeploysSuccessfully(true)
                .build();

        pipeline.run(project);

        Approvals.verify(spy);
    }
}
