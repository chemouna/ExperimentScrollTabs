package mona.android.experiments;

import android.app.Application;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by cheikhna on 21/08/2014.
 */
@Module(
    injects = {
        ExperimentsApplication.class,
        MyActivity.class
    }
)
public class ExperimentsModule {
    private final ExperimentsApplication app;

    public ExperimentsModule(ExperimentsApplication app){
        this.app = app;
    }

    /*@Provides
    @Singleton
    Application provideApplication() { return app; }*/

    @Provides @Singleton
    Bus provideBus() {
        return new Bus();
    }


}
