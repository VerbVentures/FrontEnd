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
import android.widget.TextView;

import java.util.Arrays;

import verbventures.frontend.ModelClasses.Admin;
import verbventures.frontend.ModelClasses.Verb;

/**
 * Created by thetraff on 12/5/17.
 */

public class SelectVerbsArrayAdapter extends ArrayAdapter<Verb> {
    //View lookup cache
    private static class ViewHolder {
        TextView verb;
    }

    public static boolean[] checked;
    private String[] verbsInPack;

    public SelectVerbsArrayAdapter(Context context, Verb[] verbs, String[] packVerbs) {
        super(context, R.layout.item_verb, verbs);
        verbsInPack = packVerbs;
        checked = new boolean[verbs.length];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Verb verb = getItem(position);
        //need a final var to use later
        final int position2 = position;
        // Check if an existing view is being reused, otherwise inflate the view
        SelectVerbsArrayAdapter.ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new SelectVerbsArrayAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_verb, parent, false);
            viewHolder.verb = (TextView) convertView.findViewById(R.id.tvVerb);

            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (SelectVerbsArrayAdapter.ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.verb.setText(verb.getVerb());
        Intent intent = ((Activity) this.getContext()).getIntent();
        final Admin admin = (Admin) intent.getSerializableExtra("admin");


        Button editButton = convertView.findViewById(R.id.editButton);
        CheckBox cb = convertView.findViewById(R.id.checkBox);
        cb.setVisibility(View.VISIBLE);
        editButton.setVisibility(View.GONE);

        //pre populate check boxes
        if (verbsInPack != null && Arrays.asList(verbsInPack).contains(verb.getVerbId())) {
            cb.setChecked(true);
        }
        if (cb.isChecked()) {
            checked[position2] = true;
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
