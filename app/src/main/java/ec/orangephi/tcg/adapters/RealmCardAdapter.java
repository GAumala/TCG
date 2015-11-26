package ec.orangephi.tcg.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.zip.Inflater;

import ec.orangephi.tcg.R;
import ec.orangephi.tcg.fragments.CardPageFragment;
import ec.orangephi.tcg.intefaces.CardCollector;
import ec.orangephi.tcg.models.CardModel;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

/**
 * Created by gesuwall on 11/22/15.
 */
public class RealmCardAdapter extends FragmentStatePagerAdapter {

    public RealmCardAdapter(FragmentManager fm) {
        super(fm);
    }

    public RealmCardAdapter(FragmentManager fm, RealmResults<CardModel> results) {
        super(fm);
        this.cards = results;
    }

    @Override
    public Fragment getItem(int position) {
        CardPageFragment fragment = new CardPageFragment();
        Bundle args = new Bundle();
        args.putInt(CardPageFragment.CardDrawable, cards.get(position).getDrawable());
        args.putInt(CardPageFragment.CardQuantity, cards.get(position).getQuantity());
        args.putString(CardPageFragment.CardCode, cards.get(position).getCode());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return cards.size();
    }

    private enum CardViewTypes {unlocked, locked}

    private RealmResults<CardModel> cards;

}
