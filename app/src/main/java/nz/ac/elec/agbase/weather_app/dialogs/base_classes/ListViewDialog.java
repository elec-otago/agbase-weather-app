package nz.ac.elec.agbase.weather_app.dialogs.base_classes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import nz.ac.elec.agbase.weather_app.R;
import nz.ac.elec.agbase.weather_app.models.WeatherAlert;

/**
 * Created by tm on 23/08/16.
 */
public class ListViewDialog extends WeatherAlertDialog {

    protected TextView mDialogHeader;
    protected RecyclerView mListView;

    public ListViewDialog(Context context, String dialogHeader) {
        super(context, R.layout.dialog_list_view);
        mDialogHeader.setText(dialogHeader);
    }

    @Override
    protected void init(Context context) {
        mDialogHeader = (TextView)body.findViewById(R.id.dialog_list_view_name_title);
        mListView = (RecyclerView)body.findViewById(R.id.dialog_list_view_list_output);

        okBtn = (Button)body.findViewById(R.id.dialog_list_view_ok_btn);
        okBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOkClick();
            }
        });

        cancelBtn = (Button)body.findViewById(R.id.dialog_list_view_cancel_btn);
        cancelBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelClick();
            }
        });
    }

    protected void onCancelClick() {
        dialog.dismiss();
    }

    protected void onOkClick() {
        dialog.dismiss();
    }
}
