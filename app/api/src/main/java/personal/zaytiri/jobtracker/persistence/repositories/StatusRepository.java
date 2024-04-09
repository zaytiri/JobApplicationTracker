package personal.zaytiri.jobtracker.persistence.repositories;

import jakarta.inject.Inject;
import personal.zaytiri.jobtracker.persistence.models.StatusModel;
import personal.zaytiri.jobtracker.persistence.repositories.base.Repository;
import personal.zaytiri.jobtracker.persistence.repositories.interfaces.IStatusRepository;

public class StatusRepository extends Repository<StatusModel> implements IStatusRepository {

}
