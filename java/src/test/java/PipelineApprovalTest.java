import dependencies.Project;
import dependencies.TestStatus;
import org.approvaltests.combinations.CombinationApprovals;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * This is a different style of test, an Approval test, to illustrate another way to do this.
 * The overall coverage should be similar to PipelineTest but with less code.
 */
public class PipelineApprovalTest {

    @Test
    void pipeline() {
        TestStatus[] testStatuses = {TestStatus.PASSING_TESTS, TestStatus.NO_TESTS, TestStatus.FAILING_TESTS};
        Boolean[] sendSummaries = {true, false};
        Boolean[] buildsSuccessfullies = {true, false};

        CombinationApprovals.verifyAllCombinations(
                this::doPipelineRun,
                testStatuses,
                sendSummaries,
                buildsSuccessfullies
        );
    }

    private String doPipelineRun(TestStatus testStatus, boolean sendSummary, boolean buildsSuccessfully) {
        var spy = new StringBuilder("\n");
        var config = new DefaultConfig(sendSummary);
        var emailer = new CapturingEmailer(spy);
        var log = new CapturingLogger(spy);

        List<PipelineStep> pipelineSteps = List.of(
                new TestStep(config, log),
                new DeployStep(config, log),
                new ReportStep(config, log, emailer)
        );
        var pipeline = new Pipeline(config, emailer, log, pipelineSteps);

        var project = Project.builder()
                .setTestStatus(testStatus)
                .setDeploysSuccessfully(buildsSuccessfully)
                .build();

        pipeline.run(project);
        return spy.toString();
    }

}
