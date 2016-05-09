package nz.ac.elec.agbase.weather_app.dialogs;

import android.app.Activity;
import android.widget.RadioButton;

import nz.ac.elec.agbase.weather_app.dialogs.base_classes.RadioButtonDialog;
import nz.ac.elec.agbase.weather_app.models.WeatherAlert;

/**
 * Created by tm on 9/05/16.
 */
public class EditDeleteAlertDialog extends RadioButtonDialog {

    private static String[] RBTN_LABELS = new String[] {"Edit", "Delete"};

    private WeatherAlert mAlert;

    private IEditDeleteAlertDialog mCallback;
    public interface IEditDeleteAlertDialog {
        void deleteAlert(WeatherAlert alert);
        void editAlert(WeatherAlert alert);
    }

    public EditDeleteAlertDialog(Activity activity, WeatherAlert alert) {
        super(activity, alert.getName(), RBTN_LABELS, RBTN_LABELS[0]);
        mCallback = (IEditDeleteAlertDialog)activity;
        mAlert = alert;
    }

    @Override
    protected void onOkClick() {

        int rBtnId = rBtnGroup.getCheckedRadioButtonId();
        RadioButton btn = (RadioButton)rBtnGroup.findViewById(rBtnId);
        String rbtnLabel = btn.getText().toString();

        if(rbtnLabel.equals(RBTN_LABELS[0])) {
            mCallback.editAlert(mAlert);
        }
        else if(rbtnLabel.equals(RBTN_LABELS[1])) {
            mCallback.deleteAlert(mAlert);
        }
        dialog.dismiss();
    }
}
