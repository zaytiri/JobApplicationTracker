package personal.zaytiri.jobtracker.persistence.models;

import personal.zaytiri.jobtracker.persistence.DatabaseShema;
import personal.zaytiri.jobtracker.persistence.models.base.Model;
import personal.zaytiri.makeitexplicitlyqueryable.pairs.Pair;

import java.util.ArrayList;
import java.util.List;

public class SettingsModel extends Model {
    private DatabaseShema schema = DatabaseShema.getINSTANCE();
    private String appliedStatus;
    private String closedStatus;
    public SettingsModel() {
        super(DatabaseShema.getINSTANCE().settingsTableName);
    }

    public String getAppliedStatus() {
        return appliedStatus;
    }

    public void setAppliedStatus(String appliedStatus) {
        this.appliedStatus = appliedStatus;
    }

    public String getClosedStatus() {
        return closedStatus;
    }

    public void setClosedStatus(String closedStatus) {
        this.closedStatus = closedStatus;
    }

    @Override
    public List<Pair<String, Object>> getValues() {
        List<Pair<String, Object>> values = new ArrayList<>();

        values.add(new Pair<>(schema.appliedStatusColumnName, appliedStatus));
        values.add(new Pair<>(schema.closedStatusColumnName, closedStatus));
        values.add(new Pair<>(schema.updatedAtColumnName, updatedAt));
        values.add(new Pair<>(schema.createdAtColumnName, createdAt));

        return values;
    }
}
