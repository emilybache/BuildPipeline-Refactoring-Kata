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
        private bool _buildsSuccessfully;
        private TestStatus _testStatus;

        public static ProjectBuilder Builder() {
            return new ProjectBuilder();
        }

        private Project(bool buildsSuccessfully, TestStatus testStatus) {
            this._buildsSuccessfully = buildsSuccessfully;
            this._testStatus = testStatus;
        }

        public bool HasTests() {
            return _testStatus != TestStatus.NoTests;
        }

        public string RunTests() {
            return _testStatus == TestStatus.PassingTests ? "success" : "failure";
        }

        public string Deploy() {
            return _buildsSuccessfully ? "success" : "failure";
        }

        public class ProjectBuilder {
            public bool BuildsSuccessfully { get; set; }
            public TestStatus TestStatus { get; set; }

            public Project Build() {
                return new Project(BuildsSuccessfully, TestStatus);
            }

            public ProjectBuilder SetTestStatus(TestStatus status)
            {
                this.TestStatus = status;
                return this;
            }

            public ProjectBuilder SetDeploysSuccessfully(bool deploys)
            {
                this.BuildsSuccessfully = deploys;
                return this;
            }
        }
    }
}