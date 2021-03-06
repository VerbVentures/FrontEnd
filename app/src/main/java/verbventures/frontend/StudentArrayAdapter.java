package verbventures.frontend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import verbventures.frontend.ModelClasses.Student;

/**
 * Created by thetraff on 11/9/17.
 */

public class StudentArrayAdapter extends ArrayAdapter<Student> {
    // View lookup cache
    public boolean[] checked;
    private static boolean verbPackSelect = false;
    private String[] studentsInPack;
    private static class ViewHolder {
        TextView firstName;
        TextView lastName;
        EditText IDHolder;

    }

    public StudentArrayAdapter(Context context, Student[] students) {
        super(context, R.layout.item_student, students);
        checked = new boolean[students.length];
    }

    public StudentArrayAdapter(Context context, Student[] students, String[] verbPackStudents) {
        super(context, R.layout.item_student, students);
        //verbPackSelect = true;
        studentsInPack = verbPackStudents;
        checked = new boolean[students.length];
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Student student = getItem(position);
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
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox);

        Button editButton = convertView.findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), CreateStudent.class);
                intent.putExtra("admin", student.getAdminObj());
                intent.putExtra("student", student);
                 v.getContext().startActivity(intent);
            }
        });

        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox)view).isChecked()) {
                    checked[position] = true;
                }
                else {
                    checked[position] = false;
                }
            }
        });



        // Return the completed view to render on screen

        return convertView;
    }

}
