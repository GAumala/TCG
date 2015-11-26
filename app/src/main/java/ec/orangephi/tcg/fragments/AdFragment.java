package ec.orangephi.tcg.fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ec.orangephi.tcg.R;
import ec.orangephi.tcg.intefaces.CardCollector;

/**
 * Created by gesuwall on 11/26/15.
 */
public class AdFragment extends DialogFragment{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.layout_dialog)
        .setTitle("Auspiciantes")
        .setCancelable(true);
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
