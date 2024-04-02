package personal.zaytiri.jobtracker.api.domain.entities;

import personal.zaytiri.jobtracker.api.domain.entities.base.Entity;

import java.time.LocalDateTime;

public class JobOffer extends Entity<JobOffer> {
    private String company;
    private String role;
    private String companyWebsite;
    private String location;
    private String link;
    private String description;
    private LocalDateTime appliedAt;
    private int statusId;
    private String interviewNotes;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCompanyWebsite() {
        return companyWebsite;
    }

    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getInterviewNotes() {
        return interviewNotes;
    }

    public void setInterviewNotes(String interviewNotes) {
        this.interviewNotes = interviewNotes;
    }

    public JobOffer() {}

    @Override
    public String toString() {
        return "JobOffer{" +
                "id=" + id +
                '}';
    }

    @Override
    public JobOffer setId(int id) {
        this.id = id;
        return this;
    }
}
