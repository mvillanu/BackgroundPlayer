package net.infobosccoma.backgroundplayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

/**
 * Created by Maxi on 06/02/2015.
 */
public class PopUp{

public static void CustomDialogAlert(String title, String message, final Activity activity){
        AlertDialog alertDialog = new AlertDialog.Builder(
                activity).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.abc_textfield_search_material);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                Toast.makeText(activity, "You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }



}



