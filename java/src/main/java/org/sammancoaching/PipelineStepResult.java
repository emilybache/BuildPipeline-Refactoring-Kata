package org.sammancoaching;

public record PipelineStepResult(String stepName, boolean stepPassed, String failureReason) {
}
