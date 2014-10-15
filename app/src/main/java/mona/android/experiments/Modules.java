package mona.android.experiments;

/**
 * Created by cheikhna on 10/08/2014.
 */
public class Modules {

    public static Object[] list(ExperimentsApplication app) {
        return new Object[]{
            new ExperimentsModule(app)
        };
    }

    private Modules() {
        //Not instanciable
    }

}

/**
 If context retention causes a pb --> do like philm with ContextProvider

 - Seperate TaskProviderModule on its own

 */