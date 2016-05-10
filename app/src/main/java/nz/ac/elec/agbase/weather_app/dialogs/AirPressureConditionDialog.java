package nz.ac.elec.agbase.weather_app.dialogs;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.RadioButton;

import nz.ac.elec.agbase.weather_app.dialogs.base_classes.RadioButtonDialog;

/**
 * Created by tm on 27/04/16.
 */
public class AirPressureConditionDialog extends RadioButtonDialog {

    private IAirPressureConditionDialog mCallback;
    public interface IAirPressureConditionDialog {
        void getAirPressureCondition(String condition);
    }

    public AirPressureConditionDialog(Context context, String dialogHeader, String[] radioLabels, String selectedLabel) {
        super(context, dialogHeader, radioLabels, selectedLabel);
        mCallback = (IAirPressureConditionDialog)context;
    }

    public AirPressureConditionDialog(Fragment fragment, String dialogHeader, String[] radioLabels, String selectedLabel) {
        super(fragment.getContext(), dialogHeader, radioLabels, selectedLabel);
        mCallback = (IAirPressureConditionDialog)fragment;
    }

    @Override
    protected void onOkClick() {
        int radioButtonId = rBtnGroup.getCheckedRadioButtonId();

        RadioButton btn = (RadioButton)rBtnGroup.findViewById(radioButtonId);
        String condition = btn.getText().toString();

        mCallback.getAirPressureCondition(condition);
        dialog.dismiss();
    }
}
