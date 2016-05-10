package nz.ac.elec.agbase.weather_app.dialogs.base_classes;


import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import nz.ac.elec.agbase.weather_app.R;

/**
 * TextViewDialog.java
 *
 * Base class for dialogs that contain a text view component.
 *
 * Created by tm on 2/05/16.
 */
public class TextViewDialog extends WeatherAlertDialog  {

    protected TextView mDialogHeader, mContentBody;

    private Button okBtn, cancelBtn;

    public TextViewDialog(Context context, String dialogHeader, String dialogBody) {
        super(context, R.layout.dialog_confirm);
        mDialogHeader.setText(dialogHeader);
        mContentBody.setText(dialogBody);
    }

    @Override
    protected void init(Context context) {
        mDialogHeader = (TextView)body.findViewById(R.id.dialog_confirm_dialog_header);
        mContentBody = (TextView)body.findViewById(R.id.dialog_confirm_dialog_body);

        okBtn = (Button)body.findViewById(R.id.dialog_confirm_input_ok_btn);
        okBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOkClick();
            }
        });

        cancelBtn = (Button)body.findViewById(R.id.dialog_confirm_input_cancel_btn);
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
