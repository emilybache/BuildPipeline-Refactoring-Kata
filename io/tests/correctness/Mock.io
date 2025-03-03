Mock := Object clone do(

    when := method(
        target := _extractTarget(call)
        methodName := call argAt(0) next name
        MockSetup with(target, methodName)
    )

    verify := method(
        target := _extractTarget(call)
        MockVerify with(target)
    )

    verifyNever := method(
        target := _extractTarget(call)
        MockVerifyNever with(target)
    )

    _extractTarget := method(call,
        context := Object clone prependProto(call sender)
        targetName := call argAt(0) name
        if (call sender hasLocalSlot(targetName)) then (
            target := call sender getSlot(targetName)
        ) elseif(call sender hasLocalSlot("self")) then  (
            target := call sender self getSlot(targetName)
        ) else (
            target := nil // not found
        )
        target
    )
)

MockSetup := Object clone do(

    with := method(target, methodName,
        result := self clone
        result target := target
        result methodName := methodName
        result
    )

    //doc MockSetup thenReturn set up a stubbed value for the given when
    thenReturn := method(value,
        body := method(
            value
        )
        target updateSlot(methodName, body)
        self
    )

    //doc MockSetup capture set up recording of calls to given when
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

MockVerify := Object clone do(

    with := method(target,
        result := self clone
        result target := target
        result
    )

    forward := method(
        methodName := call message name
        expected := call evalArgs
        if (target captured_calls contains(expected) not) then (
            UnitTest fail("expected call to \"#{methodName}\" with #{#{expected} but had #{target captured_calls}" interpolate)
        )
    )
)

MockVerifyNever := Object clone do(

    with := method(target,
        result := self clone
        result target := target
        result
    )

    forward := method(
        methodName := call message name
        if (target captured_calls size > 0) then (
            UnitTest fail("expected no calls to \"#{methodName}\" but had #{target captured_calls}" interpolate)
        )
    )
)
