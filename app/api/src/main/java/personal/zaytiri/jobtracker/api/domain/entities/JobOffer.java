package personal.zaytiri.jobtracker.api.domain.entities;

import personal.zaytiri.jobtracker.api.database.operations.GetOperation;
import personal.zaytiri.jobtracker.api.database.requests.GetOperationRequest;
import personal.zaytiri.jobtracker.api.domain.entities.base.Entity;
import personal.zaytiri.jobtracker.api.mappers.JobOfferMapperImpl;
import personal.zaytiri.jobtracker.api.mappers.JobOfferStatusMapperImpl;
import personal.zaytiri.jobtracker.persistence.DatabaseShema;
import personal.zaytiri.jobtracker.persistence.models.JobOfferModel;
import personal.zaytiri.jobtracker.persistence.models.JobOfferStatusModel;
import personal.zaytiri.jobtracker.persistence.repositories.base.Repository;
import personal.zaytiri.makeitexplicitlyqueryable.pairs.Pair;
import personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.query.enums.Operators;
import personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.query.enums.Order;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobOffer extends Entity<JobOffer> {
    private String company;
    private String role;
    private String companyWebsite;
    private String location;
    private String link;
    private String description;
    private int statusId;
    private LocalDateTime appliedAt;
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

    public int getStatusId(boolean fromDb) {
        if(!fromDb){
            return getStatusId();
        }

        Map<String, Pair<String, Object>> filters = new HashMap<>();
        filters.put(DatabaseShema.getINSTANCE().jobOfferIdColumnName, new Pair<>(Operators.EQUALS.value, id));

        GetOperation<JobOfferStatusModel> getOperation = new GetOperation<>();
        getOperation.setRepository(new Repository<>());

        GetOperationRequest<JobOfferStatusModel> getOperationRequest = new GetOperationRequest<>();
        getOperationRequest.setModel(new JobOfferStatusModel());
        getOperationRequest.setFilters(filters);
        getOperationRequest.setOrderByColumn(new Pair<>(Order.DESCENDING.value, DatabaseShema.getINSTANCE().changedAtColumnName));

        List<JobOfferStatus> results = new JobOfferStatusMapperImpl().toEntity(getOperation.execute(getOperationRequest), false);

        int statusId = 0;
        if(!results.isEmpty()){
            statusId = results.get(0).getStatusId();
        }

        setStatusId(statusId);
        return statusId;
    }

    public int getStatusId() {
        if(id != 0 && statusId == 0){
            getStatusId(true);
        }

        return statusId;
    }

    public void setStatusId(int statusId){
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
