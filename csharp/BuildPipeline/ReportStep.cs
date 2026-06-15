namespace UntangledConditionals
{
    public class ReportStep : PipelineStep
    {
        private readonly string _name;
        private readonly Config _config;
        private readonly Logger _log;
        private readonly Emailer _emailer;

        public ReportStep(string name, Config config, Logger log, Emailer emailer)
        {
            _name = name;
            _config = config;
            _log = log;
            _emailer = emailer;
        }

        public PipelineStepResult Run(Project project, PipelineStepResult stepResult)
        {
            if (_config.SendEmailSummary())
            {
                _log.Info("Sending email");
                if (stepResult.StepPassed)
                {
                    _emailer.Send("Deployment completed successfully");
                }
                else
                {
                    _emailer.Send(stepResult.FailureReason);
                }
            }
            else
            {
                _log.Info("Email disabled");
            }

            return new PipelineStepResult(_name, true, stepResult.FailureReason);
        }
    }
}

