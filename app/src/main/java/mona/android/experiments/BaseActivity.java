package mona.android.experiments;

import android.app.Activity;
import android.os.Bundle;

import com.squareup.otto.Bus;

import javax.inject.Inject;

import butterknife.ButterKnife;
import dagger.ObjectGraph;

/**
 * Created by cheikhna on 14/10/2014.
 */
public class BaseActivity extends Activity {

    private ObjectGraph mActivityGraph;

    @Inject
    Bus mBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        ButterKnife.inject(this);

        // Create the activity graph by .plus-ing its modules onto the application graph.
        if (mActivityGraph == null) {
            ExperimentsApplication app = (ExperimentsApplication) getApplication();
            mActivityGraph = app.getApplicationGraph().plus(Modules.list(app));
        }
        mActivityGraph.inject(this);
        mBus.register(this);
    }

    @Override
    protected void onDestroy() {
        mBus.unregister(this);
        // clear the reference to the activity graph to allow it to be garbage collected asap
        mActivityGraph = null;
        super.onDestroy();
    }

    /**
     * Inject the supplied {@code object} using the activity-specific graph.
     */
    public void inject(Object object) {
        mActivityGraph.inject(object);
    }


}
