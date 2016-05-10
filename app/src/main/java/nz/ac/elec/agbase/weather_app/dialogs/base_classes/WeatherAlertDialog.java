package nz.ac.elec.agbase.weather_app.dialogs.base_classes;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

/**
 * WeatherAlertDialog.java
 *
 * Base class for alert dialogs used by the Weather Alert app.
 * All alert dialog classes should descend from this class
 *
 * Created by tm on 26/04/16.
 */
public abstract class WeatherAlertDialog {

    protected View body;
    protected Button okBtn, cancelBtn;

    protected AlertDialog dialog;
    public AlertDialog getDialog() { return this.dialog; }

    public WeatherAlertDialog(Context context, int layoutId) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        body = inflater.inflate(layoutId, null);
        init(context);
        buildDialog(context);
    }

    private void buildDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(body);
        dialog = builder.create();
    }

    // initialization to be implemented by child classes
    protected abstract void init(Context context);
}
