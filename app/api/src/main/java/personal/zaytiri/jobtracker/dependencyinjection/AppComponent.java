package personal.zaytiri.jobtracker.dependencyinjection;


import dagger.Component;
import jakarta.inject.Singleton;


@Singleton
@Component(modules = {RepositoriesModule.class})
public interface AppComponent {

}
