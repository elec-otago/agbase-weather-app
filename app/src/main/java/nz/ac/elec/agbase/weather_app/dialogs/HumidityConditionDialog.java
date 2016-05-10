package nz.ac.elec.agbase.weather_app.dialogs;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.RadioButton;

import nz.ac.elec.agbase.weather_app.dialogs.base_classes.RadioButtonDialog;

/**
 * Created by tm on 26/04/16.
 */
public class HumidityConditionDialog extends RadioButtonDialog {

    private IHumidityConditionDialog mCallback;
    public interface IHumidityConditionDialog {
        void getHumidityCondition(String condition);
    }

    public HumidityConditionDialog(Context context, String dialogHeader, String[] radioLabels, String selectedLabel) {
        super(context, dialogHeader, radioLabels, selectedLabel);
        mCallback = (IHumidityConditionDialog)context;
    }

    public HumidityConditionDialog(Fragment fragment, String dialogHeader, String[] radioLabels, String selectedLabel) {
        super(fragment.getContext(), dialogHeader, radioLabels, selectedLabel);
        mCallback = (IHumidityConditionDialog)fragment;
    }

    @Override
    protected void onOkClick() {
        int radioButtonId = rBtnGroup.getCheckedRadioButtonId();

        RadioButton btn = (RadioButton)rBtnGroup.findViewById(radioButtonId);
        String condition = btn.getText().toString();

        mCallback.getHumidityCondition(condition);
        dialog.dismiss();
    }
}
