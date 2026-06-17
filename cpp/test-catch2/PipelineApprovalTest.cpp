#include "ApprovalTests.hpp"
#include "catch2/catch.hpp"
#include "Pipeline.h"
#include <sstream>

using namespace std;

namespace {
    class SpyEmailer : public Emailer {
        stringstream &spy;
    public:
        explicit SpyEmailer(stringstream &spy) : spy(spy) {}
        void send(const string &message) override {
            spy << "Email message: " << message << "\n";
        }
    };

    class SpyLogger : public Logger {
        stringstream &spy;
    public:
        explicit SpyLogger(stringstream &spy) : spy(spy) {}
        void info(const string &message) override {
            spy << "INFO: " << message << "\n";
        }
        void error(const string &message) override {
            spy << "ERROR: " << message << "\n";
        }
    };

    string doPipelineRun(TestStatus testStatus, bool sendSummary, bool buildsSuccessfully) {
        stringstream spy;
        Config config(sendSummary);
        SpyEmailer emailer(spy);
        SpyLogger log(spy);
        Pipeline pipeline(config, emailer, log);

        Project project(buildsSuccessfully, testStatus);
        pipeline.run(project);

        return spy.str();
    }
}

TEST_CASE("PipelineRun") {
    SECTION("PassingTests_EmailEnabled_DeploymentSuccessful") {
        auto output = doPipelineRun(PASSING_TESTS, true, true);
        ApprovalTests::Approvals::verify(output);
    }

    SECTION("PassingTests_EmailEnabled_DeploymentFailed") {
        auto output = doPipelineRun(PASSING_TESTS, true, false);
        ApprovalTests::Approvals::verify(output);
    }

    SECTION("PassingTests_EmailDisabled_DeploymentSuccessful") {
        auto output = doPipelineRun(PASSING_TESTS, false, true);
        ApprovalTests::Approvals::verify(output);
    }

    SECTION("NoTests_EmailEnabled_DeploymentSuccessful") {
        auto output = doPipelineRun(NO_TESTS, true, true);
        ApprovalTests::Approvals::verify(output);
    }

    SECTION("NoTests_EmailEnabled_DeploymentFailed") {
        auto output = doPipelineRun(NO_TESTS, true, false);
        ApprovalTests::Approvals::verify(output);
    }

    SECTION("FailingTests_EmailEnabled_DeploymentSuccessful") {
        auto output = doPipelineRun(FAILING_TESTS, true, true);
        ApprovalTests::Approvals::verify(output);
    }
}
