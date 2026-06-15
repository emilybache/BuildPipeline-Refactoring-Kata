namespace UntangledConditionals
{
    public class SmokeTestStep : PipelineStep
    {
        private readonly string _name;
        private readonly Config _config;
        private readonly Logger _log;

        public SmokeTestStep(string name, Config config, Logger log)
        {
            _name = name;
            _config = config;
            _log = log;
        }

        public PipelineStepResult Run(Project project, PipelineStepResult stepResult)
        {
            bool stepPassed = false;
            string failureReason = stepResult.FailureReason;

            if (stepResult.StepPassed)
            {
                switch (project.RunSmokeTests())
                {
                    case TestStatus.NoTests:
                        failureReason = "Missing Smoke Tests";
                        _log.Error(failureReason);
                        break;
                    case TestStatus.PassingTests:
                        stepPassed = true;
                        _log.Info(_name + " passed");
                        break;
                    case TestStatus.FailingTests:
                        failureReason = _name + " failed";
                        _log.Error(failureReason);
                        break;
                }
            }

            return new PipelineStepResult(_name, stepPassed, failureReason);
        }
    }
}

