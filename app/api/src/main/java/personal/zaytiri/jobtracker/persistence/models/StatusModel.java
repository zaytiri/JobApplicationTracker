package personal.zaytiri.jobtracker.persistence.models;

import personal.zaytiri.jobtracker.persistence.DatabaseShema;
import personal.zaytiri.jobtracker.persistence.models.base.Model;
import personal.zaytiri.makeitexplicitlyqueryable.pairs.Pair;

import java.util.ArrayList;
import java.util.List;

public class StatusModel extends Model {
    private DatabaseShema schema = DatabaseShema.getINSTANCE();
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    private String color;

    public StatusModel() {
        super(DatabaseShema.getINSTANCE().statusTableName);
    }

    @Override
    public List<Pair<String, Object>> getValues() {
        List<Pair<String, Object>> values = new ArrayList<>();

        values.add(new Pair<>(schema.nameColumnName, name));
        values.add(new Pair<>(schema.colorColumnName, color));
        values.add(new Pair<>(schema.updatedAtColumnName, updatedAt));
        values.add(new Pair<>(schema.createdAtColumnName, createdAt));

        return values;
    }
}
