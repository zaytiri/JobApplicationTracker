package personal.zaytiri.jobtracker.api.mappers;

import personal.zaytiri.jobtracker.api.domain.entities.Settings;
import personal.zaytiri.jobtracker.api.domain.entities.Status;
import personal.zaytiri.jobtracker.api.mappers.base.Mapper;
import personal.zaytiri.jobtracker.persistence.DatabaseShema;
import personal.zaytiri.jobtracker.persistence.models.SettingsModel;
import personal.zaytiri.jobtracker.persistence.models.StatusModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@org.mapstruct.Mapper
public abstract class SettingsMapper extends Mapper<Settings, SettingsModel> {

    @Override
    public List<Settings> toEntity(List<Map<String, String>> rows, boolean mixedResult) {
        List<Settings> statuses = new ArrayList<>();
        var schema = DatabaseShema.getINSTANCE();

        for (Map<String, String> row : rows) {
            Settings settings = new Settings();

            settings.setId(getRowIntValue(row, mixedResult, schema.idColumnName));
            settings.setAppliedStatus(getRowIntValue(row, mixedResult, schema.appliedStatusColumnName));
            settings.setClosedStatus(getRowIntValue(row, mixedResult, schema.closedStatusColumnName));

            statuses.add(settings);
        }
        return statuses;
    }
    public abstract SettingsModel entityToModel(Settings entity);
    public abstract Settings modelToEntity(SettingsModel model);

    protected String getTablePrefix(){
        return "settings__";
    }
}
