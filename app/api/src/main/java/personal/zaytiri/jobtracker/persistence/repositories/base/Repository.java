package personal.zaytiri.jobtracker.persistence.repositories.base;

import personal.zaytiri.jobtracker.persistence.DatabaseShema;
import personal.zaytiri.jobtracker.persistence.DbConnection;
import personal.zaytiri.jobtracker.persistence.models.base.Model;
import personal.zaytiri.jobtracker.persistence.repositories.interfaces.IRepository;
import personal.zaytiri.makeitexplicitlyqueryable.pairs.Pair;
import personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.query.builders.DeleteQueryBuilder;
import personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.query.builders.InsertQueryBuilder;
import personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.query.builders.SelectQueryBuilder;
import personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.query.builders.UpdateQueryBuilder;
import personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.query.enums.Operators;
import personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.query.enums.Order;
import personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.schema.Column;
import personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.response.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Repository<TModel extends Model> implements IRepository<TModel> {
    protected DbConnection connection;

    protected Repository() {
        this.connection = DbConnection.getInstance();
    }

    @Override
    public Response create(TModel model) {
        InsertQueryBuilder query = new InsertQueryBuilder(connection.open());

        List<String> exclude = new ArrayList<>();
        exclude.add(DatabaseShema.getINSTANCE().idColumnName);

        query.insertInto(model.getTable(), model.getTable().getAllColumnsExcept(exclude))
                .values(model.getValues());

        return query.execute();
    }

    @Override
    public Response delete(TModel model) {
        DeleteQueryBuilder query = new DeleteQueryBuilder(connection.open());

        query.deleteFrom(model.getTable())
                .where(model.getTable().getColumn(DatabaseShema.getINSTANCE().idColumnName), Operators.EQUALS, model.getId());

        return query.execute();
    }

    @Override
    public Response read(TModel model, Map<String, Pair<String, Object>> filters, Pair<String, String> orderByColumn) {
        SelectQueryBuilder query = new SelectQueryBuilder(connection.open());

        query = query.select().from(model.getTable());

        if (filters == null) {
            filters = new HashMap<>();
        }

        boolean operator = false;
        for (Map.Entry<String, Pair<String, Object>> entry : filters.entrySet()) {
            if (operator) {
                query = query.and();
            }

            Column col = model.getTable().getColumn(entry.getKey());

            if (entry.getValue().getValue() == null) {
                query = query.where(col, Operators.get(entry.getValue().getKey()));
            } else {
                query = query.where(col, Operators.get(entry.getValue().getKey()), entry.getValue().getValue());
            }

            operator = true;
        }

        if (orderByColumn != null) {
            Column orderBy = connection.getSchema().getTable(model.getTable().getName()).getColumn(orderByColumn.getValue());
            List<Column> columnsToOrderBy = new ArrayList<>();
            columnsToOrderBy.add(orderBy);
            query = query.orderBy(Order.get(orderByColumn.getKey()), columnsToOrderBy);
        }

        return query.execute();
    }

    @Override
    public Response update(TModel model) {
        return update(model, model.getValues());
    }

    @Override
    public Response update(TModel model, List<Pair<String, Object>> values) {
        UpdateQueryBuilder query = new UpdateQueryBuilder(connection.open());

        Map<Column, Object> sets = new HashMap<>();

        for (Pair<String, Object> entry : values) {
            if (entry.getKey().equals(DatabaseShema.getINSTANCE().idColumnName)) {
                continue;
            }

            Column col = model.getTable().getColumn(entry.getKey());
            sets.put(col, entry.getValue());
        }

        query.update(model.getTable())
                .values(sets).where(model.getTable().getColumn(DatabaseShema.getINSTANCE().idColumnName), Operators.EQUALS, model.getId());

        return query.execute();
    }
}