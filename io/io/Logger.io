Logger := Object clone do(
    info := method(message,
        Exception raise("not implemented")
    )
    error := method(message,
        Exception raise("not implemented")
    )
)
