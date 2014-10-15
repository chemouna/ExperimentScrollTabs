package mona.android.experiments.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.eventbus.Subscribe;

import butterknife.ButterKnife;
import butterknife.InjectView;
import mona.android.experiments.R;
import mona.android.experiments.events.AdjustScrollEvent;
import mona.android.experiments.widget.CoordinatedHeader;

/**
 * Created by cheikhna on 28/07/2014.
 */
public class DummyListFragment extends Fragment {

    @InjectView(R.id.lv_dummy)
    ListView mListView;

    public static DummyListFragment getInstance(int index){
        Bundle args = new Bundle();
        args.putInt("index", index);
        DummyListFragment frg = new DummyListFragment();
        frg.setArguments(args);
        return frg;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(android.view.LayoutInflater inflater,
                          android.view.ViewGroup container, android.os.Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dummy_list, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView.setAdapter(new SavedTabsListAdapter());

        mListView.addHeaderView(LayoutInflater.from(getActivity()).inflate(
                R.layout.fake_header, mListView, false));

        // Retrieve the index used to save the y-coordinate for this Fragment
        final int index = getArguments().getInt("index");

        // Find the CoordinatedHeader and tab strip (or anchor point) from the main Activity layout
        final CoordinatedHeader header = (CoordinatedHeader) getActivity().findViewById(R.id.activity_home_header);
        final View anchor = getActivity().findViewById(R.id.sliding_tabs);

        // Attach a custom OnScrollListener used to control the CoordinatedHeader
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {

                // Determine the maximum allowed scroll height
                final int maxScrollHeight = header.getHeight() - anchor.getHeight();

                // If the first item has scrolled off screen, anchor the header
                if (firstVisibleItem != 0) {
                    header.storeCoordinate(index, -maxScrollHeight);
                    return;
                }

                final View firstChild = view.getChildAt(firstVisibleItem);
                if (firstChild == null) {
                    return;
                }

                // Determine the offset to scroll the header
                final float offset = Math.min(-firstChild.getY(), maxScrollHeight);
                header.storeCoordinate(index, -offset);
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // Nothing to do
            }

        });

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

    public class SavedTabsListAdapter extends BaseAdapter {

        private String[] items = { " Dummy 1" , " Dummy 2 " , " Dummy 3" , " Dummy 4" , " Dummy 5" ,
                " Dummy 6" , " Dummy 7" , " Dummy 8", " Dummy 9", " Dummy 10" , " Dummy 11", " Dummy 12",
                " Dummy 13", " Dummy 14", " Dummy 15", " Dummy 16", " Dummy 16", " Dummy 17", " Dummy 18",
                " Dummy 19", " Dummy 20", " Dummy 21", " Dummy 22", " Dummy 23", " Dummy 24" };

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int i) {
            return items[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            String h = items[position];
            if (convertView == null) {
                convertView = LayoutInflater.from(
                        getActivity()).inflate(android.R.layout.simple_list_item_1, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv.setText(h);
            return convertView;
        }
    }

    static class ViewHolder{
        @InjectView(android.R.id.text1)
        TextView tv;

        public ViewHolder(View view){
            ButterKnife.inject(this, view);
        }
    }

    @Subscribe
    public void onAdjustScrollEvent(AdjustScrollEvent event) {
        Log.i("TEST", " AdjustScrollEvent - event scrollHeight : " + event.getScrollHeight());
        if (event.getScrollHeight() == 0 && mListView.getFirstVisiblePosition() >= 1) {
            // ListView does not need to adjust scroll, as the top tab bar is its sticky position
            return;
        }
        mListView.setSelectionFromTop(1, event.getScrollHeight());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

}