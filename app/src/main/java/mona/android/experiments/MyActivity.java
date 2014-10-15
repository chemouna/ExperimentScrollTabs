package mona.android.experiments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.squareup.otto.Bus;

import javax.inject.Inject;

import butterknife.ButterKnife;
import dagger.ObjectGraph;
import hugo.weaving.DebugLog;
import mona.android.experiments.R;

import butterknife.InjectView;
import mona.android.experiments.events.AdjustScrollEvent;
import mona.android.experiments.fragments.DummyFragment;
import mona.android.experiments.fragments.DummyListFragment;
import mona.android.experiments.widget.CoordinatedHeader;
import mona.android.experiments.widget.CustomSlidingTabLayout;
import mona.android.experiments.widget.SlidingTabLayout;


public class MyActivity extends BaseActivity {
    @InjectView(R.id.sliding_tabs)
    CustomSlidingTabLayout mSlidingTabLayout;

    @InjectView(R.id.view_pager)
    ViewPager mViewPager;

    @InjectView(R.id.iv_photo)
    ImageView mPhotoView;

    @InjectView(R.id.container)
    ViewGroup mContainerView;

    OurViewPagerAdapter mViewPagerAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        ButterKnife.inject(this);

        mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        mViewPagerAdapter = new OurViewPagerAdapter(getFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);

        Resources res = getResources();
        mSlidingTabLayout.setSelectedIndicatorColors(res.getColor(R.color.tab_selected_strip));
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setViewPager(mViewPager);

        mViewPager.setOffscreenPageLimit(mViewPagerAdapter.getCount());
        // Setup the CoordinatedHeader and tab strip
        final CoordinatedHeader header = (CoordinatedHeader) findViewById(R.id.activity_home_header);

        mSlidingTabLayout.addViewPagerListener(new ViewPager.SimpleOnPageChangeListener() {
            @DebugLog
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels); //? not sure
                // Update content that just became visible
                int currentItem = mViewPager.getCurrentItem();
                if (positionOffsetPixels > 0) {
                    if (position < currentItem) {
                        mBus.post(new AdjustScrollEvent(mSlidingTabLayout.getTop(), position));
                    } else {
                        mBus.post(new AdjustScrollEvent(mSlidingTabLayout.getTop(), position + 1));
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (state != ViewPager.SCROLL_STATE_IDLE) {
                    // Wait until the pager is idle to animate the header
                    return;
                }
                header.restoreCoordinate(mViewPager.getCurrentItem(), 250);
            }

            @DebugLog
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Adjust the the ViewPager's Fragments' scroll position
                mBus.post(new AdjustScrollEvent(mSlidingTabLayout.getTop(), position));
            }
        });

        /*mViewPager.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    private int mLastViewHeight;
                    @DebugLog
                    @Override
                    public void onGlobalLayout() {
                        int newHeight = mViewPager.getHeight();
                        Log.i("TEST", " newHeight : " + newHeight + " - lastViewHeight : " + mLastViewHeight);
                        if (newHeight != mLastViewHeight) {
                            //TODO: send event AdjustScroll with getTop value
                            mBus.post(new AdjustScrollEvent(mSlidingTabLayout.getTop(),
                                    mViewPager.getCurrentItem()));
                            mLastViewHeight = newHeight;
                        }
                    }
                });*/

    }

    private class OurViewPagerAdapter extends FragmentPagerAdapter {
        public OurViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return DummyListFragment.getInstance(position);
                case 1:
                    return new DummyFragment();
                default:
                    return new DummyFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case 0:
                    return "Tab 1";
                case 1:
                     return "Tab 2";
                default:
                    return "Tab";
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
