package ch.epfl.sdp.ui.main.swipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;

public class SwipeFragment extends Fragment {

    private ArrayAdapter<Event> mArrayAdapter;
    private EventDetailFragment mInfoFragment;
    private List<Event> mEventList;
    private Event mCurrentEvent;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mEventList = new ArrayList<>();
        mEventList.add(new Event("OSS-117 Movie watching",
                "We will watch OSS-117: Cairo, Nest of Spies and then we can exchange about why this is the best movie of all times",
                new Date(2021, 1, 16), R.drawable.oss_117));
        mEventList.add(new Event("Duck themed party",
                "Bring out your best duck disguises and join us for our amazing party on the lakeside. Swans disguises not allowed",
                new Date(2020, 3, 7), R.drawable.duck));
        mEventList.add(new Event("Make Internet great again",
                "At this meeting we will debate on how to make pepe the frog memes great again",
                new Date(2020, 4, 20), R.drawable.frog));
        mEventList.add(new Event("Real Fake Party",
                "This is really happening",
                new Date(2020, 11, 10)));

        mArrayAdapter = new CardArrayAdapter(getContext(), mEventList);
        mCurrentEvent = mEventList.get(0);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.swipe_fragment, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        SwipeFlingAdapterView flingAdapterView = getView().findViewById(R.id.frame);
        flingAdapterView.setAdapter(mArrayAdapter);
        flingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
                                              @Override
                                              public void removeFirstObjectInAdapter() {
                                                  mEventList.remove(0);
                                                  mArrayAdapter.notifyDataSetChanged();
                                                  mCurrentEvent = mEventList.get(0);
                                              }

                                              @Override
                                              public void onLeftCardExit(Object o) {}

                                              @Override
                                              public void onRightCardExit(Object o) {}

                                              @Override
                                              public void onAdapterAboutToEmpty(int i) {}

                                              @Override
                                              public void onScroll(float v) {}
                                          }
        );
        flingAdapterView.setOnItemClickListener((itemPosition, dataObject) -> {
            mInfoFragment = new EventDetailFragment(mCurrentEvent,this);
            getActivity().getSupportFragmentManager().beginTransaction().replace(this.getId(), mInfoFragment).commit();
        });
    }
}
