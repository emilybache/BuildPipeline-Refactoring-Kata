package org.sammancoaching;

import org.sammancoaching.dependencies.Config;
import org.sammancoaching.dependencies.NotificationService;
import org.sammancoaching.dependencies.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.sammancoaching.dependencies.TestStatus.*;

class PipelineTest {
    private Config config = mock(Config.class);
    private CapturingLogger log = new CapturingLogger();
    private NotificationService emailer = mock(EmailService.class);

    private Pipeline pipeline;

    @BeforeEach
    void setUp() {
        pipeline = new Pipeline(config, emailer, log);
    }

    @Test
    void project_with_tests_that_deploys_successfully_with_email_notification() {
        when(config.sendNotificationSummary()).thenReturn(true);

        Project project = Project.builder() //
                .setTestStatus(PASSING_TESTS) //
                .setDeploysSuccessfully(true) //
                .build();

        pipeline.run(project);

        assertEquals(Arrays.asList( //
                "INFO: Tests passed", //
                "INFO: Deployment successful", //
                "INFO: Sending notification" //
        ), log.getLoggedLines());

        verify(emailer).send("Deployment completed successfully");
    }

    @Test
    void project_with_tests_that_deploys_successfully_without_email_notification() {
        when(config.sendNotificationSummary()).thenReturn(false);

        Project project = Project.builder() //
                .setTestStatus(PASSING_TESTS) //
                .setDeploysSuccessfully(true) //
                .build();

        pipeline.run(project);

        assertEquals(Arrays.asList( //
                "INFO: Tests passed", //
                "INFO: Deployment successful", //
                "INFO: Notification disabled" //
        ), log.getLoggedLines());

        verify(emailer, never()).send(any());
    }

    @Test
    void project_without_tests_that_deploys_successfully_with_email_notification() {
        when(config.sendNotificationSummary()).thenReturn(true);

        Project project = Project.builder() //
                .setTestStatus(NO_TESTS) //
                .setDeploysSuccessfully(true) //
                .build();

        pipeline.run(project);

        assertEquals(Arrays.asList( //
                "INFO: No tests", //
                "INFO: Deployment successful", //
                "INFO: Sending notification" //
        ), log.getLoggedLines());

        verify(emailer).send("Deployment completed successfully");
    }

    @Test
    void project_without_tests_that_deploys_successfully_without_email_notification() {
        when(config.sendNotificationSummary()).thenReturn(false);

        Project project = Project.builder() //
                .setTestStatus(NO_TESTS) //
                .setDeploysSuccessfully(true) //
                .build();

        pipeline.run(project);

        assertEquals(Arrays.asList( //
                "INFO: No tests", //
                "INFO: Deployment successful", //
                "INFO: Notification disabled" //
        ), log.getLoggedLines());

        verify(emailer, never()).send(any());
    }

    @Test
    void project_with_tests_that_fail_with_email_notification() {
        when(config.sendNotificationSummary()).thenReturn(true);

        Project project = Project.builder() //
                .setTestStatus(FAILING_TESTS) //
                .build();

        pipeline.run(project);

        assertEquals(Arrays.asList( //
                "ERROR: Tests failed", //
                "INFO: Sending notification" //
        ), log.getLoggedLines());

        verify(emailer).send("Tests failed");
    }

    @Test
    void project_with_tests_that_fail_without_email_notification() {
        when(config.sendNotificationSummary()).thenReturn(false);

        Project project = Project.builder() //
                .setTestStatus(FAILING_TESTS) //
                .build();

        pipeline.run(project);

        assertEquals(Arrays.asList( //
                "ERROR: Tests failed", //
                "INFO: Notification disabled" //
        ), log.getLoggedLines());

        verify(emailer, never()).send(any());
    }

    @Test
    void project_with_tests_and_failing_build_with_email_notification() {
        when(config.sendNotificationSummary()).thenReturn(true);

        Project project = Project.builder() //
                .setTestStatus(PASSING_TESTS) //
                .setDeploysSuccessfully(false) //
                .build();

        pipeline.run(project);

        assertEquals(Arrays.asList( //
                "INFO: Tests passed", //
                "ERROR: Deployment failed", //
                "INFO: Sending notification" //
        ), log.getLoggedLines());

        verify(emailer).send("Deployment failed");
    }

    @Test
    void project_with_tests_and_failing_build_without_email_notification() {
        when(config.sendNotificationSummary()).thenReturn(false);

        Project project = Project.builder() //
                .setTestStatus(PASSING_TESTS) //
                .setDeploysSuccessfully(false) //
                .build();

        pipeline.run(project);

        assertEquals(Arrays.asList( //
                "INFO: Tests passed", //
                "ERROR: Deployment failed", //
                "INFO: Notification disabled" //
        ), log.getLoggedLines());

        verify(emailer, never()).send(any());
    }

    @Test
    void project_without_tests_and_failing_build_with_email_notification() {
        when(config.sendNotificationSummary()).thenReturn(true);

        Project project = Project.builder() //
                .setTestStatus(NO_TESTS) //
                .setDeploysSuccessfully(false) //
                .build();

        pipeline.run(project);

        assertEquals(Arrays.asList( //
                "INFO: No tests", //
                "ERROR: Deployment failed", //
                "INFO: Sending notification" //
        ), log.getLoggedLines());

        verify(emailer).send("Deployment failed");
    }

    @Test
    void project_without_tests_and_failing_build_without_email_notification() {
        when(config.sendNotificationSummary()).thenReturn(false);

        Project project = Project.builder() //
                .setTestStatus(NO_TESTS) //
                .setDeploysSuccessfully(false) //
                .build();

        pipeline.run(project);

        assertEquals(Arrays.asList( //
                "INFO: No tests", //
                "ERROR: Deployment failed", //
                "INFO: Notification disabled" //
        ), log.getLoggedLines());

        verify(emailer, never()).send(any());
    }
}
