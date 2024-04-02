package personal.zaytiri.jobtracker.dependencyinjection;


import dagger.Component;
import jakarta.inject.Singleton;
import personal.zaytiri.jobtracker.api.domain.entities.JobOffer;


@Singleton
@Component(modules = {RepositoriesModule.class})
public interface AppComponent {
}
