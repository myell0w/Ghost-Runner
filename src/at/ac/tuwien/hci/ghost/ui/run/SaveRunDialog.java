package at.ac.tuwien.hci.ghost.ui.run;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import at.ac.tuwien.hci.ghost.R;
import at.ac.tuwien.hci.ghost.data.entities.Run;
import at.ac.tuwien.hci.ghost.util.Date;

public class SaveRunDialog extends Dialog {

	public interface ReadyListener {
		public void readyToFinishActivity(boolean saveRun, boolean saveRunAsRoute, String routeName);
	}

	private ReadyListener readyListener;
	private Run run;

	public SaveRunDialog(Context context, Run run, ReadyListener readyListener) {
		super(context);

		this.readyListener = readyListener;
		this.run = run;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.saverun_dialog);
		setTitle(getContext().getResources().getString(R.string.run_save));

		CheckBox cbSaveAsRoute = (CheckBox) findViewById(R.id.saveAsRoute);
		EditText routeName = (EditText) findViewById(R.id.routeName);
		Button buttonSave = (Button) findViewById(R.id.buttonSave);
		TextView txtSaveAsRoute = (TextView) findViewById(R.id.textSaveAsRoute);
		TextView txtRouteName = (TextView) findViewById(R.id.textRouteName);
		
		if (run.hasRoute()) {
			if (cbSaveAsRoute != null) {
				cbSaveAsRoute.setVisibility(View.GONE);
				txtSaveAsRoute.setVisibility(View.GONE);
			}
			if (routeName != null) {
				routeName.setVisibility(View.GONE);
				txtRouteName.setVisibility(View.GONE);
			}
		} else {
			if (cbSaveAsRoute != null) {
				cbSaveAsRoute.setVisibility(View.VISIBLE);
				txtSaveAsRoute.setVisibility(View.VISIBLE);
				cbSaveAsRoute.setChecked(true);
			}

			if (routeName != null) {
				routeName.setVisibility(View.VISIBLE);
				txtRouteName.setVisibility(View.VISIBLE);
				routeName.setText(this.getContext().getResources().getString(R.string.routes_defaultName) + " " + new Date().toString("dd.MM.yyyy"));

			}
		}

		if (cbSaveAsRoute != null) {
			cbSaveAsRoute.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					EditText routeName = (EditText) findViewById(R.id.routeName);
					TextView txtRouteName = (TextView) findViewById(R.id.textRouteName);

					if (isChecked) {
						routeName.setVisibility(View.VISIBLE);
						txtRouteName.setVisibility(View.VISIBLE);
					} else {
						routeName.setVisibility(View.GONE);
						txtRouteName.setVisibility(View.GONE);
					}
				}
			});
		}

		buttonSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CheckBox cbSaveAsRoute = (CheckBox) findViewById(R.id.saveAsRoute);
				EditText txtRouteName = (EditText) findViewById(R.id.routeName);

				if (run.hasRoute()) {
					readyListener.readyToFinishActivity(true, false, null);
				} else {
					readyListener.readyToFinishActivity(true, cbSaveAsRoute.isChecked(), txtRouteName.getEditableText().toString());
				}

				SaveRunDialog.this.dismiss();
			}
		});

		Button buttonDiscard = (Button) findViewById(R.id.buttonDiscard);
		buttonDiscard.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				readyListener.readyToFinishActivity(false, false, null);
				SaveRunDialog.this.dismiss();
			}
		});
	}
}
