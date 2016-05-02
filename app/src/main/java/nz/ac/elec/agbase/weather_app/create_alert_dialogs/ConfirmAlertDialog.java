package nz.ac.elec.agbase.weather_app.create_alert_dialogs;

import android.app.Activity;

import nz.ac.elec.agbase.weather_app.create_alert_dialogs.base_classes.TextViewDialog;

/**
 * Created by tm on 2/05/16.
 */
public class ConfirmAlertDialog extends TextViewDialog{

    private IConfirmAlertDialog mCallback;
    public interface IConfirmAlertDialog {
        void confirmOk();
    }

    public ConfirmAlertDialog(Activity activity, String dialogHeader, String dialogBody) {
        super(activity, dialogHeader, dialogBody);
        mCallback = (IConfirmAlertDialog)activity;
    }

    @Override
    protected void onOkClick() {
        mCallback.confirmOk();
        dialog.dismiss();
    }
}