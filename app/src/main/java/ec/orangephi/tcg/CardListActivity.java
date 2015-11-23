package ec.orangephi.tcg;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import ec.orangephi.tcg.ZXing.IntentIntegrator;
import ec.orangephi.tcg.ZXing.IntentResult;
import ec.orangephi.tcg.adapters.RealmCardAdapter;
import ec.orangephi.tcg.intefaces.CardCollector;
import ec.orangephi.tcg.models.CardModel;
import ec.orangephi.tcg.utils.DeckUtils;
import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmResults;

public class CardListActivity extends AppCompatActivity implements CardCollector{

    private Realm cardRealm;
    private RealmAsyncTask activeTask;
    private RealmCardAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                IntentIntegrator integrator = new IntentIntegrator(CardListActivity.this);
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
        Log.d("CardListActivity", "initListView");
        RealmResults<CardModel> results =  cardRealm.where(CardModel.class).findAllAsync();
        results.load();
        Log.d("CardListActivity", "initListView with " + results.size() + " cards");
        adapter = new RealmCardAdapter(results);
        RecyclerView listView = (RecyclerView) findViewById(R.id.listview);
        listView.setLayoutManager(new LinearLayoutManager(this));
       listView.setAdapter(adapter);


    }
   public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
          // handle scan result
            String code = scanResult.getContents();
            Log.d("CardListActivty", "scanned: " + code);
            if(addCardToCollection(code))
                viewCard(code);
        }
    }

    /**
     * Agrega una carta a la coleccion. Busca la carta en Realm por el id y le suma 1 unidad a la
     * cantidad
     * @param id id de la carta a agregar a la coleccion.
     * @return true si el codigo ingresado fue valido y se pudo agregar la carta.
     */
    private boolean addCardToCollection(String id){
        CardModel card = cardRealm.where(CardModel.class).equalTo("code", id).findFirst();
        if(card != null) {
            cardRealm.beginTransaction();
            card.setQuantity(card.getQuantity() + 1);
            cardRealm.commitTransaction();

            adapter.notifyDataSetChanged();
            return true;
        }
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_card_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onStop () {
        if (activeTask != null && !activeTask.isCancelled()) {
            activeTask.cancel();
        }
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        if(cardRealm != null)
            cardRealm.close();
        super.onDestroy();
    }

    @Override
    public void viewCard(String code) {
        Intent intent = new Intent(this, CardViewActivity.class);
        intent.putExtra(CardViewActivity.CardCode, code);
        startActivity(intent);
    }
}
