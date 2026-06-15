using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Xunit;
using VerifyTests;
using VerifyXunit;
using UntangledConditionals;

namespace BuildPipelineTests;

public class PipelineApprovalTest
{
    [Fact]
    public async Task PassingTests_EmailEnabled_DeploymentSuccessful()
    {
        var output = DoPipelineRun(TestStatus.PassingTests, true, true);
        await Verifier.Verify(output);
    }

    [Fact]
    public async Task PassingTests_EmailEnabled_DeploymentFailed()
    {
        var output = DoPipelineRun(TestStatus.PassingTests, true, false);
        await Verifier.Verify(output);
    }

    [Fact]
    public async Task PassingTests_EmailDisabled_DeploymentSuccessful()
    {
        var output = DoPipelineRun(TestStatus.PassingTests, false, true);
        await Verifier.Verify(output);
    }

    [Fact]
    public async Task NoTests_EmailEnabled_DeploymentSuccessful()
    {
        var output = DoPipelineRun(TestStatus.NoTests, true, true);
        await Verifier.Verify(output);
    }

    [Fact]
    public async Task NoTests_EmailEnabled_DeploymentFailed()
    {
        var output = DoPipelineRun(TestStatus.NoTests, true, false);
        await Verifier.Verify(output);
    }

    [Fact]
    public async Task FailingTests_EmailEnabled_DeploymentSuccessful()
    {
        var output = DoPipelineRun(TestStatus.FailingTests, true, true);
        await Verifier.Verify(output);
    }

    private string DoPipelineRun(TestStatus testStatus, bool sendSummary, bool buildsSuccessfully)
    {
        var spy = new StringBuilder();
        var config = new DefaultConfig(sendSummary);
        var emailer = new SpyEmailer(spy);
        var log = new SpyLogger(spy);
        var pipeline = new Pipeline(config, emailer, log);

        var project = Project.Builder()
            .SetTestStatus(testStatus)
            .SetDeploysSuccessfully(buildsSuccessfully)
            .Build();

        pipeline.Run(project);
        return spy.ToString();
    }

    private class DefaultConfig : Config
    {
        private readonly bool _sendEmail;
        public DefaultConfig(bool sendEmail) => _sendEmail = sendEmail;
        public bool SendEmailSummary() => _sendEmail;
    }

    private class SpyEmailer : Emailer
    {
        private readonly StringBuilder _spy;
        public SpyEmailer(StringBuilder spy) => _spy = spy;
        public void Send(string message)
        {
            _spy.AppendLine($"Email message: {message}");
        }
    }

    private class SpyLogger : Logger
    {
        private readonly StringBuilder _spy;
        public SpyLogger(StringBuilder spy) => _spy = spy;

        public void Info(string message)
        {
            _spy.AppendLine($"INFO: {message}");
        }

        public void Error(string message)
        {
            _spy.AppendLine($"ERROR: {message}");
        }
    }
}

