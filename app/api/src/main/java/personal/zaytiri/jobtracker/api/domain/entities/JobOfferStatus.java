package personal.zaytiri.jobtracker.api.domain.entities;

import personal.zaytiri.jobtracker.api.domain.entities.base.Entity;

import java.time.LocalDateTime;

public class JobOfferStatus extends Entity<JobOfferStatus> {

    private int jobOfferId;
    private int statusId;
    private LocalDateTime changedAt;

    public int getJobOfferId() {
        return jobOfferId;
    }

    public void setJobOfferId(int jobOfferId) {
        this.jobOfferId = jobOfferId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }

    public JobOfferStatus() {}

    @Override
    public String toString() {
        return "JobOfferStatus{" +
                "id=" + id +
                '}';
    }

    @Override
    public JobOfferStatus setId(int id) {
        this.id = id;
        return this;
    }
}
