package personal.zaytiri.jobtracker.api.database.operations;

import personal.zaytiri.jobtracker.api.database.IDatabaseOperation;
import personal.zaytiri.jobtracker.api.database.requests.GetOperationRequest;
import personal.zaytiri.jobtracker.persistence.DatabaseShema;
import personal.zaytiri.jobtracker.persistence.models.JobOfferStatusModel;
import personal.zaytiri.jobtracker.persistence.models.StatusModel;
import personal.zaytiri.jobtracker.persistence.repositories.interfaces.IRepository;
import personal.zaytiri.makeitexplicitlyqueryable.pairs.Pair;
import personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.query.enums.Operators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetStatusesByJobOfferIdOperation implements IDatabaseOperation<JobOfferStatusModel, Integer, List<Map<String, String>>> {
    private IRepository<JobOfferStatusModel> repository;
    @Override
    public void setRepository(IRepository<JobOfferStatusModel> repository) {
        this.repository = repository;
    }

    @Override
    public List<Map<String, String>> execute(Integer request) {
        Map<String, Pair<String, Object>> filters = new HashMap<>();
        filters.put(DatabaseShema.getINSTANCE().jobOfferIdColumnName, new Pair<>(Operators.EQUALS.value, request));

        GetOperation<JobOfferStatusModel> getOperation = new GetOperation<>();
        getOperation.setRepository(this.repository);

        GetOperationRequest<JobOfferStatusModel> getRequest = new GetOperationRequest<>();
        getRequest.setModel(new JobOfferStatusModel());
        getRequest.setFilters(filters);

        return getOperation.execute(getRequest);
    }
}
