package nz.ac.elec.agbase.weather_app.dialogs;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.InputType;

import nz.ac.elec.agbase.weather_app.dialogs.base_classes.EditTextDialog;

/**
 * Created by tm on 26/04/16.
 */
public class HumidityValueDialog extends EditTextDialog {

    private final String TAG = "HumidityValueDialog";

    private IHumidityValueDialog mCallback;
    public interface IHumidityValueDialog {
        void getHumidityValue(double humidityValue);
    }

    public HumidityValueDialog(Context context, String dialogHeader, String editTextValue) {
        super(context, dialogHeader, editTextValue);
        mCallback = (IHumidityValueDialog)context;
        mTextInput.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL| InputType.TYPE_NUMBER_FLAG_SIGNED |
                InputType.TYPE_CLASS_NUMBER);
    }

    public HumidityValueDialog(Fragment fragment, String dialogHeader, String editTextValue) {
        super(fragment.getContext(), dialogHeader, editTextValue);
        mCallback = (IHumidityValueDialog)fragment;
        mTextInput.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL| InputType.TYPE_NUMBER_FLAG_SIGNED |
                InputType.TYPE_CLASS_NUMBER);
    }

    @Override
    protected void onOkClick() {
        double humidityValue = 0.0;
        try {
            humidityValue = Double.parseDouble(mTextInput.getText().toString());
        }
        catch(NumberFormatException e) {
            e.printStackTrace();
        }
        mCallback.getHumidityValue(humidityValue);
        dialog.dismiss();
    }
}
