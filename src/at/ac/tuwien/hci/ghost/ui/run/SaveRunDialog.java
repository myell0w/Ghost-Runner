package at.ac.tuwien.hci.ghost.ui.run;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import at.ac.tuwien.hci.ghost.R;

public class SaveRunDialog extends Dialog {
	
	public interface ReadyListener { 
        public void readyToFinishActivity(boolean saveRun, boolean saveRunAsRoute); 
   } 

	private boolean saveAsRoute;
	private ReadyListener readyListener; 

	public SaveRunDialog(Context context, boolean saveAsRoute, ReadyListener readyListener) {
		super(context);
		
		this.saveAsRoute = saveAsRoute;
		this.readyListener = readyListener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.saverun_dialog);
		setTitle(getContext().getResources().getString(R.string.run_save));

		CheckBox cbSaveAsRoute = (CheckBox) findViewById(R.id.saveAsRoute);
		cbSaveAsRoute.setChecked(saveAsRoute);

		Button buttonSave = (Button) findViewById(R.id.buttonSave);
		buttonSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CheckBox cbSaveAsRoute = (CheckBox) findViewById(R.id.saveAsRoute);
				
				readyListener.readyToFinishActivity(true, cbSaveAsRoute.isChecked());
				
				SaveRunDialog.this.dismiss();
			}
		});

		Button buttonDiscard = (Button) findViewById(R.id.buttonDiscard);
		buttonDiscard.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				readyListener.readyToFinishActivity(false, false);
				SaveRunDialog.this.dismiss();
			}
		});
	}
}
