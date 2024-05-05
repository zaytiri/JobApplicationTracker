package personal.zaytiri.jobtracker.api.mappers;

import personal.zaytiri.jobtracker.api.domain.entities.JobOfferStatus;
import personal.zaytiri.jobtracker.api.domain.entities.Status;
import personal.zaytiri.jobtracker.api.mappers.base.Mapper;
import personal.zaytiri.jobtracker.persistence.DatabaseShema;
import personal.zaytiri.jobtracker.persistence.models.JobOfferStatusModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@org.mapstruct.Mapper
public abstract class JobOfferStatusMapper extends Mapper<JobOfferStatus, JobOfferStatusModel> {

    @Override
    public List<JobOfferStatus> toEntity(List<Map<String, String>> rows, boolean mixedResult) {
        List<JobOfferStatus> statuses = new ArrayList<>();
        var schema = DatabaseShema.getINSTANCE();

        for (Map<String, String> row : rows) {
            JobOfferStatus status = new JobOfferStatus();

            status.setId(getRowIntValue(row, mixedResult, schema.idColumnName));
            status.setJobOfferId(getRowIntValue(row, mixedResult, schema.jobOfferIdColumnName));
            status.setStatusId(getRowIntValue(row, mixedResult, schema.statusIdColumnName));
            status.setChangedAt(getRowDateValue(row, mixedResult, schema.changedAtColumnName));

            statuses.add(status);
        }
        return statuses;
    }
    public abstract JobOfferStatusModel entityToModel(JobOfferStatus entity);
    public abstract JobOfferStatus modelToEntity(JobOfferStatusModel model);

    protected String getTablePrefix(){
        return "job_offer_status__";
    }
}
