package nz.ac.elec.agbase.weather_app.create_alert_dialogs;

import android.app.Activity;
import android.widget.RadioButton;

import nz.ac.elec.agbase.weather_app.create_alert_dialogs.base_classes.RadioButtonDialog;

/**
 * Created by tm on 27/04/16.
 */
public class AirPressureConditionDialog extends RadioButtonDialog {

    private IAirPressureConditionDialog mCallback;
    public interface IAirPressureConditionDialog {
        void getAirPressureCondition(String condition);
    }

    public AirPressureConditionDialog(Activity activity, String dialogHeader, String[] radioLabels, String selectedLabel) {
        super(activity, dialogHeader, radioLabels, selectedLabel);
        mCallback = (IAirPressureConditionDialog)activity;
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
