package personal.zaytiri.jobtracker.api.database.operations;

import personal.zaytiri.jobtracker.api.database.IDatabaseOperation;
import personal.zaytiri.jobtracker.api.database.requests.CreateOperationResponse;
import personal.zaytiri.jobtracker.persistence.models.base.Model;
import personal.zaytiri.jobtracker.persistence.repositories.interfaces.IRepository;

public class CreateOperation<M extends Model> implements IDatabaseOperation<M, M, CreateOperationResponse> {
    private IRepository<M> repository;
    @Override
    public void setRepository(IRepository<M> repository) {
        this.repository = repository;
    }

    @Override
    public CreateOperationResponse execute(M request) {
        var repoResponse = repository.create(request);

        var responseToReturn = new CreateOperationResponse();
        responseToReturn.setCreated(repoResponse.isSuccess());
        responseToReturn.setIdCreated(repoResponse.getLastInsertedId());

        return responseToReturn;
    }
}
