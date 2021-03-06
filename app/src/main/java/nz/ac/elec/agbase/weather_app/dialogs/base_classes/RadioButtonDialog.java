package nz.ac.elec.agbase.weather_app.dialogs.base_classes;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import nz.ac.elec.agbase.weather_app.R;

/**
 * Created by tm on 26/04/16.
 */
public class RadioButtonDialog extends WeatherAlertDialog {

    protected TextView mDialogHeader;
    protected RadioGroup rBtnGroup;

    public RadioButtonDialog(Context context, String dialogHeader, String[] radioLabels, String selectedLabel) {
        super(context, R.layout.dialog_radio_button);
        mDialogHeader.setText(dialogHeader);
        initRadioBtns(context, radioLabels, selectedLabel);
    }

    private void initRadioBtns(Context context, String[] radioLabels, String selectedLabel) {

        final RadioButton[] rBtns = new RadioButton[radioLabels.length];

        for(int i = 0; i < rBtns.length; i++) {
            rBtns[i] = new RadioButton(context);
            rBtnGroup.addView(rBtns[i]);
            rBtns[i].setText(radioLabels[i]);

            if(radioLabels[i].equals(selectedLabel)) {
                rBtns[i].setChecked(true);
            }
        }
    }

    @Override
    protected void init(Context context) {
        mDialogHeader = (TextView)body.findViewById(R.id.dialog_radio_button_name_title);
        rBtnGroup = (RadioGroup)body.findViewById(R.id.dialog_radio_button_group);

        okBtn = (Button)body.findViewById(R.id.dialog_radio_button_ok_btn);
        okBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOkClick();
            }
        });

        cancelBtn = (Button)body.findViewById(R.id.dialog_radio_button_cancel_btn);
        cancelBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelClick();
            }
        });
    }


    protected void onCancelClick() {
        dialog.dismiss();
    }

    protected void onOkClick() {
        dialog.dismiss();
    }
}
