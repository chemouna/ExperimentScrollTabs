package mona.android.experiments.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cheikhna on 15/10/2014.
 */
public class CustomSlidingTabLayout extends SlidingTabLayout {

    public CustomSlidingTabLayout(Context context) {
        super(context);
    }

    public CustomSlidingTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSlidingTabLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void addViewPagerListener(ViewPager.OnPageChangeListener viewPagerListener) {
        CompositePageListener compositeListener = new CompositePageListener();
        compositeListener.addPageListener(viewPagerListener);
        mViewPager.setOnPageChangeListener(compositeListener);
    }

    protected class CompositePageListener extends InternalViewPagerListener {
        private List<ViewPager.OnPageChangeListener> mListeners = new ArrayList<ViewPager.OnPageChangeListener>();

        public void addPageListener(ViewPager.OnPageChangeListener listener) {
            mListeners.add(listener);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            for(ViewPager.OnPageChangeListener listener : mListeners){
                listener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            super.onPageScrollStateChanged(state);
            for(ViewPager.OnPageChangeListener listener : mListeners) {
                listener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            for(ViewPager.OnPageChangeListener listener : mListeners) {
                listener.onPageSelected(position);
            }
        }
    }

}
