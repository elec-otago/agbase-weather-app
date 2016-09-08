package nz.ac.elec.agbase.weather_app.agbase_sync.snyc_adapter_requests;

import android.os.Bundle;

import java.io.IOException;

import nz.ac.elec.agbase.android_agbase_api.AgBaseApi;
import nz.ac.elec.agbase.android_agbase_api.agbase_models.Sensor;
import nz.ac.elec.agbase.android_agbase_api.api_models.ApiSensors;
import retrofit.Call;
import retrofit.Response;

/**
 * Created by tm on 28/04/16.
 */
public class SensorRequest extends SyncAdapterRequest<Sensor[]>  {

    public static String ARGS_CATEGORY  = "SensorRequest.ARGS_CATEGORY";
    public static String ARGS_INCLUDE   = "SensorRequest.ARGS_INCLUDE";

    private Sensor[] sensors;

    @Override
    public boolean performRequest(Bundle extras) {
        boolean ret = false;
        Integer category = (Integer)extras.getSerializable(ARGS_CATEGORY);
        String include = extras.getString(ARGS_INCLUDE);

        try {
            Call req = AgBaseApi.getApi().getSensorsV2(category, include);
            Response<ApiSensors.GetManyResponse> res = req.execute();

            if(res.isSuccess()) {
                ret = true;
                sensors = res.body().sensors;
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public Sensor[] getRequestData() {
        return sensors;
    }
}
