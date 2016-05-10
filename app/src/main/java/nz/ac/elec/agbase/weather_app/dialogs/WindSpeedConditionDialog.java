package nz.ac.elec.agbase.weather_app.dialogs;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
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

    public WindSpeedConditionDialog(Context context, String dialogHeader, String[] radioLabels, String selectedLabel) {
        super(context, dialogHeader, radioLabels, selectedLabel);
        mCallback = (IWindSpeedConditionDialog)context;
    }

    public WindSpeedConditionDialog(Fragment fragment, String dialogHeader, String[] radioLabels, String selectedLabel) {
        super(fragment.getContext(), dialogHeader, radioLabels, selectedLabel);
        mCallback = (IWindSpeedConditionDialog)fragment;
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
