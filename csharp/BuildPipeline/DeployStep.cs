namespace UntangledConditionals
{
    public class DeployStep : PipelineStep
    {
        private readonly string _name;
        private readonly Config _config;
        private readonly Logger _log;

        public DeployStep(string name, Config config, Logger log)
        {
            _name = name;
            _config = config;
            _log = log;
        }

        public PipelineStepResult Run(Project project, PipelineStepResult previousStepResult)
        {
            bool stepPassed = false;
            string failureReason = previousStepResult.FailureReason;

            if (previousStepResult.StepPassed)
            {
                if ("success".Equals(project.Deploy()))
                {
                    _log.Info(_name + " successful");
                    stepPassed = true;
                }
                else
                {
                    string reason = _name + " failed";
                    _log.Error(reason);
                    stepPassed = false;
                    failureReason = reason;
                }
            }

            return new PipelineStepResult(_name, stepPassed, failureReason);
        }
    }
}

