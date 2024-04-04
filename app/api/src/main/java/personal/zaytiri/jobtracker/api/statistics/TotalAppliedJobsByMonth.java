package personal.zaytiri.jobtracker.api.statistics;

import personal.zaytiri.jobtracker.api.domain.entities.JobOffer;

import java.time.LocalDateTime;
import java.util.*;

public class TotalAppliedJobsByMonth implements IStatistic<JobOffer> {
    private int month;

    private int numberOfJobs;

    public int getMonth() {
        return month;
    }

    public int getNumberOfJobs() {
        return numberOfJobs;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setNumberOfJobs(int numberOfJobs) {
        this.numberOfJobs = numberOfJobs;
    }

    @Override
    public String getIdentifier(){
        return "TotalAppliedJobsByMonth";
    }

    @Override
    public List<IStatistic<JobOffer>> calculate(List<JobOffer> data) {
        Map<Integer, Integer> mappedStats = new HashMap<>();

        Set<Integer> monthTracking = new HashSet<>();
        for (JobOffer jo : data){
            if(jo.getAppliedAt().equals(LocalDateTime.MIN)){
                continue;
            }

            int appliedAtMonth = jo.getAppliedAt().getMonth().getValue() - 1;
            monthTracking.add(appliedAtMonth);

            int currentNumberOfJobOffers = mappedStats.getOrDefault(appliedAtMonth, 0);

            mappedStats.put(appliedAtMonth, ++currentNumberOfJobOffers);
        }

        fillRemainingMonths(mappedStats, monthTracking);

        List<IStatistic<JobOffer>> statResults = new ArrayList<>();
        for (var mappedStat : mappedStats.entrySet()){
            TotalAppliedJobsByMonth stat = new TotalAppliedJobsByMonth();
            stat.setMonth(mappedStat.getKey());
            stat.setNumberOfJobs(mappedStat.getValue());

            statResults.add(stat);
        }

        return statResults;
    }

    private void fillRemainingMonths(Map<Integer, Integer> mappedStats, Set<Integer> monthTracking){
        for (int i = 0; i < 12; i++) {
            if(monthTracking.contains(i)){
                continue;
            }
            mappedStats.put(i, 0);
        }
    }
}
