package personal.zaytiri.jobtracker.api.statistics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import personal.zaytiri.jobtracker.api.domain.entities.JobOffer;

import java.util.List;
@JsonSerialize
public interface IStatistic<E> {
    @JsonIgnore
    String getIdentifier();
    @JsonIgnore
    List<IStatistic<E>> calculate(List<E> data);
}
