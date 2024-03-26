package personal.zaytiri.jobtracker.persistence.repositories;

import jakarta.inject.Inject;
import personal.zaytiri.jobtracker.persistence.models.JobOfferModel;
import personal.zaytiri.jobtracker.persistence.repositories.base.Repository;
import personal.zaytiri.jobtracker.persistence.repositories.interfaces.IJobOfferRepository;

public class JobOfferRepository extends Repository<JobOfferModel> implements IJobOfferRepository {
    @Inject
    public JobOfferRepository() {
    }
}
