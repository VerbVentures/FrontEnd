package verbventures.frontend;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import verbventures.frontend.ModelClasses.VerbPack;

/**
 * Created by thetraff on 11/9/17.
 */

public class VerbPackArrayAdapter extends ArrayAdapter<VerbPack> {
    // View lookup cache
    private static class ViewHolder {
        TextView title;
    }

    public VerbPackArrayAdapter(Context context, VerbPack[] verbPacks) {
        super(context, R.layout.item_verb, verbPacks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        VerbPack verbPack = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_verbpack, parent, false);
            //grab the textViews from the viewHolder
            viewHolder.title = (TextView) convertView.findViewById(R.id.tvTitle);

            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // populate the view with the data
        viewHolder.title.setText(verbPack.getTitle());

        // Return the completed view to render on screen
        return convertView;
    }
}
