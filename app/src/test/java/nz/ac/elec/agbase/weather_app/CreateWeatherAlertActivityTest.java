package nz.ac.elec.agbase.weather_app;

import android.widget.Button;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;

import nz.ac.elec.agbase.android_agbase_api.agbase_models.Sensor;
import nz.ac.elec.agbase.android_agbase_api.agbase_models.SensorCategory;
import nz.ac.elec.agbase.android_agbase_api.agbase_models.SensorType;
import nz.ac.elec.agbase.android_agbase_db.AgBaseDatabase;
import nz.ac.elec.agbase.android_agbase_db.AgBaseDatabaseManager;
import nz.ac.elec.agbase.weather_app.activities.CreateWeatherAlertActivity;
import nz.ac.elec.agbase.weather_app.alert_db.AlertDatabaseManager;
import nz.ac.elec.agbase.weather_app.preferences.PreferenceHandler;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by tm on 9/05/16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class CreateWeatherAlertActivityTest {

    private CreateWeatherAlertActivity activity;

    private final String WEATHER_STATION_CAT = "Weather Station";
    private final String WEATHER_STATION_TYPE = "Weather Station type";
    private final String FIRST_WEATHER_STATION = "first";
    private final String SECOND_WEATHER_STATION = "second";

    @Before
    public void setup() {
        activity = Robolectric.buildActivity(CreateWeatherAlertActivity.class)
                .setup().get();

        PreferenceHandler.initialize(activity);
        AgBaseDatabaseManager.initialize(activity, "test_agbase_db");
        AlertDatabaseManager.initialize(activity, "test_alert_db");

        createTestData();
    }

    private void createTestData() {
        SensorCategory category = new SensorCategory();
        category.name = WEATHER_STATION_CAT;
        category.id = AgBaseDatabaseManager.getInstance().createSensorCategory(category);

        SensorType type = new SensorType();
        type.name = WEATHER_STATION_TYPE;
        type.sensorCategoryId = category.id;
        type.id = AgBaseDatabaseManager.getInstance().createSensorType(type);

        Sensor sensor1 = new Sensor();
        sensor1.name = FIRST_WEATHER_STATION;
        sensor1.guid = FIRST_WEATHER_STATION;
        sensor1.sensorTypeId = type.id;
        sensor1.id = AgBaseDatabaseManager.getInstance().createSensor(sensor1);

        Sensor sensor2 = new Sensor();
        sensor2.name = FIRST_WEATHER_STATION;
        sensor2.guid = FIRST_WEATHER_STATION;
        sensor2.sensorTypeId = type.id;
        sensor2.id = AgBaseDatabaseManager.getInstance().createSensor(sensor2);
    }

    @Test
    public void clickSave_shouldFail() {
        Button saveBtn = (Button)activity.findViewById(R.id.alert_form_submit_btn);
        saveBtn.performClick();

        assertThat(ShadowToast.getTextOfLatestToast(), equalTo(activity.FAIL_NO_CONDITION_MSG));
    }
}
