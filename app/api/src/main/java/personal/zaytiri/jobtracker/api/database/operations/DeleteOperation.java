package personal.zaytiri.jobtracker.api.database.operations;

import personal.zaytiri.jobtracker.api.database.IDatabaseOperation;
import personal.zaytiri.jobtracker.persistence.models.base.Model;
import personal.zaytiri.jobtracker.persistence.repositories.interfaces.IRepository;

public class DeleteOperation<M extends Model> implements IDatabaseOperation<M, M, Boolean> {
    private IRepository<M> repository;
    @Override
    public void setRepository(IRepository<M> repository) {
        this.repository = repository;
    }

    @Override
    public Boolean execute(M request) {
        return repository.delete(request).isSuccess();
    }
}
