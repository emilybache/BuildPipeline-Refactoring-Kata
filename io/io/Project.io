Project := Object clone do(

    buildsSuccessfully := false
    testStatus := nil

    builder := method(
        ProjectBuilder clone
    )

    with := method(buildsSuccessfully, testStatus,
        result := self clone
        result buildsSuccessfully = buildsSuccessfully
        result testStatus = testStatus
        result
    )

    hasTests := method(
        testStatus != TestStatus NO_TESTS
    )

    runTests := method(
        if (testStatus == TestStatus PASSING_TESTS, "success", "failure")
    )

    deploy := method(
        if (buildsSuccessfully, "success", "failure")
    )
)

ProjectBuilder := Object clone do(

    buildsSuccessfully := false
    testStatus := nil

    setTestStatus := method(testStatus,
        self testStatus = testStatus
        self
    )

    setDeploysSuccessfully := method(buildsSuccessfully,
        self buildsSuccessfully = buildsSuccessfully
        self
    )

    build := method(
        Project with(buildsSuccessfully, testStatus)
    )
)
