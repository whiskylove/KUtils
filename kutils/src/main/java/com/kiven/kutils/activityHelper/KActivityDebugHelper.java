package com.kiven.kutils.activityHelper;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.kiven.kutils.logHelper.KLog;
import com.kiven.kutils.logHelper.KShowLog;

/**
 * helper, 可显示日志
 * Created by kiven on 16/7/22.
 */
public class KActivityDebugHelper extends KActivityHelper implements SensorEventListener {

    protected SensorManager sensorManager;

    @Override
    public void onCreate(KHelperActivity activity, Bundle savedInstanceState) {
        super.onCreate(activity, savedInstanceState);

        sensorManager = (SensorManager) mActivity.getSystemService(Activity.SENSOR_SERVICE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (showLog()) {
            showLogTime = System.currentTimeMillis();
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (showLog())
            sensorManager.unregisterListener(this);
    }

    protected boolean showLog() {
        return KLog.isDebug();
    }

    private long showLogTime;

    /**
     * Called when sensor values have changed.
     * <p>See {@link SensorManager SensorManager}
     * for details on possible sensor types.
     * <p>See also {@link SensorEvent SensorEvent}.
     * <p/>
     * <p><b>NOTE:</b> The application doesn't own the
     * {@link SensorEvent event}
     * object passed as a parameter and therefore cannot hold on to it.
     * The object may be part of an internal pool and may be reused by
     * the framework.
     *
     * @param event the {@link SensorEvent SensorEvent}.
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (System.currentTimeMillis() - showLogTime < 2000) {
            return;
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            // assign directions
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

//            KLog.i("(" + x + ", " + y + ", " + z + ")");

            float xx = Math.abs(x);
            float yy = Math.abs(y);
            float zz = Math.abs(z - 9.8f);

            double sens = Math.sqrt(xx * xx + yy * yy + zz * zz);

//            KLog.i("( " + sens + " )");

            if (sens > 15) {
                new KShowLog().startActivity(mActivity);
                showLogTime = System.currentTimeMillis();
            }
        }
    }

    /**
     * Called when the accuracy of the registered sensor has changed.
     * <p/>
     * <p>See the SENSOR_STATUS_* constants in
     * {@link SensorManager SensorManager} for details.
     *
     * @param sensor
     * @param accuracy The new accuracy of this sensor, one of
     *                 {@code SensorManager.SENSOR_STATUS_*}
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
