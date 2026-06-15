namespace UntangledConditionals
{
    public class TestStep : PipelineStep
    {
        private readonly string _name;
        private readonly Config _config;
        private readonly Logger _log;

        public TestStep(string name, Config config, Logger log)
        {
            _name = name;
            _config = config;
            _log = log;
        }

        public PipelineStepResult Run(Project project, PipelineStepResult previousStepResult)
        {
            bool stepPassed;
            string failureReason = "";
            
            if (project.HasTests())
            {
                if ("success".Equals(project.RunTests()))
                {
                    _log.Info(_name + " passed");
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
            else
            {
                _log.Info("No tests");
                stepPassed = true;
            }

            return new PipelineStepResult(_name, stepPassed, failureReason);
        }
    }
}

