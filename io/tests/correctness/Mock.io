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

    thenReturn := method(value,
        target updateSlot(methodName, value)
    )
    
)
