package nz.ac.elec.agbase.weather_app.dialogs;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import nz.ac.elec.agbase.weather_app.dialogs.base_classes.EditTextDialog;

/**
 * Created by tm on 26/04/16.
 */
public class AlertNameDialog extends EditTextDialog {

    private final String TAG = "AlertNameDialog";

    private IAlertNameDialog mCallback;
    public interface IAlertNameDialog {
        void getName(String name);
    }

    public AlertNameDialog(Context context, String dialogHeader, String editTextValue) {
        super(context, dialogHeader, editTextValue);
        mCallback = (IAlertNameDialog)context;
    }

    public AlertNameDialog(Fragment fragment, String dialogHeader, String editTextValue) {
        super(fragment.getContext(), dialogHeader, editTextValue);
        mCallback = (IAlertNameDialog)fragment;
    }

    @Override
    protected void onOkClick() {
        String name = mTextInput.getText().toString();
        mCallback.getName(name);
        dialog.dismiss();
    }
}
