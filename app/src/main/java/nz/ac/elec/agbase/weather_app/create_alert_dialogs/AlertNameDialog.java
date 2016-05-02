package nz.ac.elec.agbase.weather_app.create_alert_dialogs;

import android.app.Activity;

import nz.ac.elec.agbase.weather_app.create_alert_dialogs.base_classes.EditTextDialog;

/**
 * Created by tm on 26/04/16.
 */
public class AlertNameDialog extends EditTextDialog {

    private final String TAG = "AlertNameDialog";

    private IAlertNameDialog mCallback;
    public interface IAlertNameDialog {
        void getName(String name);
    }

    public AlertNameDialog(Activity activity, String dialogHeader, String editTextValue) {
        super(activity, dialogHeader, editTextValue);
        mCallback = (IAlertNameDialog)activity;
    }
    @Override
    protected void onOkClick() {
        String name = mTextInput.getText().toString();
        mCallback.getName(name);
        dialog.dismiss();
    }
}
