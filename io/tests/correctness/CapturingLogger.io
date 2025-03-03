doRelativeFile("../../io/Logger.io")

CapturingLogger := Logger clone do(

    init := method(
        self lines := list()
    )

    info := method(message,
        lines append("INFO: " .. message)
    )

    error := method(message,
        lines append("ERROR: " .. message)
    )

    getLoggedLines := method(
        self lines
    )
)
