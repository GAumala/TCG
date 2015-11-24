package ec.orangephi.tcg.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.zip.Inflater;

import ec.orangephi.tcg.R;
import ec.orangephi.tcg.intefaces.CardCollector;
import ec.orangephi.tcg.models.CardModel;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

/**
 * Created by gesuwall on 11/22/15.
 */
public class RealmCardAdapter extends RecyclerView.Adapter<RealmCardAdapter.CardHolder>{

    private enum CardViewTypes {unlocked, locked}

    private RealmResults<CardModel> cards;
    @Override
    public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType == CardViewTypes.unlocked.ordinal()){
         view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_list_item, parent, false);
        } else
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_list_item_locked, parent, false);
        return new CardHolder(view);
    }

    public RealmCardAdapter(RealmResults<CardModel> cards) {
        this.cards = cards;
    }

    @Override
    public int getItemViewType(int position) {
        return cards.get(position).getQuantity() == 0 ? CardViewTypes.locked.ordinal() :
                CardViewTypes.unlocked.ordinal();
    }

    @Override
    public void onBindViewHolder(CardHolder holder, int position) {
        final CardModel card = cards.get(position);
        holder.bindCard(card);
        if(holder.ripple != null)
            holder.ripple.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   if(card.getQuantity() > 0){
                       CardCollector collector = (CardCollector) v.getContext();
                       collector.viewCard(card.getCode());
                   }
                }
            });
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public class CardHolder extends RecyclerView.ViewHolder{
        private TextView text;
        private View ripple;
        CardModel card;

        public CardHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
            ripple = itemView.findViewById(R.id.ripple);
        }

        public void bindCard(CardModel card){
            this.card = card;
            if(this.card.getQuantity() == 0)
                text.setText("#" + card.getIndex() + " UNKNOWN ???");
            else {
                text.setText("#" + card.getIndex());

            }

        }
    }
}
