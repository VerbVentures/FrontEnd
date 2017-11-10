package verbventures.frontend;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import verbventures.frontend.ModelClasses.Student;

/**
 * Created by thetraff on 11/9/17.
 */

public class StudentArrayAdapter extends ArrayAdapter<Student> {
    // View lookup cache
    private static class ViewHolder {
        TextView firstName;
        TextView lastName;
    }

    public StudentArrayAdapter(Context context, Student[] students) {
        super(context, R.layout.item_student, students);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Student student = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_student, parent, false);
            viewHolder.firstName = (TextView) convertView.findViewById(R.id.tvFirstname);
            viewHolder.lastName = (TextView) convertView.findViewById(R.id.tvLastname);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.firstName.setText(student.getUser().getFirstName());
        viewHolder.lastName.setText(student.getUser().getLastName());
        // Return the completed view to render on screen
        return convertView;
    }
}
