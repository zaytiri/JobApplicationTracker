package personal.zaytiri.jobtracker.api.domain.entities;

import jakarta.inject.Inject;
import personal.zaytiri.jobtracker.api.domain.entities.base.Entity;
import personal.zaytiri.jobtracker.api.domain.entities.base.IStorageOperations;
import personal.zaytiri.jobtracker.api.mappers.JobOfferMapperImpl;
import personal.zaytiri.jobtracker.dependencyinjection.AppComponent;
import personal.zaytiri.jobtracker.api.mappers.JobOfferMapper;
import personal.zaytiri.jobtracker.persistence.DatabaseShema;
import personal.zaytiri.jobtracker.persistence.repositories.interfaces.IJobOfferRepository;
import personal.zaytiri.makeitexplicitlyqueryable.pairs.Pair;
import personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.query.enums.Operators;
import personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.response.Response;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobOffer extends Entity<JobOffer, IJobOfferRepository, JobOfferMapper> implements IStorageOperations<JobOffer> {
    private IJobOfferRepository repository;
    private String company;
    private String role;
    private String companyWebsite;
    private String location;
    private String link;
    private String description;
    private LocalDate appliedAt;
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

    public LocalDate getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(LocalDate appliedAt) {
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

    @Inject
    public JobOffer(IJobOfferRepository repository) {
        this.repository = repository;
        this.mapper = new JobOfferMapperImpl();
    }

    @Override
    public JobOffer setId(int id) {
        this.id = id;
        return this;
    }

    @Override
    protected JobOffer getInjectedComponent(AppComponent component) {
        return component.getJobOffer();
    }

    @Override
    public boolean create() {
        if (exists()) {
            return false;
        }

        Response response = repository.create(mapper.entityToModel(this));

        this.id = response.getLastInsertedId();

        return response.isSuccess();
    }

    @Override
    public boolean delete() {
        Response response = repository.delete(mapper.entityToModel(this));
        if (get() != null) {
            return false;
        }
        return response.isSuccess();
    }

    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public List<JobOffer> get(Map<String, Pair<String, Object>> filters, Pair<String, String> orderByColumn) {
        Response response = repository.read(mapper.entityToModel(this), filters, orderByColumn);

        return mapper.toEntity(response.getResult(), false);
    }

    @Override
    public JobOffer get() {
        Map<String, Pair<String, Object>> filters = new HashMap<>();
        filters.put(DatabaseShema.getINSTANCE().idColumnName, new Pair<>(Operators.EQUALS.value, this.id));

        List<JobOffer> results = get(filters, null);
        if (results.isEmpty()) {
            return null;
        }

        return results.get(0);
    }

    @Override
    public boolean update() {
        return repository.update(mapper.entityToModel(this)).isSuccess();
    }

}
