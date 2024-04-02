package personal.zaytiri.jobtracker.api.database.requests;

import personal.zaytiri.makeitexplicitlyqueryable.pairs.Pair;

import java.util.Map;

public class GetOperationRequest<M> {
    private M model;
    private Map<String, Pair<String, Object>> filters;

    public M getModel() {
        return model;
    }

    public void setModel(M model) {
        this.model = model;
    }

    public Map<String, Pair<String, Object>> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Pair<String, Object>> filters) {
        this.filters = filters;
    }

    public Pair<String, String> getOrderByColumn() {
        return orderByColumn;
    }

    public void setOrderByColumn(Pair<String, String> orderByColumn) {
        this.orderByColumn = orderByColumn;
    }

    private Pair<String, String> orderByColumn;
}
