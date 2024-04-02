package personal.zaytiri.jobtracker.api.database.operations;

import personal.zaytiri.jobtracker.api.database.IDatabaseOperation;
import personal.zaytiri.jobtracker.api.database.requests.FindByTextOperationRequest;
import personal.zaytiri.jobtracker.api.database.requests.GetOperationRequest;
import personal.zaytiri.jobtracker.persistence.models.base.Model;
import personal.zaytiri.jobtracker.persistence.repositories.interfaces.IRepository;
import personal.zaytiri.makeitexplicitlyqueryable.pairs.Pair;
import personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.query.enums.Operators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindByTextOperation<M extends Model> implements IDatabaseOperation<M, FindByTextOperationRequest<M>, List<Map<String, String>>> {
    private IRepository<M> repository;
    @Override
    public void setRepository(IRepository<M> repository) {
        this.repository = repository;
    }

    @Override
    public List<Map<String, String>> execute(FindByTextOperationRequest<M> request) {
        Map<String, Pair<String, Object>> filters = new HashMap<>();
        filters.put(request.getColumnToFind(), new Pair<>(Operators.LIKE.value, request.getTextToFind()));

        GetOperation<M> getOperation = new GetOperation<>();
        getOperation.setRepository(this.repository);

        GetOperationRequest<M> getRequest = new GetOperationRequest<>();
        getRequest.setModel(request.getModel());
        getRequest.setFilters(filters);

        return getOperation.execute(getRequest);
    }
}
