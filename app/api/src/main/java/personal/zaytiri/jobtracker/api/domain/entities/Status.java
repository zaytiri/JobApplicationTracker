package personal.zaytiri.jobtracker.api.domain.entities;

import personal.zaytiri.jobtracker.api.domain.entities.base.Entity;

import java.time.LocalDateTime;

public class Status extends Entity<Status> {
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
    public Status() {}

    @Override
    public String toString() {
        return "Status{" +
                "id=" + id +
                '}';
    }

    @Override
    public Status setId(int id) {
        this.id = id;
        return this;
    }
}
