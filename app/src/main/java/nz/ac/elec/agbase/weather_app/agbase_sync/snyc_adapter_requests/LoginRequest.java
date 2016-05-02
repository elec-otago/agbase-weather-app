package nz.ac.elec.agbase.weather_app.agbase_sync.snyc_adapter_requests;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import nz.ac.elec.agbase.android_agbase_api.AgBaseApi;
import nz.ac.elec.agbase.android_agbase_api.api_models.ApiAuth;
import nz.ac.elec.agbase.android_agbase_login.AccountWorker;
import nz.ac.elec.agbase.weather_app.R;
import retrofit.Call;
import retrofit.Response;

/**
 * Created by tm on 28/04/16.
 */
public class LoginRequest extends SyncAdapterRequest<ApiAuth.LoginResponse> {

    public static final String ARGS_EMAIL = "LoginRequest.email";
    public static final String ARGS_PASSWORD = "LoginRequest.password";

    private ApiAuth.LoginResponse response;

    @Override
    public boolean performRequest(Bundle extras) {
        boolean ret = false;
        String email = extras.getString(ARGS_EMAIL);
        String password = extras.getString(ARGS_PASSWORD);

        try {
            ApiAuth.LoginRequest credentials = new ApiAuth().new LoginRequest(email, password);
            Call req = AgBaseApi.getApi().login(credentials);
            Response<ApiAuth.LoginResponse> res = req.execute();

            if(res.isSuccess()) {

                response = res.body();
                ret = true;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public ApiAuth.LoginResponse getRequestData() {
        return response;
    }
}
