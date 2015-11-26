package ec.orangephi.tcg.utils;


import android.util.Log;

import java.util.StringTokenizer;

import ec.orangephi.tcg.R;
import ec.orangephi.tcg.models.CardModel;
import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmResults;

/**
 * Created by gesuwall on 11/22/15.
 */
public class DeckUtils {
    public static RealmAsyncTask initDeck(Realm realm, final String[] defaultCards, Realm.Transaction.Callback callback){
        RealmAsyncTask defaultDeck = realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (!realm.where(CardModel.class).findAll().isEmpty()) {
                    throw new IllegalStateException("There are already cards in Realm.");
                }
                for (int i = 0; i < defaultCards.length; i++) {
                    StringTokenizer tokens = new StringTokenizer(defaultCards[i], "@");
                    CardModel card = realm.createObject(CardModel.class);
                    card.setCode(tokens.nextToken());
                    card.setText(tokens.nextToken());
                    card.setIndex(i + 1);
                    card.setQuantity(0);
                    card.setDrawable(getDrawableFromIndex(i + 1));
                }
            }
        }, callback);
        return defaultDeck;
    }

    public static int getDrawableFromIndex(int index){
        switch (index){
            case 1:
                return R.drawable.card01;
            case 2:
                return R.drawable.card02;
            case 3:
                return R.drawable.card03;
            case 4:
                return R.drawable.card04;
            case 5:
                return R.drawable.card05;
            case 6:
                return R.drawable.card06;
            case 7:
                return R.drawable.card07;
            case 8:
                return R.drawable.card08;
            case 9:
                return R.drawable.card09;
            case 10:
                return R.drawable.card10;
            case 11:
                return R.drawable.card11;
            case 12:
                return R.drawable.card12;
            case 13:
                return R.drawable.card13;
            case 14:
                return R.drawable.card14;
            case 15:
                return R.drawable.card15;
            case 16:
                return R.drawable.card16;
            case 17:
                return R.drawable.card17;
        }
        return 0;
    }
}
