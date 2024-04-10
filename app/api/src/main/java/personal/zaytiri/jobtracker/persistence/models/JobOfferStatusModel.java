package personal.zaytiri.jobtracker.persistence.models;

import personal.zaytiri.jobtracker.persistence.DatabaseShema;
import personal.zaytiri.jobtracker.persistence.models.base.Model;
import personal.zaytiri.makeitexplicitlyqueryable.pairs.Pair;

import java.util.ArrayList;
import java.util.List;

public class JobOfferStatusModel extends Model {
    private DatabaseShema schema = DatabaseShema.getINSTANCE();
    private String jobOfferId;
    private String statusId;
    private String changedAt;

    public String getJobOfferId() {
        return jobOfferId;
    }

    public void setJobOfferId(String jobOfferId) {
        this.jobOfferId = jobOfferId;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(String changedAt) {
        this.changedAt = changedAt;
    }


    public JobOfferStatusModel() {
        super(DatabaseShema.getINSTANCE().jobOfferStatusTableName);
    }

    @Override
    public List<Pair<String, Object>> getValues() {
        List<Pair<String, Object>> values = new ArrayList<>();

        values.add(new Pair<>(schema.jobOfferIdColumnName, jobOfferId));
        values.add(new Pair<>(schema.statusIdColumnName, statusId));
        values.add(new Pair<>(schema.changedAtColumnName, changedAt));
        values.add(new Pair<>(schema.updatedAtColumnName, updatedAt));
        values.add(new Pair<>(schema.createdAtColumnName, createdAt));

        return values;
    }
}
