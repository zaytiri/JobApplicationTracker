package personal.zaytiri.jobtracker.api.domain.entities.base;


import personal.zaytiri.makeitexplicitlyqueryable.pairs.Pair;

import java.util.List;
import java.util.Map;

public interface IStorageOperations<M> {

    boolean create(M model);

    boolean delete(M model);

//    boolean exists();

    List<Map<String, String>> get(M model, Map<String, Pair<String, Object>> filters, Pair<String, String> orderByColumn);

    boolean update(M model);
}
