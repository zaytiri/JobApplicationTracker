package personal.zaytiri.jobtracker.persistence.mappers.base;

import java.util.List;
import java.util.Map;

public interface IMapper<E, M> {

    List<E> toEntity(List<Map<String, String>> rows, boolean mixedResult);

    E toEntity(M model);

    M toModel(E entity);
}
