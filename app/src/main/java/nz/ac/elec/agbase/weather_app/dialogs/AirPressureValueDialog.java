package nz.ac.elec.agbase.weather_app.dialogs;

import android.app.Activity;
import android.text.InputType;

import nz.ac.elec.agbase.weather_app.dialogs.base_classes.EditTextDialog;

/**
 * Created by tm on 27/04/16.
 */
public class AirPressureValueDialog extends EditTextDialog {

    private IAirPressureValueDialog mCallback;
    public interface IAirPressureValueDialog {
        void getAirPressureValue(double airPressureValue);
    }

    public AirPressureValueDialog(Activity activity, String dialogHeader, String editTextValue) {
        super(activity, dialogHeader, editTextValue);
        mCallback = (IAirPressureValueDialog)activity;
        mTextInput.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL| InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_CLASS_NUMBER);
    }

    @Override
    protected void onOkClick() {
        double airPressureValue = 0.0;
        try {
            airPressureValue = Double.parseDouble(mTextInput.getText().toString());
        }
        catch(NumberFormatException e) {
            e.printStackTrace();
        }
        mCallback.getAirPressureValue(airPressureValue);
        dialog.dismiss();
    }
}
