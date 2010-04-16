package at.ac.tuwien.hci.ghost.ui.run;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import at.ac.tuwien.hci.ghost.R;
import at.ac.tuwien.hci.ghost.R.layout;

public class StartRunActivity extends Activity {
	private static final int DIALOG_BLOED = 101;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //showDialog(DIALOG_BLOED);
    }
    
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = null;
        switch(id) {
        case DIALOG_BLOED:
            builder = new AlertDialog.Builder(this);
            builder.setMessage("Wer das liest ist blšd!");
            builder.setNeutralButton("Eh kloa!", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
               }
            });
            dialog = builder.create();
            break;
        default:
        	dialog = null;
        }
        return dialog;
    }
}