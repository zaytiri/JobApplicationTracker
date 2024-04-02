package personal.zaytiri.jobtracker.api.database.operations;

import personal.zaytiri.jobtracker.api.database.IDatabaseOperation;
import personal.zaytiri.jobtracker.api.database.requests.GetOperationRequest;
import personal.zaytiri.jobtracker.persistence.models.base.Model;
import personal.zaytiri.jobtracker.persistence.repositories.interfaces.IRepository;

import java.util.List;
import java.util.Map;

public class GetOperation<M extends Model> implements IDatabaseOperation<M, GetOperationRequest<M>, List<Map<String, String>>> {
    private IRepository<M> repository;
    @Override
    public void setRepository(IRepository<M> repository) {
        this.repository = repository;
    }

    @Override
    public List<Map<String, String>> execute(GetOperationRequest<M> request) {
        return this.repository.read(request.getModel(), request.getFilters(), request.getOrderByColumn()).getResult();
    }
}

