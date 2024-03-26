package personal.zaytiri.jobtracker.api.mappers;

import personal.zaytiri.jobtracker.api.domain.entities.JobOffer;
import personal.zaytiri.jobtracker.api.mappers.base.Mapper;
import personal.zaytiri.jobtracker.persistence.DatabaseShema;
import personal.zaytiri.jobtracker.persistence.models.JobOfferModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@org.mapstruct.Mapper
public abstract class JobOfferMapper extends Mapper<JobOffer, JobOfferModel> {

    @Override
    public List<JobOffer> toEntity(List<Map<String, String>> rows, boolean mixedResult) {
        List<JobOffer> jobOffers = new ArrayList<>();
        var schema = DatabaseShema.getINSTANCE();

        for (Map<String, String> row : rows) {
            JobOffer jobOffer = new JobOffer().getInstance();

            jobOffer.setId(getRowIntValue(row, mixedResult, schema.idColumnName));
            jobOffer.setCompany(getRowStringValue(row, mixedResult, schema.companyColumnName));
            jobOffer.setRole(getRowStringValue(row, mixedResult, schema.roleColumnName));
            jobOffer.setCompanyWebsite(getRowStringValue(row, mixedResult, schema.companyWebsiteColumnName));
            jobOffer.setLink(getRowStringValue(row, mixedResult, schema.linkColumnName));
            jobOffer.setDescription(getRowStringValue(row, mixedResult, schema.descriptionColumnName));
            jobOffer.setLocation(getRowStringValue(row, mixedResult, schema.locationColumnName));
            jobOffer.setAppliedAt(getRowDateValue(row, mixedResult, schema.appliedAtColumnName));
            jobOffer.setStatusId(getRowIntValue(row, mixedResult, schema.statusIdColumnName));
            jobOffer.setInterviewNotes(getRowStringValue(row, mixedResult, schema.interviewNotesColumnName));

            jobOffers.add(jobOffer);
        }
        return jobOffers;
    }
    public abstract JobOfferModel entityToModel(JobOffer entity);
    public abstract JobOffer modelToEntity(JobOfferModel model);

    protected String getTablePrefix(){
        return "job_offer__";
    }
}
