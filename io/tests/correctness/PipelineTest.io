doRelativeFile("../../io/Config.io")
doRelativeFile("../../io/Emailer.io")
doRelativeFile("../../io/Project.io")
doRelativeFile("../../io/TestStatus.io")
doRelativeFile("../../io/Pipeline.io")

PipelineTest := UnitTest clone do(

    setUp := method(
        self config := Config clone
        self log := CapturingLogger clone
        self emailer := Emailer clone
        Mock when(emailer send) thenReturn("") // TODO later

        self pipeline := Pipeline with(config, emailer, log)
    )

    test_project_with_tests_that_deploys_successfully_with_email_notification := method(
        Mock when(config sendEmailSummary) thenReturn(true)

        project := Project builder \
                 setTestStatus(TestStatus PASSING_TESTS) \
                 setDeploysSuccessfully(true) \
                 build

        pipeline run(project)

        assertEquals(list(
                "INFO: Tests passed",
                "INFO: Deployment successful",
                "INFO: Sending email"
        ), log getLoggedLines)

        # verify(emailer) send("Deployment completed successfully")
    )

    test_project_with_tests_that_deploys_successfully_without_email_notification := method(
        Mock when(config sendEmailSummary) thenReturn(false)

        project := Project builder \
                 setTestStatus(TestStatus PASSING_TESTS) \
                 setDeploysSuccessfully(true) \
                 build

        pipeline run(project)

        assertEquals(list(
                "INFO: Tests passed",
                "INFO: Deployment successful",
                "INFO: Email disabled"
        ), log getLoggedLines)

        # verify(emailer, never) send(any)
    )

    test_project_without_tests_that_deploys_successfully_with_email_notification := method(
        Mock when(config sendEmailSummary) thenReturn(true)

        project := Project builder \
                 setTestStatus(TestStatus NO_TESTS) \
                 setDeploysSuccessfully(true) \
                 build

        pipeline run(project)

        assertEquals(list(
                "INFO: No tests",
                "INFO: Deployment successful",
                "INFO: Sending email"
        ), log getLoggedLines)

        # verify(emailer) send("Deployment completed successfully")
    )

    test_project_without_tests_that_deploys_successfully_without_email_notification := method(
        Mock when(config sendEmailSummary) thenReturn(false)

        project := Project builder \
                 setTestStatus(TestStatus NO_TESTS) \
                 setDeploysSuccessfully(true) \
                 build

        pipeline run(project)

        assertEquals(list(
                "INFO: No tests",
                "INFO: Deployment successful",
                "INFO: Email disabled"
        ), log getLoggedLines)

        # verify(emailer, never) send(any)
    )

    test_project_with_tests_that_fail_with_email_notification := method(
        Mock when(config sendEmailSummary) thenReturn(true)

        project := Project builder \
                 setTestStatus(TestStatus FAILING_TESTS) \
                 build

        pipeline run(project)

        assertEquals(list(
                "ERROR: Tests failed",
                "INFO: Sending email"
        ), log getLoggedLines)

        # verify(emailer) send("Tests failed")
    )

    test_project_with_tests_that_fail_without_email_notification := method(
        Mock when(config sendEmailSummary) thenReturn(false)

        project := Project builder \
                 setTestStatus(TestStatus FAILING_TESTS) \
                 build

        pipeline run(project)

        assertEquals(list(
                "ERROR: Tests failed",
                "INFO: Email disabled"
        ), log getLoggedLines)

        # verify(emailer, never) send(any)
    )

    test_project_with_tests_and_failing_build_with_email_notification := method(
        Mock when(config sendEmailSummary) thenReturn(true)

        project := Project builder \
                 setTestStatus(TestStatus PASSING_TESTS) \
                 setDeploysSuccessfully(false) \
                 build

        pipeline run(project)

        assertEquals(list(
                "INFO: Tests passed",
                "ERROR: Deployment failed",
                "INFO: Sending email"
        ), log getLoggedLines)

        # verify(emailer) send("Deployment failed")
    )

    test_project_with_tests_and_failing_build_without_email_notification := method(
        Mock when(config sendEmailSummary) thenReturn(false)

        project := Project builder \
                 setTestStatus(TestStatus PASSING_TESTS) \
                 setDeploysSuccessfully(false) \
                 build

        pipeline run(project)

        assertEquals(list(
                "INFO: Tests passed",
                "ERROR: Deployment failed",
                "INFO: Email disabled"
        ), log getLoggedLines)

        # verify(emailer, never) send(any)
    )

    test_project_without_tests_and_failing_build_with_email_notification := method(
        Mock when(config sendEmailSummary) thenReturn(true)

        project := Project builder \
                 setTestStatus(TestStatus NO_TESTS) \
                 setDeploysSuccessfully(false) \
                 build

        pipeline run(project)

        assertEquals(list(
                "INFO: No tests",
                "ERROR: Deployment failed",
                "INFO: Sending email"
        ), log getLoggedLines)

        # verify(emailer) send("Deployment failed")
    )

    test_project_without_tests_and_failing_build_without_email_notification := method(
        Mock when(config sendEmailSummary) thenReturn(false)

        project := Project builder \
                 setTestStatus(TestStatus NO_TESTS) \
                 setDeploysSuccessfully(false) \
                 build

        pipeline run(project)

        assertEquals(list(
                "INFO: No tests",
                "ERROR: Deployment failed",
                "INFO: Email disabled"
        ), log getLoggedLines)

        # verify(emailer, never) send(any)
    )
)
