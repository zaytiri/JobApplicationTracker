package personal.zaytiri.jobtracker.api.domain.entities.base;


import com.fasterxml.jackson.annotation.JsonIgnore;
import personal.zaytiri.jobtracker.dependencyinjection.AppComponent;
import personal.zaytiri.jobtracker.dependencyinjection.DaggerAppComponent;

public abstract class Entity<E> {

    protected int id;

    public int getId() {
        return id;
    }

    public abstract E setId(int id);
}
