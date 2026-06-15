using System;

namespace UntangledConditionals
{
    public enum TestStatus {
        NoTests, //
        PassingTests, //
        FailingTests
    }
    
    public class Project
    {
        private readonly bool _deploysSuccessfully;
        private readonly TestStatus _testStatus;
        private readonly bool _deploysSuccessfullyToStaging;
        private readonly TestStatus _smokeTestStatus;

        public static ProjectBuilder Builder() {
            return new ProjectBuilder();
        }

        private Project(bool deploysSuccessfully, TestStatus testStatus, bool deploysSuccessfullyToStaging, TestStatus smokeTestStatus) {
            _deploysSuccessfully = deploysSuccessfully;
            _testStatus = testStatus;
            _deploysSuccessfullyToStaging = deploysSuccessfullyToStaging;
            _smokeTestStatus = smokeTestStatus;
        }

        public bool HasTests() {
            return _testStatus != TestStatus.NoTests;
        }

        public String RunTests() {
            return _testStatus == TestStatus.PassingTests ? "success" : "failure";
        }

        public String Deploy() {
            return Deploy(DeploymentEnvironment.Production);
        }

        public String Deploy(DeploymentEnvironment environment) {
            switch (environment) {
                case DeploymentEnvironment.Staging:
                    return _deploysSuccessfullyToStaging ? "success" : "failure";
                case DeploymentEnvironment.Production:
                    return _deploysSuccessfully ? "success" : "failure";
                default:
                    return "failure";
            }
        }

        public TestStatus RunSmokeTests() {
            return _smokeTestStatus;
        }

        public class ProjectBuilder {
            private bool _deploysSuccessfully;
            private TestStatus _testStatus;
            private bool _deploysSuccessfullyToStaging = false;
            private TestStatus _smokeTestStatus = TestStatus.NoTests;

            public ProjectBuilder SetTestStatus(TestStatus testStatus) {
                _testStatus = testStatus;
                return this;
            }

            public ProjectBuilder SetSmokeTestStatus(TestStatus smokeTestStatus) {
                _smokeTestStatus = smokeTestStatus;
                return this;
            }

            public ProjectBuilder SetDeploysSuccessfully(bool deploysSuccessfully) {
                _deploysSuccessfully = deploysSuccessfully;
                return this;
            }

            public ProjectBuilder SetDeploysSuccessfullyToStaging(bool deploysSuccessfully) {
                _deploysSuccessfullyToStaging = deploysSuccessfully;
                return this;
            }

            public Project Build() {
                return new Project(_deploysSuccessfully, _testStatus, _deploysSuccessfullyToStaging, _smokeTestStatus);
            }
        }
    }
}