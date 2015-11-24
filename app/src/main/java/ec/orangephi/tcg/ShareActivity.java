package ec.orangephi.tcg;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import io.fabric.sdk.android.Fabric;

/**
 * Created by gesuwall on 11/24/15.
 */
public class ShareActivity extends AppCompatActivity {
    protected CallbackManager callbackManager;
    protected ShareDialog shareDialog;
    private AsyncTask<Bitmap, Void, Uri> pendingTask;
    private static String tempIMG = "/temporalImage.png";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        // this part is optional
        //shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() { ... });

        TwitterAuthConfig authConfig =  new TwitterAuthConfig("consumerKey", "consumerSecret");
        Fabric.with(this, new TwitterCore(authConfig), new TweetComposer());
    }

    protected void sharePhotoFB(Bitmap image){
        if(shareDialog.canShow(SharePhotoContent.class)) {
            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(image)
                    .build();
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();
            shareDialog.show(this, content);
        } else {
            Log.d("ShareActivity", "Cant share photo");
            Toast.makeText(this, getResources().getText(R.string.installFB), Toast.LENGTH_SHORT).show();
        }
    }

    protected void sharePhotoTW(Bitmap bmp){
        /*
        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + getResources().getResourcePackageName(drawable)
                + '/' + getResources().getResourceTypeName(drawable) + '/' + getResources().getResourceEntryName(drawable));

        Log.d("ShareActivity", uri.toString());*/
        pendingTask = new AsyncTask<Bitmap, Void, Uri>() {
            @Override
            protected Uri doInBackground(Bitmap... params) {
                try {
                    OutputStream out = new FileOutputStream(Environment.getExternalStorageDirectory() + tempIMG);
                    params[0].compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    return Uri.parse(Environment.getExternalStorageDirectory() + tempIMG);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Uri uri) {
                pendingTask = null;
                if(uri != null) {
                    Log.d("ShareActivity", uri.toString());
                    TweetComposer.Builder builder = new TweetComposer.Builder(ShareActivity.this)
                            .text("just setting up my Fabric.")
                            .image(uri);
                    builder.show();
                }
            }
        };
        pendingTask.execute(bmp);

    }

    @Override
    protected void onStop() {
        if(pendingTask != null)
            pendingTask.cancel(true);
        super.onStop();
    }
}
