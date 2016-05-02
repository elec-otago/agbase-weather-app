package nz.ac.elec.agbase.weather_app.create_alert_dialogs;

import android.app.Activity;
import android.widget.RadioButton;

import nz.ac.elec.agbase.weather_app.create_alert_dialogs.base_classes.RadioButtonDialog;

/**
 * Created by tm on 26/04/16.
 */
public class TempConditionDialog extends RadioButtonDialog {

    private final String TAG = "TempConditionDialog";

    private ITempConditionDialog mCallback;
    public interface ITempConditionDialog {
        void getTempCondition(String condition);
    }

    public TempConditionDialog(Activity activity, String dialogHeader,
                               String[] radioLabels, String selectedLabel) {
        super(activity, dialogHeader, radioLabels, selectedLabel);
        mCallback = (ITempConditionDialog) activity;
    }

    @Override
    protected void onOkClick() {
        int radioButtonId = rBtnGroup.getCheckedRadioButtonId();

        RadioButton btn = (RadioButton)rBtnGroup.findViewById(radioButtonId);
        String condition = btn.getText().toString();

        mCallback.getTempCondition(condition);
        dialog.dismiss();
    }
}
