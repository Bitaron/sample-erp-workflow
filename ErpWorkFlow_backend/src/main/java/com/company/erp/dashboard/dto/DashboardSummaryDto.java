package com.company.erp.dashboard.dto;

public class DashboardSummaryDto {
    private Long totalInProgress;
    private Long totalCompleted;
    private Long myPendingActions;
    private Long myCompletedActions;

    public DashboardSummaryDto() {}

    public DashboardSummaryDto(Long totalInProgress, Long totalCompleted, Long myPendingActions, Long myCompletedActions) {
        this.totalInProgress = totalInProgress;
        this.totalCompleted = totalCompleted;
        this.myPendingActions = myPendingActions;
        this.myCompletedActions = myCompletedActions;
    }

    public Long getTotalInProgress() { return totalInProgress; }
    public void setTotalInProgress(Long totalInProgress) { this.totalInProgress = totalInProgress; }
    public Long getTotalCompleted() { return totalCompleted; }
    public void setTotalCompleted(Long totalCompleted) { this.totalCompleted = totalCompleted; }
    public Long getMyPendingActions() { return myPendingActions; }
    public void setMyPendingActions(Long myPendingActions) { this.myPendingActions = myPendingActions; }
    public Long getMyCompletedActions() { return myCompletedActions; }
    public void setMyCompletedActions(Long myCompletedActions) { this.myCompletedActions = myCompletedActions; }
}

