package ec.orangephi.tcg.utils;


import android.util.Log;

import java.util.StringTokenizer;

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
                }
            }
        }, callback);
        return defaultDeck;
    }
}
