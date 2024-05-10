package personal.zaytiri.jobtracker.api.statistics;

import personal.zaytiri.jobtracker.api.database.operations.GetOperation;
import personal.zaytiri.jobtracker.api.database.requests.GetOperationRequest;
import personal.zaytiri.jobtracker.api.domain.entities.JobOffer;
import personal.zaytiri.jobtracker.api.domain.entities.Status;
import personal.zaytiri.jobtracker.api.mappers.StatusMapperImpl;
import personal.zaytiri.jobtracker.persistence.models.StatusModel;
import personal.zaytiri.jobtracker.persistence.repositories.base.Repository;

import java.util.*;
import java.util.stream.Collectors;
public class TotalJobsByLatestStatus implements IStatistic<JobOffer> {

    private String status;

    private int numberOfJobs;

    public String getStatus() {
        return status;
    }

    public int getNumberOfJobs() {
        return numberOfJobs;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setNumberOfJobs(int numberOfJobs) {
        this.numberOfJobs = numberOfJobs;
    }

    @Override
    public String getIdentifier() {
        return "TotalJobsByLatestStatus";
    }

    @Override
    public List<IStatistic<JobOffer>> calculate(List<JobOffer> data) {
        GetOperation<StatusModel> getOperation = new GetOperation<>();
        getOperation.setRepository(new Repository<>());

        GetOperationRequest<StatusModel> getOperationRequest = new GetOperationRequest<>();
        getOperationRequest.setModel(new StatusModel());
        getOperationRequest.setFilters(new HashMap<>());
        getOperationRequest.setOrderByColumn(null);
        List<Status> statusResults = new StatusMapperImpl().toEntity(getOperation.execute(getOperationRequest), false);

        Map<Integer, Integer> mappedStats = new HashMap<>();

        Set<Integer> statusTracking = new HashSet<>();
        for (JobOffer jo : data){
            int statusId = jo.getStatusId();
            statusTracking.add(statusId);

            int currentNumberOfJobOffers = mappedStats.getOrDefault(statusId, 0);

            mappedStats.put(statusId, ++currentNumberOfJobOffers);
        }

        fillRemainingStatus(mappedStats, statusResults, statusTracking);

        Map<Integer, String> statusMap = statusResults.stream().collect(Collectors.toMap(Status::getId, Status::getName));
        statusMap.put(0, "No Status");
        List<IStatistic<JobOffer>> statResults = new ArrayList<>();
        for (var mappedStat : mappedStats.entrySet()){
            TotalJobsByLatestStatus stat = new TotalJobsByLatestStatus();
            stat.setStatus(statusMap.get(mappedStat.getKey()));
            stat.setNumberOfJobs(mappedStat.getValue());

            statResults.add(stat);
        }

        return statResults;
    }


    private void fillRemainingStatus(Map<Integer, Integer> mappedStats, List<Status> statusResults, Set<Integer> statusTracking){
        for (Status stat : statusResults) {
            if(statusTracking.contains(stat.getId())){
                continue;
            }
            mappedStats.put(stat.getId(), 0);
        }
    }
}
