package personal.zaytiri.jobtracker.persistence.models.base;

import personal.zaytiri.jobtracker.persistence.DbConnection;
import personal.zaytiri.makeitexplicitlyqueryable.pairs.Pair;
import personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.schema.Database;
import personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.schema.Table;

import java.time.LocalDate;
import java.util.List;

public abstract class Model {

    protected Table table;
    protected int id;
    protected LocalDate updatedAt;
    protected LocalDate createdAt;

    protected Model(String tableName) {
        Database schema = DbConnection.getInstance().getSchema();
        table = schema.getTable(tableName);
        if (table == null) {
            System.err.println("No table was found in schema with the following name: " + tableName);
            return;
        }

        createdAt = LocalDate.now();
        updatedAt = LocalDate.now();
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    protected void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public Model setId(int id) {
        this.id = id;
        return this;
    }

    public Table getTable() {
        return table;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    protected void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public abstract List<Pair<String, Object>> getValues();
}
