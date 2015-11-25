package ec.orangephi.tcg.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ec.orangephi.tcg.CardViewActivity;
import ec.orangephi.tcg.R;
import ec.orangephi.tcg.models.ShareOption;

/**
 * Created by gesuwall on 11/24/15.
 */
public class ShareOptionAdapter extends BaseAdapter{
    ArrayList<Integer> apps;
    public ShareOptionAdapter(ArrayList<Integer> options) {
        apps = options;
    }


    @Override
    public int getCount() {
        return apps.size();
    }

    @Override
    public Object getItem(int position) {
        return apps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
            convertView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.simple_share_item, parent, false);
        ImageView image = (ImageView) convertView.findViewById(R.id.image_view);
        TextView text = (TextView) convertView.findViewById(R.id.text_view);
        Context c = convertView.getContext();
        switch (ShareOption.values()[apps.get(position)]){
            case Facebook:
                image.setImageDrawable(ContextCompat.getDrawable(c, R.drawable.facebook));
                text.setText("Facebook");
                break;
            case Twitter:
                image.setImageDrawable(ContextCompat.getDrawable(c, R.drawable.twitter));
                text.setText("Twitter");
                break;
            case Other:
                image.setImageDrawable(ContextCompat.getDrawable(c, R.drawable.share));
                text.setText(c.getString(R.string.other));
                break;
        }
        return convertView;
    }
}
