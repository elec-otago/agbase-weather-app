package nz.ac.elec.agbase.weather_app.dialogs;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.InputType;

import nz.ac.elec.agbase.weather_app.dialogs.base_classes.EditTextDialog;

/**
 * Created by tm on 27/04/16.
 */
public class RainValueDialog extends EditTextDialog {

    private IRainValueDialog mCallback;
    public interface IRainValueDialog {
        void getRainValue(double rainValue);
    }

    public RainValueDialog(Context context, String dialogHeader, String editTextValue) {
        super(context, dialogHeader, editTextValue);
        mCallback = (IRainValueDialog)context;
        mTextInput.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED |
                InputType.TYPE_CLASS_NUMBER);
    }

    public RainValueDialog(Fragment fragment, String dialogHeader, String editTextValue) {
        super(fragment.getContext(), dialogHeader, editTextValue);
        mCallback = (IRainValueDialog)fragment;
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
