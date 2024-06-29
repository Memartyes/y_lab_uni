package ru.domain.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WorkspaceConfigTest {

    @Test
    public void testWorkspaceConfigValues() {
        assertThat(WorkspaceConfig.BOOKING_DURATION_HOURS.getValue()).isEqualTo(1);
        assertThat(WorkspaceConfig.WORKSPACES_CAPACITY.getValue()).isEqualTo(8);
        assertThat(WorkspaceConfig.START_HOUR.getValue()).isEqualTo(8);
        assertThat(WorkspaceConfig.END_HOUR.getValue()).isEqualTo(16);
        assertThat(WorkspaceConfig.WORK_DAYS.getDays()).containsExactly("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY");
    }
}