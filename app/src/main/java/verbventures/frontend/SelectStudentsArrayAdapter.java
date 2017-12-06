package verbventures.frontend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;

import verbventures.frontend.ModelClasses.Admin;
import verbventures.frontend.ModelClasses.Student;
import verbventures.frontend.ModelClasses.Verb;

/**
 * Created by thetraff on 12/5/17.
 */

public class SelectStudentsArrayAdapter extends ArrayAdapter<Student> {
    //View lookup cache
    private static class ViewHolder {
        TextView firstName;
        TextView lastName;
        EditText IDHolder;
    }

    public static boolean[] checked;
    private String[] studentsInPack;

    public SelectStudentsArrayAdapter(Context context, Student[] students, String[] packStudents) {
        super(context, R.layout.item_student, students);
        studentsInPack = packStudents;
        checked = new boolean[students.length];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Student student = getItem(position);
        //need a final var to use later
        final int position2 = position;
        // Check if an existing view is being reused, otherwise inflate the view
        SelectStudentsArrayAdapter.ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new SelectStudentsArrayAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_student, parent, false);
            viewHolder.firstName = (TextView) convertView.findViewById(R.id.tvFirstname);
            viewHolder.lastName = (TextView) convertView.findViewById(R.id.tvLastname);

            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (SelectStudentsArrayAdapter.ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.firstName.setText(student.getUser().getFirstName());
        viewHolder.lastName.setText(student.getUser().getFirstName());


        Button editButton = convertView.findViewById(R.id.editButton);
        CheckBox cb = convertView.findViewById(R.id.checkBox);
        cb.setVisibility(View.VISIBLE);
        cb.setText("assign verb pack");
        editButton.setVisibility(View.GONE);

        //pre populate check boxes
        if (studentsInPack != null && Arrays.asList(studentsInPack).contains(student.getUser().getUserId())) {
            cb.setChecked(true);
        }
        if (cb.isChecked()) {
            checked[position2]=true;
        }

        cb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                if(((CheckBox)v).isChecked())
                {
                    checked[position2]=true;
                }
                else
                {
                    checked[position2]=false;

                }
            }
        });


        // Return the completed view to render on screen
        return convertView;
    }
}
