package mona.android.experiments;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import dagger.ObjectGraph;

/**
 * Created by cheikhna on 03/08/2014.
 */
public class ExperimentsApplication extends Application {

    protected ObjectGraph mApplicationGraph;

    @Override
    public void onCreate(){
        super.onCreate();

        if (BuildConfig.DEBUG) {
            //temporary for debugging
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDialog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyDeath()
                    .penaltyLog()
                    .build());
        }
        //buildObjectGraphAndInject();
    }

    /*@DebugLog
    public void buildObjectGraphAndInject() {
        mApplicationGraph = getApplicationGraph();
        mApplicationGraph.inject(this);
    }*/

    public ObjectGraph getApplicationGraph() {
        if (mApplicationGraph == null)
            mApplicationGraph = ObjectGraph.create(Modules.list(this));
        return mApplicationGraph;
    }

    public void inject(Object object){
        mApplicationGraph.inject(object);
    }

    public static ExperimentsApplication get(Context context) {
        return (ExperimentsApplication) context.getApplicationContext();
    }


}