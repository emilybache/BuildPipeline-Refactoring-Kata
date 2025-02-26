Logger := Object clone do(
    info := method(message,
        Error raise("not implemented")
    )
    error := method(message,
        Error raise("not implemented")
    )
)
