package nz.ac.elec.agbase.weather_app.dialogs.base_classes;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import nz.ac.elec.agbase.weather_app.R;

/**
 * EditTextDialog.java
 *
 * Base class for alert dialogs that include an edit text component.
 * All alert dialog classes that descend from this should override the
 * onOkClick function.
 *
 * Created by tm on 26/04/16.
 */
public class EditTextDialog extends WeatherAlertDialog {

    protected TextView mDialogHeader;
    protected EditText mTextInput;

    private Button okBtn, cancelBtn;

    public EditTextDialog(Context context, String dialogHeader, String editTextValue) {
        super(context, R.layout.dialog_text_input);
        mTextInput.setText(editTextValue);
        mDialogHeader.setText(dialogHeader);
    }

    @Override
    protected void init(Context context) {
        mDialogHeader = (TextView)body.findViewById(R.id.dialog_text_input_name_title);
        mTextInput = (EditText)body.findViewById(R.id.dialog_text_input_name_input);

        okBtn = (Button)body.findViewById(R.id.dialog_text_input_ok_btn);
        okBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOkClick();
            }
        });

        cancelBtn = (Button)body.findViewById(R.id.dialog_text_input_cancel_btn);
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
