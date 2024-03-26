package personal.zaytiri.jobtracker.api.domain.entities.base;


import com.fasterxml.jackson.annotation.JsonIgnore;
import personal.zaytiri.jobtracker.dependencyinjection.AppComponent;
import personal.zaytiri.jobtracker.dependencyinjection.DaggerAppComponent;

public abstract class Entity<E, R, M> implements IDependencyInjection<E> {
    @JsonIgnore
    protected R repository;
    @JsonIgnore
    protected M mapper;

    protected int id;

    public int getId() {
        return id;
    }

    @JsonIgnore
    public E getInstance() {
        AppComponent component = DaggerAppComponent.create();

        return getInjectedComponent(component);
    }

    public abstract E setId(int id);

    @JsonIgnore
    protected abstract E getInjectedComponent(AppComponent component);
}
