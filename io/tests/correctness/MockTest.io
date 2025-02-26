doRelativeFile("../../io/Config.io")
doRelativeFile("../../io/Emailer.io")

MockTest := UnitTest clone do(

    test_stub_field := method(
        self config := Config clone
        Mock when(config sendEmailSummary) thenReturn(true)
        assertEquals(true, config sendEmailSummary)
    )

    test_stub_local := method(
        config := Config clone
        Mock when(config sendEmailSummary) thenReturn(false)
        assertEquals(false, config sendEmailSummary)
    )

    test_capture_internal := method(
        emailer := Emailer clone
        Mock when(emailer send) capture
        assertEquals(0, emailer captured_calls size)

        emailer send("message")

        assertEquals(1, emailer captured_calls size)
        assertEquals("message", emailer captured_calls at(0) at(0))
    )

    test_capture_verify := method(
        self emailer := Emailer clone
        Mock when(emailer send) capture

        emailer send("Deployment failed")

        Mock verify(emailer) send("Deployment failed")
        assertRaisesException(
            Mock verify(emailer) send("Missing")
        )

        // TODO bring method name into capture
        // assertRaisesException(
        //     Mock verify(emailer) other("Deployment failed")
        // )
    )

    test_capture_verify_never := method(
        self emailer := Emailer clone
        Mock when(emailer send) capture
        Mock verifyNever(emailer) send(whatever)

        emailer send("Deployment failed")

        assertRaisesException(
            Mock verifyNever(emailer) send(whatever)
        )
    )

)
