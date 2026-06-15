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
    public async Task Pipeline()
    {
        var testStatuses = new[] { TestStatus.PASSING_TESTS, TestStatus.NO_TESTS, TestStatus.FAILING_TESTS };
        var sendSummaries = new[] { true, false };
        var buildsSuccessfullies = new[] { true, false };

        var results = new StringBuilder();

        foreach (var testStatus in testStatuses)
        {
            foreach (var sendSummary in sendSummaries)
            {
                foreach (var buildsSuccessfully in buildsSuccessfullies)
                {
                    var output = DoPipelineRun(testStatus, sendSummary, buildsSuccessfully);
                    results.AppendLine($"[{testStatus}, {sendSummary.ToString().ToLower()}, {buildsSuccessfully.ToString().ToLower()}] => {output}");
                }
            }
        }

        await Verifier.Verify(results.ToString());
    }

    private string DoPipelineRun(TestStatus testStatus, bool sendSummary, bool buildsSuccessfully)
    {
        var spy = new StringBuilder();
        var config = new DefaultConfig(sendSummary);
        var emailer = new SpyEmailer(spy);
        var log = new SpyLogger(spy);
        var pipeline = new Pipeline(config, emailer, log);

        var project = Project.builder()
            .SetTestStatus(testStatus)
            .SetDeploysSuccessfully(buildsSuccessfully)
            .build();

        pipeline.run(project);
        return spy.ToString();
    }

    private class DefaultConfig : Config
    {
        private readonly bool _sendEmail;
        public DefaultConfig(bool sendEmail) => _sendEmail = sendEmail;
        public bool sendEmailSummary() => _sendEmail;
    }

    private class SpyEmailer : Emailer
    {
        private readonly StringBuilder _spy;
        public SpyEmailer(StringBuilder spy) => _spy = spy;
        public void send(string message)
        {
            _spy.AppendLine($"Email message: {message}");
        }
    }

    private class SpyLogger : Logger
    {
        private readonly StringBuilder _spy;
        public SpyLogger(StringBuilder spy) => _spy = spy;

        public void info(string message)
        {
            _spy.AppendLine($"INFO: {message}");
        }

        public void error(string message)
        {
            _spy.AppendLine($"ERROR: {message}");
        }
    }
}