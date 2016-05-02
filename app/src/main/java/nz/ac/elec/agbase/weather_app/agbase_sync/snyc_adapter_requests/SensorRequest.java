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

    public static String ARGS_FARM      = "SensorRequest.ARGS_FARM";
    public static String ARGS_GUID      = "SensorRequest.ARGS_GUID";
    public static String ARGS_SERIAL    = "SensorRequest.ARGS_SERIAL";
    public static String ARGS_TYPE      = "SensorRequest.ARGS_SENSOR_TYPE";
    public static String ARGS_INCLUDE   = "SensorRequest.ARGS_INCLUDE";
    public static String ARGS_LIMIT     = "SensorRequest.ARGS_LIMIT";
    public static String ARGS_OFFSET    = "SensorRequest.ARGS_OFFSET";

    private Sensor[] sensors;

    @Override
    public boolean performRequest(Bundle extras) {
        boolean ret = false;
        Integer farmId = (Integer)extras.getSerializable(ARGS_FARM);
        String guid = extras.getString(ARGS_GUID);
        String serial = extras.getString(ARGS_SERIAL);
        String type = extras.getString(ARGS_TYPE);
        String include = extras.getString(ARGS_INCLUDE);
        Integer limit = (Integer)extras.getSerializable(ARGS_LIMIT);
        Integer offset = (Integer)extras.getSerializable(ARGS_OFFSET);

        try {
            Call req = AgBaseApi.getApi().getSensors(farmId, guid, serial, type,
                    include, limit, offset);
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
