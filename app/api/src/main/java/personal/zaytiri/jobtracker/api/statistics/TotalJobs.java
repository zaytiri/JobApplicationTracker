package personal.zaytiri.jobtracker.api.statistics;

import personal.zaytiri.jobtracker.api.domain.entities.JobOffer;

import java.util.ArrayList;
import java.util.List;

public class TotalJobs implements IStatistic<JobOffer> {

    private int numberOfJobs;

    public int getNumberOfJobs() {
        return numberOfJobs;
    }

    public void setNumberOfJobs(int numberOfJobs) {
        this.numberOfJobs = numberOfJobs;
    }

    @Override
    public String getIdentifier() {
        return "TotalJobs";
    }

    @Override
    public List<IStatistic<JobOffer>> calculate(List<JobOffer> data) {
        List<IStatistic<JobOffer>> statResults = new ArrayList<>();

        TotalJobs stat = new TotalJobs();
        stat.setNumberOfJobs(data.size());
        statResults.add(stat);

        return statResults;
    }
}
