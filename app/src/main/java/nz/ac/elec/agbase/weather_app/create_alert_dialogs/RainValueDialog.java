package nz.ac.elec.agbase.weather_app.create_alert_dialogs;

import android.app.Activity;
import android.text.InputType;

import nz.ac.elec.agbase.weather_app.create_alert_dialogs.base_classes.EditTextDialog;

/**
 * Created by tm on 27/04/16.
 */
public class RainValueDialog extends EditTextDialog {

    private IRainValueDialog mCallback;
    public interface IRainValueDialog {
        void getRainValue(double rainValue);
    }

    public RainValueDialog(Activity activity, String dialogHeader, String editTextValue) {
        super(activity, dialogHeader, editTextValue);
        mCallback = (IRainValueDialog)activity;
        mTextInput.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED |
                InputType.TYPE_CLASS_NUMBER);
    }

    @Override
    protected void onOkClick() {
        double rainValue = 0.0;
        try {
            rainValue = Double.parseDouble(mTextInput.getText().toString());
        }
        catch(NumberFormatException e) {
            e.printStackTrace();
        }
        mCallback.getRainValue(rainValue);
        dialog.dismiss();
    }
}
