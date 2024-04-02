package personal.zaytiri.jobtracker.api.database;

import personal.zaytiri.jobtracker.persistence.models.base.Model;
import personal.zaytiri.jobtracker.persistence.repositories.interfaces.IRepository;

public interface IDatabaseOperation<M extends Model, Req, Res> {

    void setRepository(IRepository<M> repository);

    Res execute(Req request);
}
