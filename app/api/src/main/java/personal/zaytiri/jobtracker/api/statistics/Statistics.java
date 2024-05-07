package personal.zaytiri.jobtracker.api.statistics;

import personal.zaytiri.jobtracker.api.database.operations.GetOperation;
import personal.zaytiri.jobtracker.api.database.requests.GetOperationRequest;
import personal.zaytiri.jobtracker.api.domain.entities.JobOffer;
import personal.zaytiri.jobtracker.api.mappers.JobOfferMapperImpl;
import personal.zaytiri.jobtracker.persistence.models.JobOfferModel;
import personal.zaytiri.jobtracker.persistence.repositories.base.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistics {

    private final List<IStatistic<JobOffer>> statistics;

    public Statistics() {
        this.statistics = new ArrayList<>();
    }

    public void addStatistic(IStatistic<JobOffer> statistic){
        statistics.add(statistic);
    }

    public Map<String, List<IStatistic<JobOffer>>> process(){
        GetOperation<JobOfferModel> getOperation = new GetOperation<>();
        getOperation.setRepository(new Repository<>());

        GetOperationRequest<JobOfferModel> getOperationRequest = new GetOperationRequest<>();
        getOperationRequest.setModel(new JobOfferModel());
        getOperationRequest.setFilters(new HashMap<>());
        getOperationRequest.setOrderByColumn(null);
        List<JobOffer> results = new JobOfferMapperImpl().toEntity(getOperation.execute(getOperationRequest), false);

        if(results.isEmpty()){
            return new HashMap<>();
        }
        Map<String, List<IStatistic<JobOffer>>> calculatedStatistics = new HashMap<>();
        for(IStatistic<JobOffer> stat : statistics){

            calculatedStatistics.put(stat.getIdentifier(), stat.calculate(results));
        }

        return calculatedStatistics;
    }
}
