package personal.zaytiri.jobtracker.api.domain.entities;

import personal.zaytiri.jobtracker.api.domain.entities.base.IStorageOperations;
import personal.zaytiri.jobtracker.persistence.models.base.Model;
import personal.zaytiri.jobtracker.persistence.repositories.interfaces.IRepository;
import personal.zaytiri.makeitexplicitlyqueryable.pairs.Pair;

import java.util.List;
import java.util.Map;

public class StorageOperations<M extends Model> implements IStorageOperations<M> {

    public void setRepository(IRepository<M> repository) {
        this.repository = repository;
    }

    private IRepository<M> repository;

    @Override
    public boolean create(M model) {
        return repository.create(model).isSuccess();
    }

    @Override
    public boolean delete(M model) {
        return repository.delete(model).isSuccess();
    }

    @Override
    public List<Map<String, String>> get(M model, Map<String, Pair<String, Object>> filters, Pair<String, String> orderByColumn) {
        return repository.read(model, filters, orderByColumn).getResult();
    }

    @Override
    public boolean update(M model) {
        return repository.update(model).isSuccess();
    }
}
