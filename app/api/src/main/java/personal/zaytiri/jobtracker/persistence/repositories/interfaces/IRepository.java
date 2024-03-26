package personal.zaytiri.jobtracker.persistence.repositories.interfaces;


import personal.zaytiri.jobtracker.persistence.models.base.Model;
import personal.zaytiri.makeitexplicitlyqueryable.pairs.Pair;
import personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.response.Response;

import java.util.List;
import java.util.Map;

public interface IRepository<TModel extends Model> {

    /**
     * Creates a new model in the database.
     *
     * @param model to be created
     * @return type Response object
     */
    Response create(TModel model);

    /**
     * Deletes an existent entity from the database.
     *
     * @param model to be deleted
     * @return type Response object
     */
    Response delete(TModel model);

    /**
     * Returns any results that match the given filters.
     * <p>
     * If no filters are given, all found results are returned.
     * A filter consists of a column's name, the desired operator and a value respectively.
     * The Map's key must be the column's name while the Pair object consists of the operator (Pair's key) and the value (Pair's value).
     * If the value is null, it means the condition does not need the value, e.g. to use the IS NULL operator, this does not need a second value.
     * <p>
     * The order by value is set by instantiating a Pair containing the order (Pair's key) and the column's name (Pair's value).
     * If no order is necessary, input null.
     *
     * @param filters a set of 3 values, the column's name, the operator and a value.
     * @return type Response object
     */
    Response read(TModel model, Map<String, Pair<String, Object>> filters, Pair<String, String> orderByColumn);

    /**
     * Updates an entity with all values from that entity.
     *
     * @param model to be updated
     * @return type Response object
     */
    Response update(TModel model);

    /**
     * Updates an entity with given values, meaning it will only update those values and not all values, opposite of the Update(GEntity entity) method.
     *
     * @param model to be updated
     * @param values to be updated
     * @return type Response object
     */
    Response update(TModel model, List<Pair<String, Object>> values);
}
