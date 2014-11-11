package pl.wroc.uni.ii.kokodzambocar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by michal on 23.10.14.
 */
public class Commons {
    public static void showNotImplemented(Context ctx) {
        AlertDialog dialog = new AlertDialog.Builder(ctx)
                .setTitle("Alert")
                .setMessage("To be implemented")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                .show();
    }
}
