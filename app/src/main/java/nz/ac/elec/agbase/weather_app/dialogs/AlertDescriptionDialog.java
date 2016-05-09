package nz.ac.elec.agbase.weather_app.dialogs;

import android.app.Activity;

import nz.ac.elec.agbase.weather_app.dialogs.base_classes.EditTextDialog;

/**
 * Created by tm on 26/04/16.
 */
public class AlertDescriptionDialog extends EditTextDialog {

    private final String TAG = "AlertDescriptionDialog";

    private IAlertDescriptionDialog mCallback;
    public interface IAlertDescriptionDialog {
        void getDescription(String description);
    }

    public AlertDescriptionDialog(Activity activity, String dialogHeader, String editTextValue) {
        super(activity, dialogHeader, editTextValue);
        mCallback = (IAlertDescriptionDialog)activity;
        mTextInput.setSingleLine(false);
    }

    @Override
    protected void onOkClick() {
        String description = mTextInput.getText().toString();
        mCallback.getDescription(description);
        dialog.dismiss();
    }
}
