package personal.zaytiri.jobtracker.api.domain.entities;

import personal.zaytiri.jobtracker.api.domain.entities.base.Entity;

public class Settings extends Entity<Settings> {
    private int appliedStatus;
    private int closedStatus;

    public Settings() {}

    public int getAppliedStatus() {
        return appliedStatus;
    }

    public void setAppliedStatus(int appliedStatus) {
        this.appliedStatus = appliedStatus;
    }

    public int getClosedStatus() {
        return closedStatus;
    }

    public void setClosedStatus(int closedStatus) {
        this.closedStatus = closedStatus;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "id=" + id +
                '}';
    }

    @Override
    public Settings setId(int id) {
        this.id = id;
        return this;
    }
}
