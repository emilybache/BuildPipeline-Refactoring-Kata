Mock := Object clone do(

    when := method(
        context := Object clone prependProto(call sender)
        targetName := call argAt(0) name
        if (call sender hasLocalSlot(targetName)) then (
            target := call sender getSlot(targetName)
        ) elseif(call sender hasLocalSlot("self")) then  (
            target := call sender self getSlot(targetName)
        ) else (
            target := nil // not found
        )
        methodName := call argAt(0) next name
        with(target, methodName)
    )

    with := method(target, methodName,
        result := self clone
        result target := target
        result methodName := methodName
        result
    )

    //doc Mock thenReturn set up a stubbed value for the given when
    thenReturn := method(value,
        body := method(
            value
        )
        target updateSlot(methodName, body)
        self
    )

    //doc Mock capture set up recording of calls to given when
    capture := method(
        target captured_calls := list()
        body := method(
            captured_calls append(call evalArgs)
            nil
        )
        target updateSlot(methodName, getSlot("body"))
        self
    )

    captureAndReturn := method(value,
        target captured_calls := list()
        body := method(
            captured_calls append(call evalArgs)
            value
        )
        target updateSlot(methodName, getSlot("body"))
        self
    )

)
