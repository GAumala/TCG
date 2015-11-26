package ec.orangephi.tcg.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.twitter.sdk.android.tweetcomposer.Card;

import ec.orangephi.tcg.R;
import ec.orangephi.tcg.intefaces.CardCollector;

/**
 * Created by gesuwall on 11/26/15.
 */
public class CardPageFragment extends Fragment {

    public static String CardDrawable =  "CardPageFragment.CardDrawable";
    public static String CardCode =  "CardPageFragment.CardCode";
    public static String CardQuantity =  "CardPageFragment.CardQuantity";
    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    private int drawable;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null)
            return null;
        drawable = getArguments().getInt(CardDrawable);
        final int quantity = getArguments().getInt(CardQuantity);
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.card_page, container, false);
        ImageView iv = (ImageView) rootView.findViewById(R.id.card_img);
        if(quantity > 0) {
            iv.setImageDrawable(ContextCompat.getDrawable(getActivity(), drawable));
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CardCollector collector = (CardCollector) getActivity();
                    collector.viewCard(getArguments().getString(CardCode));
                }
            });
        } else {
            iv.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.blocked));
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CardCollector collector = (CardCollector)getActivity();
                    collector.showNewCardDialog();
                }
            });
        }


        return rootView;
    }
}
