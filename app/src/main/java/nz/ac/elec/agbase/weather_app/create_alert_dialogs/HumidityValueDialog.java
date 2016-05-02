package nz.ac.elec.agbase.weather_app.create_alert_dialogs;

import android.app.Activity;
import android.text.InputType;

import nz.ac.elec.agbase.weather_app.create_alert_dialogs.base_classes.EditTextDialog;

/**
 * Created by tm on 26/04/16.
 */
public class HumidityValueDialog extends EditTextDialog {

    private final String TAG = "HumidityValueDialog";

    private IHumidityValueDialog mCallback;
    public interface IHumidityValueDialog {
        void getHumidityValue(double humidityValue);
    }

    public HumidityValueDialog(Activity activity, String dialogHeader, String editTextValue) {
        super(activity, dialogHeader, editTextValue);
        mCallback = (IHumidityValueDialog)activity;
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
