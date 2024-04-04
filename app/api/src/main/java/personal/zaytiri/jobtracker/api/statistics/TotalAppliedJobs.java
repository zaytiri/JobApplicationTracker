package personal.zaytiri.jobtracker.api.statistics;

import com.fasterxml.jackson.annotation.JsonInclude;
import personal.zaytiri.jobtracker.api.domain.entities.JobOffer;

import java.time.LocalDateTime;
import java.util.*;

public class TotalAppliedJobs implements IStatistic<JobOffer>{

    private int numberOfAppliedJobs;

    public int getNumberOfAppliedJobs() {
        return numberOfAppliedJobs;
    }

    public void setNumberOfAppliedJobs(int numberOfAppliedJobs) {
        this.numberOfAppliedJobs = numberOfAppliedJobs;
    }

    @Override
    public String getIdentifier() {
        return "TotalAppliedJobs";
    }

    @Override
    public List<IStatistic<JobOffer>> calculate(List<JobOffer> data) {
        List<IStatistic<JobOffer>> statResults = new ArrayList<>();

        TotalAppliedJobs stat = new TotalAppliedJobs();
        for (JobOffer jo : data){
            if(jo.getAppliedAt().equals(LocalDateTime.MIN)){
                continue;
            }

            stat.setNumberOfAppliedJobs(stat.getNumberOfAppliedJobs() + 1);
        }
        statResults.add(stat);

        return statResults;
    }
}
