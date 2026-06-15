from approvaltests import verify
from build_pipeline.pipeline import Pipeline
from build_pipeline.project import Project
from build_pipeline.status import PASSING_TESTS, NO_TESTS, FAILING_TESTS

class ConfigForTest:
    def __init__(self, send_email):
        self.send_email = send_email

    def send_email_summary(self):
        return self.send_email

class SpyEmailer:
    def __init__(self, output):
        self.output = output

    def send(self, message):
        self.output.append(f"Email message: {message}")

class SpyLogger:
    def __init__(self, output):
        self.output = output

    def info(self, message):
        self.output.append(f"INFO: {message}")

    def error(self, message):
        self.output.append(f"ERROR: {message}")

def run_pipeline(test_status, send_email, deploys_successfully):
    output = []
    config = ConfigForTest(send_email)
    emailer = SpyEmailer(output)
    log = SpyLogger(output)
    pipeline = Pipeline(config, emailer, log)

    project = Project.builder() \
        .set_test_status(test_status) \
        .set_deploys_successfully(deploys_successfully) \
        .build()

    pipeline.run(project)

    return "\n".join(output) + "\n"


def test_passing_tests_email_enabled_deployment_successful():
    output = run_pipeline(PASSING_TESTS, True, True)
    verify(output)

def test_passing_tests_email_enabled_deployment_failed():
    output = run_pipeline(PASSING_TESTS, True, False)
    verify(output)

def test_passing_tests_email_disabled_deployment_successful():
    output = run_pipeline(PASSING_TESTS, False, True)
    verify(output)

def test_no_tests_email_enabled_deployment_successful():
    output = run_pipeline(NO_TESTS, True, True)
    verify(output)

def test_no_tests_email_enabled_deployment_failed():
    output = run_pipeline(NO_TESTS, True, False)
    verify(output)

def test_failing_tests_email_enabled_deployment_successful():
    output = run_pipeline(FAILING_TESTS, True, True)
    verify(output)
