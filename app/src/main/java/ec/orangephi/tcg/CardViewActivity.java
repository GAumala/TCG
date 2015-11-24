package ec.orangephi.tcg;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class CardViewActivity extends ShareActivity {

    public static String CardCode = "CardViewActivity.CardCode";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bmp = ((BitmapDrawable)ContextCompat.getDrawable(CardViewActivity.this, R.drawable.ajna1)).getBitmap();
                //sharePhotoFB(bmp);
                sharePhotoTW(bmp);
            }
        });
    }
}
