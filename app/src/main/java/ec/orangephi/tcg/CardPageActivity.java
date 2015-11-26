package ec.orangephi.tcg;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import ec.orangephi.tcg.ZXing.IntentIntegrator;
import ec.orangephi.tcg.ZXing.IntentResult;
import ec.orangephi.tcg.adapters.RealmCardAdapter;
import ec.orangephi.tcg.intefaces.CardCollector;
import ec.orangephi.tcg.models.CardModel;
import ec.orangephi.tcg.utils.DeckUtils;
import ec.orangephi.tcg.utils.DividerItemDecoration;
import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmResults;

/**
 * Created by gesuwall on 11/26/15.
 */
public class CardPageActivity extends ShareActivity implements CardCollector{
    private Realm cardRealm;
    private RealmAsyncTask activeTask;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private RealmCardAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_card_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                IntentIntegrator integrator = new IntentIntegrator(CardPageActivity.this);
                integrator.initiateScan();
            }
        });
        cardRealm = Realm.getInstance(this);
        initCards();
    }
    /**
     * Inicializa la baraja por defecto de manera asincrona. Despues de que la baraja este lista en
     * Realm, inicializa el listview.
     */
    private void initCards(){
        activeTask = DeckUtils.initDeck(cardRealm, getResources().getStringArray(R.array.cardlist),
                new Realm.Transaction.Callback() {
                    @Override
                    public void onError(Exception e) {
                        Log.d("CardListActivity", e.getMessage());
                        activeTask = null;
                        initListView();
                    }

                    @Override
                    public void onSuccess() {
                        activeTask = null;
                        initListView();
                    }
                });
    }

    /**
     * Saca todas las cartas de Realm, crea el adapter y se lo coloca al RecycleView para mostrar
     * la coleccion en pantalla.
     */
    private void initListView(){
        RealmResults<CardModel> results =  cardRealm.where(CardModel.class).findAllAsync();
        results.load();

        mPager = (ViewPager) findViewById(R.id.pager);
        adapter = new RealmCardAdapter(getSupportFragmentManager(), results);
        mPager.setAdapter(adapter);


    }
   public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
          // handle scan result
            String code = scanResult.getContents();
            Log.d("CardListActivty", "scanned: " + code);
            CardModel newCard = addCardToCollection(code);
            if(newCard != null && newCard.getQuantity() == 1)
                viewNewCard(code);
            else if(newCard != null)
                viewCard(code);
        }
    }


    /**
     * Agrega una carta a la coleccion. Busca la carta en Realm por el id y le suma 1 unidad a la
     * cantidad
     * @param id id de la carta a agregar a la coleccion.
     * @return true si el codigo ingresado fue valido y se pudo agregar la carta.
     */
    private CardModel addCardToCollection(String id){
        if(id == null)
            return null;

        CardModel card = cardRealm.where(CardModel.class).equalTo("code", id).findFirst();
        if(card != null) {
            cardRealm.beginTransaction();
            card.setQuantity(card.getQuantity() + 1);
            cardRealm.commitTransaction();

            if(adapter != null)
                adapter.notifyDataSetChanged();
            else
                Log.d("CardPageActivity", "cant update adapter");
        }
        return card;
    }
    @Override
    public void viewCard(String code) {
        Intent intent = new Intent(this, CardViewActivity.class);
        intent.putExtra(CardViewActivity.CardCode, code);
        startActivity(intent);
    }

    public void viewNewCard(String code) {
        Intent intent = new Intent(this, CardViewActivity.class);
        intent.putExtra(CardViewActivity.CardCode, code);
        intent.putExtra(CardViewActivity.NewCard, true);
        startActivity(intent);
    }
}
