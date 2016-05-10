package nz.ac.elec.agbase.weather_app.dialogs;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.InputType;

import nz.ac.elec.agbase.weather_app.dialogs.base_classes.EditTextDialog;

/**
 * Created by tm on 27/04/16.
 */
public class SnowValueDialog extends EditTextDialog {

    private ISnowValueDialog mCallback;
    public interface ISnowValueDialog {
        void getSnowValue(double snowValue);
    }

    public SnowValueDialog(Context context, String dialogHeader, String editTextValue) {
        super(context, dialogHeader, editTextValue);
        mCallback = (ISnowValueDialog)context;
        mTextInput.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED |
                InputType.TYPE_CLASS_NUMBER);
    }

    public SnowValueDialog(Fragment fragment, String dialogHeader, String editTextValue) {
        super(fragment.getContext(), dialogHeader, editTextValue);
        mCallback = (ISnowValueDialog)fragment;
        mTextInput.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED |
                InputType.TYPE_CLASS_NUMBER);
    }

    @Override
    protected void onOkClick() {
        double snowValue = 0.0;
        try {
            snowValue = Double.parseDouble(mTextInput.getText().toString());
        }
        catch(NumberFormatException e) {
            e.printStackTrace();
        }
        mCallback.getSnowValue(snowValue);
        dialog.dismiss();
    }
}
