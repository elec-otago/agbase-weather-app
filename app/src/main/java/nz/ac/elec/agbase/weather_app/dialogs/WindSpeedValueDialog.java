package nz.ac.elec.agbase.weather_app.dialogs;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.InputType;

import nz.ac.elec.agbase.weather_app.dialogs.base_classes.EditTextDialog;

/**
 * Created by tm on 27/04/16.
 */
public class WindSpeedValueDialog extends EditTextDialog {

    private IWindSpeedValueDialog mCallback;
    public interface IWindSpeedValueDialog {
        void getWindSpeedValue(double windSpeedValue);
    }

    public WindSpeedValueDialog(Context context, String dialogHeader, String editTextValue) {
        super(context, dialogHeader, editTextValue);
        mTextInput.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_CLASS_NUMBER);
        mCallback = (IWindSpeedValueDialog)context;
    }

    public WindSpeedValueDialog(Fragment fragment, String dialogHeader, String editTextValue) {
        super(fragment.getContext(), dialogHeader, editTextValue);
        mTextInput.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_CLASS_NUMBER);
        mCallback = (IWindSpeedValueDialog)fragment;
    }

    @Override
    protected void onOkClick() {
        double windSpeedValue = 0.0;
        try {
            windSpeedValue = Double.parseDouble(mTextInput.getText().toString());
        }
        catch(NumberFormatException e) {
            e.printStackTrace();
        }
        mCallback.getWindSpeedValue(windSpeedValue);
        dialog.dismiss();
    }
}
