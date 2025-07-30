package com.university.management.dto;

import jakarta.validation.constraints.NotBlank;

public class ApplicationReviewDto {
    
    @NotBlank(message = "Application status is required")
    private String applicationStatus; // "APPROVED" or "REJECTED" - APPLIED applications can be reviewed
    
    private String reviewComments;
    
    private String rejectionReason; // Required if status is REJECTED
    
    // Constructors
    public ApplicationReviewDto() {}
    
    public ApplicationReviewDto(String applicationStatus, String reviewComments) {
        this.applicationStatus = applicationStatus;
        this.reviewComments = reviewComments;
    }
    
    // Getters and Setters
    public String getApplicationStatus() {
        return applicationStatus;
    }
    
    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }
    
    public String getReviewComments() {
        return reviewComments;
    }
    
    public void setReviewComments(String reviewComments) {
        this.reviewComments = reviewComments;
    }
    
    public String getRejectionReason() {
        return rejectionReason;
    }
    
    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}
