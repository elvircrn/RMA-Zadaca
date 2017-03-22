package ba.unsa.etf.rma.elvircrn.movieinfo;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class GlumacAdapter extends ArrayAdapter<Glumac> {
    public GlumacAdapter(Context context, ArrayList<Glumac> glumci) {
        super(context, 0, glumci);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Glumac glumac = getItem(position);

        // convertView je sada LinearLayout object i sadrzi TextView objekat, rasporedjen
        // prema xml-u
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.glumac_list_item, parent, false);
        }

        TextView nameTextView = ((TextView)convertView.findViewById(R.id.nameTextView));
        TextView godineTextView = ((TextView)convertView.findViewById(R.id.godineText));
        TextView ratingTextView = ((TextView)convertView.findViewById(R.id.ratingTextView));
        TextView mjestTextView = ((TextView)convertView.findViewById(R.id.mjestoTextView));

        nameTextView.setText(glumac.getFullName());
        godineTextView.setText(glumac.getGodinaFormatted());
        mjestTextView.setText(glumac.getMjestoRodjenja());
        ratingTextView.setText(String.format(Locale.getDefault(), "%d", glumac.getRating()));

        return convertView;
    }
}
