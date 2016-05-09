package nz.ac.elec.agbase.weather_app.dialogs;

import android.app.Activity;
import android.widget.RadioButton;

import nz.ac.elec.agbase.weather_app.dialogs.base_classes.RadioButtonDialog;

/**
 * Created by tm on 27/04/16.
 */
public class WindSpeedConditionDialog extends RadioButtonDialog {

    private IWindSpeedConditionDialog mCallback;
    public interface IWindSpeedConditionDialog {
        void getWindSpeedCondition(String condition);
    }

    public WindSpeedConditionDialog(Activity activity, String dialogHeader, String[] radioLabels, String selectedLabel) {
        super(activity, dialogHeader, radioLabels, selectedLabel);
        mCallback = (IWindSpeedConditionDialog)activity;
    }

    @Override
    protected void onOkClick() {
        int radioButtonId = rBtnGroup.getCheckedRadioButtonId();

        RadioButton btn = (RadioButton)rBtnGroup.findViewById(radioButtonId);
        String condition = btn.getText().toString();

        mCallback.getWindSpeedCondition(condition);
        dialog.dismiss();
    }
}
