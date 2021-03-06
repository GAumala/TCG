package ec.orangephi.tcg;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import ec.orangephi.tcg.models.CardModel;
import ec.orangephi.tcg.utils.DeckUtils;
import io.realm.Realm;

public class CardViewActivity extends ShareActivity {

    public static String CardCode = "CardViewActivity.CardCode";
    public static String NewCard = "CardViewActivity.NewCard";
    private Realm cardRealm;
    private MediaPlayer player;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView textView = (TextView) findViewById(R.id.text_view);

        setSupportActionBar(toolbar);
        cardRealm = Realm.getInstance(this);
        String code = getIntent().getStringExtra(CardCode);
        final CardModel selectedCard = cardRealm.where(CardModel.class).equalTo("code", code).findFirst();
        final int index = selectedCard.getIndex();
        final String title =  "#" + selectedCard.getIndex();
        final String text = selectedCard.getText();

        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitle("#" + selectedCard.getIndex());
        textView.setText(text);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bmp = ((BitmapDrawable) ContextCompat.getDrawable(CardViewActivity.this,
                        DeckUtils.getDrawableFromIndex(index))).getBitmap();
                showShareDialog(getAvailableShareApps(), bmp, "Carta " + title);
            }
        });

        if(getIntent().getBooleanExtra(NewCard, false))
            player = MediaPlayer.create(this, R.raw.newcard);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(player != null){
            player.start();
            Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator), getString(R.string.new_card), Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(player != null){
            player.release();
            player = null;
        }
    }

    @Override
    public void onBackPressed() {
        if(getIntent().getBooleanExtra(NewCard, false)){
            String code = getIntent().getStringExtra(CardCode);
            CardModel card = cardRealm.where(CardModel.class).equalTo("code", code).findFirst();
            Intent intent = new Intent(this, CardPageActivity.class);
            intent.putExtra(CardPageActivity.FirstPage, card.getIndex() - 1);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            finish();
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if(cardRealm != null)
            cardRealm.close();
        super.onDestroy();
    }
}
