package verbventures.frontend;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import verbventures.frontend.ModelClasses.Student;

/**
 * Created by thetraff on 11/9/17.
 */

public class StudentArrayAdapter extends ArrayAdapter<Student> {
    // View lookup cache
    public boolean[] checked;
    private static class ViewHolder {
        TextView firstName;
        TextView lastName;
        EditText IDHolder;
    }

    public StudentArrayAdapter(Context context, Student[] students) {
        super(context, R.layout.item_student, students);
        checked = new boolean[students.length];
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
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
            viewHolder.IDHolder = (EditText) convertView.findViewById(R.id.IDHolder);
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
        viewHolder.IDHolder.setText(student.getUser().getUserId());
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox);
        // Return the completed view to render on screen

        cb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                if(((CheckBox)v).isChecked())
                {
                    checked[position]=true;
                }
                else
                {
                    checked[position]=false;

                }
            }
        });
        return convertView;
    }
}
