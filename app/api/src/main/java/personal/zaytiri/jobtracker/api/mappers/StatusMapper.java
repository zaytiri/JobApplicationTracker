package personal.zaytiri.jobtracker.api.mappers;

import personal.zaytiri.jobtracker.api.domain.entities.Status;
import personal.zaytiri.jobtracker.api.mappers.base.Mapper;
import personal.zaytiri.jobtracker.persistence.DatabaseShema;
import personal.zaytiri.jobtracker.persistence.models.StatusModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@org.mapstruct.Mapper
public abstract class StatusMapper extends Mapper<Status, StatusModel> {

    @Override
    public List<Status> toEntity(List<Map<String, String>> rows, boolean mixedResult) {
        List<Status> statuses = new ArrayList<>();
        var schema = DatabaseShema.getINSTANCE();

        for (Map<String, String> row : rows) {
            Status status = new Status();

            status.setId(getRowIntValue(row, mixedResult, schema.idColumnName));
            status.setName(getRowStringValue(row, mixedResult, schema.nameColumnName));
            status.setColor(getRowStringValue(row, mixedResult, schema.colorColumnName));

            statuses.add(status);
        }
        return statuses;
    }
    public abstract StatusModel entityToModel(Status entity);
    public abstract Status modelToEntity(StatusModel model);

    protected String getTablePrefix(){
        return "status__";
    }
}
