Pipeline := Object clone do(

    config := nil
    emailer := nil
    log := nil

    with := method(config, emailer, log,
        result := self clone
        result config = config
        result emailer = emailer
        result log = log
        result
    )

    run := method(project,
        testsPassed := false
        deploySuccessful := false

        if (project hasTests,
            if ("success" == project runTests,
                log info("Tests passed")
                testsPassed = true
            ,
                log error("Tests failed")
                testsPassed = false
            )
        ,
            log info("No tests")
            testsPassed = true
        )

        if (testsPassed,
            if ("success" == project deploy,
                log info("Deployment successful")
                deploySuccessful = true
            ,
                log error("Deployment failed")
                deploySuccessful = false
            )
        ,
            deploySuccessful = false
        )

        if (config sendEmailSummary,
            log info("Sending email")
            if (testsPassed,
                if (deploySuccessful,
                    emailer send("Deployment completed successfully")
                ,
                    emailer send("Deployment failed")
                )
            ,
                emailer send("Tests failed")
            )
        ,
            log info("Email disabled")
        )
    )
)
