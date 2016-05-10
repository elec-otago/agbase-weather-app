package nz.ac.elec.agbase.weather_app.dialogs;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

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

    public AlertDescriptionDialog(Context context, String dialogHeader, String editTextValue) {
        super(context, dialogHeader, editTextValue);
        mCallback = (IAlertDescriptionDialog)context;
        mTextInput.setSingleLine(false);
    }

    public AlertDescriptionDialog(Fragment fragment, String dialogHeader, String editTextValue) {
        super(fragment.getContext(), dialogHeader, editTextValue);
        mCallback = (IAlertDescriptionDialog)fragment;
        mTextInput.setSingleLine(false);
    }

    @Override
    protected void onOkClick() {
        String description = mTextInput.getText().toString();
        mCallback.getDescription(description);
        dialog.dismiss();
    }
}
