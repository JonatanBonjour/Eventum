package ch.epfl.sdp.ui.main.swipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;

public class CardArrayAdapter extends ArrayAdapter<Event> {

    public CardArrayAdapter(Context context, List<Event> items){
        super(context, R.layout.cardview_swipe_item, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Event event = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cardview_swipe_item, parent, false);
        }

        TextView name = convertView.findViewById(R.id.eventName);
        ImageView imageView = convertView.findViewById(R.id.imageCard);
        TextView description = convertView.findViewById(R.id.eventDescription);

        name.setText(event.getTitle());
        imageView.setImageResource(event.getImageID());
        description.setText(event.getDescription());
        return convertView;
    }
}