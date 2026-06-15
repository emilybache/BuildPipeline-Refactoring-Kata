using System;

namespace UntangledConditionals
{
    public class Pipeline
    {
        private readonly Config _config;
        private readonly Emailer _emailer;
        private readonly Logger _log;


        public Pipeline(Config config, Emailer emailer, Logger log)
        {
            _config = config;
            _emailer = emailer;
            _log = log;
        }
        
        public void Run(Project project) {
            bool testsPassed;
            bool deploySuccessful;

            if (project.HasTests()) {
                if ("success".Equals(project.RunTests())) {
                    _log.Info("Tests passed");
                    testsPassed = true;
                } else {
                    _log.Error("Tests failed");
                    testsPassed = false;
                }
            } else {
                _log.Info("No tests");
                testsPassed = true;
            }

            if (testsPassed) {
                if ("success".Equals(project.Deploy())) {
                    _log.Info("Deployment successful");
                    deploySuccessful = true;
                } else {
                    _log.Error("Deployment failed");
                    deploySuccessful = false;
                }
            } else {
                deploySuccessful = false;
            }

            if (_config.SendEmailSummary()) {
                _log.Info("Sending email");
                if (testsPassed) {
                    if (deploySuccessful) {
                        _emailer.Send("Deployment completed successfully");
                    } else {
                        _emailer.Send("Deployment failed");
                    }
                } else {
                    _emailer.Send("Tests failed");
                }
            } else {
                _log.Info("Email disabled");
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