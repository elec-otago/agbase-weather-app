package nz.ac.elec.agbase.weather_app.dialogs;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import nz.ac.elec.agbase.weather_app.dialogs.base_classes.TextViewDialog;

/**
 * Created by tm on 2/05/16.
 */
public class ConfirmAlertDialog extends TextViewDialog{

    private IConfirmAlertDialog mCallback;
    public interface IConfirmAlertDialog {
        void confirmOk();
        void cancelClick();
    }

    public ConfirmAlertDialog(Context context, String dialogHeader, String dialogBody) {
        super(context, dialogHeader, dialogBody);
        mCallback = (IConfirmAlertDialog)context;
    }

    public ConfirmAlertDialog(Fragment fragment, String dialogHeader, String dialogBody) {
        super(fragment.getContext(), dialogHeader, dialogBody);
        mCallback = (IConfirmAlertDialog)fragment;
    }

    @Override
    protected void onOkClick() {
        mCallback.confirmOk();
        dialog.dismiss();
    }

    @Override
    protected void onCancelClick() {
        mCallback.cancelClick();
        dialog.dismiss();
    }
}