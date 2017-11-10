package verbventures.frontend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import verbventures.frontend.ModelClasses.Session;

/**
 * Created by thetraff on 11/9/17.
 */

public class SessionArrayAdapter extends ArrayAdapter<Session> {
    // View lookup cache
    private static class ViewHolder {
        TextView sessionId;
        TextView sessionDate;

    }

    public SessionArrayAdapter(Context context, Session[] sessions) {
        super(context, R.layout.item_session, sessions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Session session = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_verb, parent, false);
            viewHolder.sessionId = (TextView) convertView.findViewById(R.id.tvSessionId);
            viewHolder.sessionDate = (TextView) convertView.findViewById(R.id.tvSessionDate);

            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.sessionId.setText(session.getSessionId());
        viewHolder.sessionDate.setText(session.getSessionDt());

        // Return the completed view to render on screen
        return convertView;
    }
}
