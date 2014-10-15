package mona.android.experiments.events;

/**
 * Created by cheikhna on 14/10/2014.
 */
public class AdjustScrollEvent {

    private int scrollHeight;
    private int containerFragmentPosition;

    public AdjustScrollEvent(int scrollHeight, int position){
        this.scrollHeight = scrollHeight;
        this.containerFragmentPosition = position;
    }

    public int getScrollHeight() {
        return scrollHeight;
    }

    public int getContainerFragmentPosition() {
        return containerFragmentPosition;
    }


    @Override
    public String toString() {
        return "AdjustScrollEvent{" +
                "scrollHeight=" + scrollHeight +
                ", containerFragmentPosition=" + containerFragmentPosition +
                '}';
    }

}
