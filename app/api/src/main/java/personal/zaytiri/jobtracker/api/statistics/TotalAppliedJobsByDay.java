package personal.zaytiri.jobtracker.api.statistics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import personal.zaytiri.jobtracker.api.domain.entities.JobOffer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
public class TotalAppliedJobsByDay implements IStatistic<JobOffer> {

    private LocalDate date = LocalDate.MIN;

    private int numberOfAppliedJobsByDay;

    public TotalAppliedJobsByDay() {
    }

    public LocalDate getDate() {
        return date;
    }

    public int getNumberOfAppliedJobsByDay() {
        return numberOfAppliedJobsByDay;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setNumberOfAppliedJobsByDay(int numberOfAppliedJobsByDay) {
        this.numberOfAppliedJobsByDay = numberOfAppliedJobsByDay;
    }

    @Override
    public String getIdentifier() {
        return "TotalAppliedJobsByDay";
    }

    @Override
    public List<IStatistic<JobOffer>> calculate(List<JobOffer> data) {
        Map<LocalDate, Integer> mappedStats = new HashMap<>();

        Set<LocalDate> dateTracking = new HashSet<>();
        for (JobOffer jo : data){
            if(jo.getAppliedAt().equals(LocalDateTime.MIN)){
                continue;
            }

            LocalDate appliedAt = jo.getAppliedAt().toLocalDate();
            dateTracking.add(appliedAt);

            int currentNumberOfJobOffers = mappedStats.getOrDefault(appliedAt, 0);

            mappedStats.put(appliedAt, ++currentNumberOfJobOffers);
        }

        fillRemainingMonths(mappedStats, dateTracking);

        List<IStatistic<JobOffer>> statResults = new ArrayList<>();
        for (var mappedStat : mappedStats.entrySet()){
            TotalAppliedJobsByDay stat = new TotalAppliedJobsByDay();
            stat.setDate(mappedStat.getKey());
            stat.setNumberOfAppliedJobsByDay(mappedStat.getValue());

            statResults.add(stat);
        }

        return statResults;
    }


    private void fillRemainingMonths(Map<LocalDate, Integer> mappedStats, Set<LocalDate> dateTracking){
        List<LocalDate> sortedDates = dateTracking.stream()
                .sorted()
                .toList();

        for (LocalDate date : getDatesBetween(sortedDates.get(0), sortedDates.get(sortedDates.size() - 1))) {
            if(dateTracking.contains(date)){
                continue;
            }
            mappedStats.put(date, 0);
        }
    }


    private static List<LocalDate> getDatesBetween(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> datesInRange = new ArrayList<>();
        LocalDate date = startDate;
        while (!date.isAfter(endDate)) {
            datesInRange.add(date);
            date = date.plusDays(1);
        }
        return datesInRange;
    }
}
