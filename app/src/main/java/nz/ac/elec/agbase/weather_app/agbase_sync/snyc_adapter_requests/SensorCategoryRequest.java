package nz.ac.elec.agbase.weather_app.agbase_sync.snyc_adapter_requests;

import android.os.Bundle;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import nz.ac.elec.agbase.android_agbase_api.AgBaseApi;
import nz.ac.elec.agbase.android_agbase_api.agbase_models.SensorCategory;
import nz.ac.elec.agbase.android_agbase_api.agbase_models.SensorType;
import nz.ac.elec.agbase.android_agbase_api.api_models.ApiSensorCategories;
import nz.ac.elec.agbase.android_agbase_db.AgBaseDatabaseManager;
import retrofit.Call;
import retrofit.Response;

/**
 * Created by tm on 28/04/16.
 */
public class SensorCategoryRequest extends SyncAdapterRequest<SensorCategory[]> {

    public static String ARGS_INCLUDE = "SensorCategoryRequest.ARGS_INCLUDE";
    public static String ARGS_NAME = "SensorCategoryRequest.ARGS_NAME";

    private SensorCategory[] sensorCategories;

    @Override
    public boolean performRequest(Bundle extras) {
        String include = extras.getString(ARGS_INCLUDE);
        String name = extras.getString(ARGS_NAME);
        boolean ret = false;

        try {
            Call req = AgBaseApi.getApi().getSensorCategories(include, name);
            Response<ApiSensorCategories.GetManyResponse> res = req.execute();

            if(res.isSuccess()) {
                sensorCategories = res.body().sensorCategories;
                ret = true;
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public SensorCategory[] getRequestData() {
        return sensorCategories;
    }
}
