package ec.orangephi.tcg;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import ec.orangephi.tcg.ZXing.IntentIntegrator;
import ec.orangephi.tcg.ZXing.IntentResult;
import ec.orangephi.tcg.adapters.RealmCardAdapter;
import ec.orangephi.tcg.fragments.AdFragment;
import ec.orangephi.tcg.fragments.CardPageFragment;
import ec.orangephi.tcg.fragments.NewCardsDialog;
import ec.orangephi.tcg.intefaces.CardCollector;
import ec.orangephi.tcg.models.CardModel;
import ec.orangephi.tcg.utils.DeckUtils;
import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmResults;

/**
 * Created by gesuwall on 11/26/15.
 */
public class CardPageActivity extends ShareActivity implements CardCollector{
    public static String FirstPage = "CardPageActivity.FirstPage";
    private Realm cardRealm;
    private RealmAsyncTask activeTask;
    private DiscreteSeekBar seekBar;

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

        getSupportActionBar().setTitle(getString(R.string.collection));

        ImageView ad = (ImageView) findViewById(R.id.frutanga);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            ad.setVisibility(View.GONE);

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
        initSeekBar();
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
                        initViewPager();
                    }

                    @Override
                    public void onSuccess() {
                        activeTask = null;
                        initViewPager();
                    }
                });
    }

    /**
     * Saca todas las cartas de Realm, crea el adapter y se lo coloca al RecycleView para mostrar
     * la coleccion en pantalla.
     */
    private void initViewPager(){
        RealmResults<CardModel> results =  cardRealm.where(CardModel.class).findAllAsync();
        results.load();

        mPager = (ViewPager) findViewById(R.id.pager);
        adapter = new RealmCardAdapter(getSupportFragmentManager(), results);
        mPager.setAdapter(adapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(seekBar != null)
                    seekBar.setProgress(position + 1);
                else
                    Log.d("CardPageActivty", "Can't update null seekbar");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        int firstPage = getIntent().getIntExtra(FirstPage, 0);
        if(firstPage > 0)
            mPager.setCurrentItem(firstPage);


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

        }
        return card;
    }

    private void initSeekBar(){
        seekBar = (DiscreteSeekBar) findViewById(R.id.seekbar);
        seekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
                if(mPager != null)
                    mPager.setCurrentItem(seekBar.getProgress() - 1);
                else
                    Log.d("CardPageActivity", "cant update null Pager");
            }
        });
    }

    public void showAdDialog(View view){
            FragmentManager fm = getSupportFragmentManager();
            AdFragment dialog = new AdFragment();
            dialog.show(fm, "fragment_ad");

    }
    @Override
    public void viewCard(String code) {
        Intent intent = new Intent(this, CardViewActivity.class);
        intent.putExtra(CardViewActivity.CardCode, code);
        startActivity(intent);
    }

    @Override
    public void showNewCardDialog(){
            FragmentManager fm = getSupportFragmentManager();
            NewCardsDialog editNameDialog = new NewCardsDialog();
            editNameDialog.show(fm, "fragment_new_cards");

    }

    public void viewNewCard(String code) {
        Intent intent = new Intent(this, CardViewActivity.class);
        intent.putExtra(CardViewActivity.CardCode, code);
        intent.putExtra(CardViewActivity.NewCard, true);
        startActivity(intent);
        finish();
    }
}
