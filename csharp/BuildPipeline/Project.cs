using System;

namespace UntangledConditionals
{
    public enum TestStatus {
        NoTests,
        PassingTests,
        FailingTests
    }
    public class Project
    {
        public bool BuildsSuccessfully { get; }
        public TestStatus TestStatus { get; }

        public static ProjectBuilder Builder() {
            return new ProjectBuilder();
        }

        private Project(bool buildsSuccessfully, TestStatus testStatus) {
            this.BuildsSuccessfully = buildsSuccessfully;
            this.TestStatus = testStatus;
        }

        public bool HasTests() {
            return TestStatus != TestStatus.NoTests;
        }

        public string RunTests() {
            return TestStatus == TestStatus.PassingTests ? "success" : "failure";
        }

        public string Deploy() {
            return BuildsSuccessfully ? "success" : "failure";
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