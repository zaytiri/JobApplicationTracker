package personal.zaytiri.jobtracker.persistence.models;

import personal.zaytiri.jobtracker.persistence.DatabaseShema;
import personal.zaytiri.jobtracker.persistence.models.base.Model;
import personal.zaytiri.makeitexplicitlyqueryable.pairs.Pair;

import java.util.ArrayList;
import java.util.List;

public class JobOfferModel extends Model {
    private DatabaseShema schema = DatabaseShema.getINSTANCE();
    private String company;
    private String role;
    private String companyWebsite;
    private String location;
    private String link;
    private String description;
    private String appliedAt;
    private String statusId;
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

    public String getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(String appliedAt) {
        this.appliedAt = appliedAt;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getInterviewNotes() {
        return interviewNotes;
    }

    public void setInterviewNotes(String interviewNotes) {
        this.interviewNotes = interviewNotes;
    }

    public JobOfferModel() {
        super(DatabaseShema.getINSTANCE().jobOfferTableName);
    }

    @Override
    public List<Pair<String, Object>> getValues() {
        List<Pair<String, Object>> values = new ArrayList<>();

        values.add(new Pair<>(schema.companyColumnName, company));
        values.add(new Pair<>(schema.roleColumnName, role));
        values.add(new Pair<>(schema.companyWebsiteColumnName, companyWebsite));
        values.add(new Pair<>(schema.locationColumnName, location));
        values.add(new Pair<>(schema.linkColumnName, link));
        values.add(new Pair<>(schema.descriptionColumnName, description));
        values.add(new Pair<>(schema.appliedAtColumnName, appliedAt));
        values.add(new Pair<>(schema.statusIdColumnName, statusId));
        values.add(new Pair<>(schema.interviewNotesColumnName, interviewNotes));
        values.add(new Pair<>(schema.updatedAtColumnName, updatedAt));
        values.add(new Pair<>(schema.createdAtColumnName, createdAt));

        return values;
    }
}
