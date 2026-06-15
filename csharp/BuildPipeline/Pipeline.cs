using System;
using System.Collections.Generic;

namespace UntangledConditionals
{
    public class Pipeline
    {
        private readonly Config _config;
        private readonly Emailer _emailer;
        private readonly Logger _log;
        private readonly List<PipelineStep> _pipeline;

        public Pipeline(Config config, Emailer emailer, Logger log) 
            : this(config, emailer, log, null)
        {
        }

        public Pipeline(Config config, Emailer emailer, Logger log, List<PipelineStep> pipelineSteps)
        {
            _config = config;
            _emailer = emailer;
            _log = log;
            _pipeline = pipelineSteps ?? new List<PipelineStep>
            {
                new TestStep("Tests", config, log),
                new DeployStep("Deployment", config, log),
                new ReportStep("Report", config, log, emailer)
            };
        }

        public void Run(Project project)
        {
            var previousStepResult = new PipelineStepResult("", true, "");
            foreach (var step in _pipeline)
            {
                previousStepResult = step.Run(project, previousStepResult);
            }
        }
    }

    public interface Logger
    {
        void Info(string message);
        void Error(string message);
    }

    public interface Emailer
    {
        void Send(string message);
    }

    public interface Config
    {
        bool SendEmailSummary();
    }
}
