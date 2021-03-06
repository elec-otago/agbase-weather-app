package nz.ac.elec.agbase.weather_app.dialogs;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.InputType;

import nz.ac.elec.agbase.weather_app.dialogs.base_classes.EditTextDialog;

/**
 * Created by tm on 26/04/16.
 */
public class TempValueDialog extends EditTextDialog {

    private final String TAG ="AlertTempValueDialog";

    private ITempValueDialog mCallback;
    public interface ITempValueDialog {
        void getTempValue(double tempValue);
    }

    public TempValueDialog(Context context, String dialogHeader, String editTextValue) {
        super(context, dialogHeader, editTextValue);
        mCallback = (ITempValueDialog)context;
        mTextInput.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL| InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_CLASS_NUMBER);
    }

    public TempValueDialog(Fragment fragment, String dialogHeader, String editTextValue) {
        super(fragment.getContext(), dialogHeader, editTextValue);
        mCallback = (ITempValueDialog)fragment;
        mTextInput.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL| InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_CLASS_NUMBER);
    }

    @Override
    protected void onOkClick() {
        double tempValue = 0.0;
        try {
            tempValue = Double.parseDouble(mTextInput.getText().toString());
        }
        catch(NumberFormatException e) {
            e.printStackTrace();
        }
        mCallback.getTempValue(tempValue);
        dialog.dismiss();
    }
}
