using System.Collections.Generic;
using Xunit;
using Moq;
using UntangledConditionals;

namespace BuildPipelineTests;

public class PipelineTest
{
    private Pipeline _pipeline;
    private CapturingLogger _log = new CapturingLogger();
    private Mock<Config> _configMock;
    private Mock<Emailer> _emailerMock;

    public PipelineTest()
    {
        _configMock = new Mock<Config>();
        _emailerMock = new Mock<Emailer>();
        _pipeline = new Pipeline(_configMock.Object, _emailerMock.Object, _log);
    }

    [Fact]
    void project_with_tests_that_deploys_successfully_with_email_notification()
    {
        _configMock.Setup(c => c.SendEmailSummary()).Returns(true);

        Project project = Project.Builder() //
            .SetTestStatus(TestStatus.PassingTests) //
            .SetDeploysSuccessfully(true) //
            .Build();

        _pipeline.Run(project);

        Assert.Equal(new List<string>()
        {
            "INFO: Tests passed", //
            "INFO: Deployment successful", //
            "INFO: Sending email" //
        }, _log.Lines);

        _emailerMock.Verify(m => m.Send("Deployment completed successfully"));
    }

    [Fact]
    void project_with_tests_that_deploys_successfully_without_email_notification()
    {
        _configMock.Setup(c => c.SendEmailSummary()).Returns(false);

        Project project = Project.Builder() //
            .SetTestStatus(TestStatus.PassingTests) //
            .SetDeploysSuccessfully(true) //
            .Build();

        _pipeline.Run(project);

        Assert.Equal(new List<string>()
        {
            "INFO: Tests passed", //
            "INFO: Deployment successful", //
            "INFO: Email disabled" //
        }, _log.Lines);

        _emailerMock.Verify(m => m.Send(It.IsAny<string>()), Times.Never);
    }

    [Fact]
    void project_without_tests_that_deploys_successfully_with_email_notification()
    {
        _configMock.Setup(c => c.SendEmailSummary()).Returns(true);

        Project project = Project.Builder() //
            .SetTestStatus(TestStatus.NoTests) //
            .SetDeploysSuccessfully(true) //
            .Build();

        _pipeline.Run(project);

        Assert.Equal(new List<string>()
        {
            "INFO: No tests", //
            "INFO: Deployment successful", //
            "INFO: Sending email" //
        }, _log.Lines);

        _emailerMock.Verify(m => m.Send("Deployment completed successfully"));
    }

    [Fact]
    void project_without_tests_that_deploys_successfully_without_email_notification()
    {
        _configMock.Setup(c => c.SendEmailSummary()).Returns(false);

        Project project = Project.Builder() //
            .SetTestStatus(TestStatus.NoTests) //
            .SetDeploysSuccessfully(true) //
            .Build();

        _pipeline.Run(project);

        Assert.Equal(new List<string>()
        {
            "INFO: No tests", //
            "INFO: Deployment successful", //
            "INFO: Email disabled" //
        }, _log.Lines);

        _emailerMock.Verify(m => m.Send(It.IsAny<string>()), Times.Never);
    }

    [Fact]
    void project_with_tests_that_fail_with_email_notification()
    {
        _configMock.Setup(c => c.SendEmailSummary()).Returns(true);

        Project project = Project.Builder() //
            .SetTestStatus(TestStatus.FailingTests) //
            .Build();

        _pipeline.Run(project);

        Assert.Equal(new List<string>()
        {
            "ERROR: Tests failed", //
            "INFO: Sending email" //
        }, _log.Lines);

        _emailerMock.Verify(m => m.Send("Tests failed"));
    }

    [Fact]
    void project_with_tests_that_fail_without_email_notification()
    {
        _configMock.Setup(c => c.SendEmailSummary()).Returns(false);
        Project project = Project.Builder() //
            .SetTestStatus(TestStatus.FailingTests) //
            .Build();

        _pipeline.Run(project);

        Assert.Equal(new List<string>()
        {
            "ERROR: Tests failed", //
            "INFO: Email disabled" //
        }, _log.Lines);

        _emailerMock.Verify(m => m.Send(It.IsAny<string>()), Times.Never);
    }

    [Fact]
    void project_with_tests_and_failing_build_with_email_notification()
    {
        _configMock.Setup(c => c.SendEmailSummary()).Returns(true);

        Project project = Project.Builder() //
            .SetTestStatus(TestStatus.PassingTests) //
            .SetDeploysSuccessfully(false) //
            .Build();

        _pipeline.Run(project);

        Assert.Equal(new List<string>()
        {
            "INFO: Tests passed", //
            "ERROR: Deployment failed", //
            "INFO: Sending email" //
        }, _log.Lines);

        _emailerMock.Verify(m => m.Send("Deployment failed"));
    }

    [Fact]
    void project_with_tests_and_failing_build_without_email_notification()
    {
        _configMock.Setup(c => c.SendEmailSummary()).Returns(false);

        Project project = Project.Builder() //
            .SetTestStatus(TestStatus.PassingTests) //
            .SetDeploysSuccessfully(false) //
            .Build();

        _pipeline.Run(project);

        Assert.Equal(new List<string>()
        {
            "INFO: Tests passed", //
            "ERROR: Deployment failed", //
            "INFO: Email disabled" //
        }, _log.Lines);

        _emailerMock.Verify(m => m.Send(It.IsAny<string>()), Times.Never);
    }

    [Fact]
    void project_without_tests_and_failing_build_with_email_notification()
    {
        _configMock.Setup(c => c.SendEmailSummary()).Returns(true);

        Project project = Project.Builder() //
            .SetTestStatus(TestStatus.NoTests) //
            .SetDeploysSuccessfully(false) //
            .Build();

        _pipeline.Run(project);

        Assert.Equal(new List<string>()
        {
            "INFO: No tests", //
            "ERROR: Deployment failed", //
            "INFO: Sending email" //
        }, _log.Lines);

        _emailerMock.Verify(m => m.Send("Deployment failed"));
    }

    [Fact]
    void project_without_tests_and_failing_build_without_email_notification()
    {
        _configMock.Setup(c => c.SendEmailSummary()).Returns(false);

        Project project = Project.Builder() //
            .SetTestStatus(TestStatus.NoTests) //
            .SetDeploysSuccessfully(false) //
            .Build();

        _pipeline.Run(project);

        Assert.Equal(new List<string>()
        {
            "INFO: No tests", //
            "ERROR: Deployment failed", //
            "INFO: Email disabled" //
        }, _log.Lines);

        _emailerMock.Verify(m => m.Send(It.IsAny<string>()), Times.Never);
    }
}