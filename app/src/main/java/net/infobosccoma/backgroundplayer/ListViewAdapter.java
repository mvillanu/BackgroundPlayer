package net.infobosccoma.backgroundplayer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Created by Maxi on 06/02/2015.
 */
public class ListViewAdapter extends ArrayAdapter<String> {
    private String[] dades;

    ListViewAdapter(Activity context, String[] dades) {
        super(context, R.layout.list_view_layout,dades);
        this.dades = dades;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View element = convertView;

        if(element==null){
            LayoutInflater inflater = ((Activity)getContext()).getLayoutInflater();
            element = inflater.inflate(R.layout.search_activity, null);
        }


            //TextView url = (TextView) element.findViewById(R.id.);
            //url.setText(dades[position]);

        return element;
    }


}
