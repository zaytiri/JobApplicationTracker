package personal.zaytiri.jobtracker.api.mappers.base;

import java.util.List;
import java.util.Map;

public interface IMapper<E, M> {

    List<E> toEntity(List<Map<String, String>> rows, boolean mixedResult);
}
