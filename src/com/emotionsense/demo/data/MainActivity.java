package com.emotionsense.demo.data;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.emotionsense.demo.data.loggers.StoreOnlyEncryptedFiles;
import com.example.test.R;
import com.ubhave.datahandler.loggertypes.AbstractDataLogger;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class MainActivity extends Activity implements SensorDataListener
{
	private final static String LOG_TAG = "MainActivity";
	
	private ESSensorManager sensorManager;
	private AbstractDataLogger logger;
	private int subscriptionId;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try
		{
			logger = StoreOnlyEncryptedFiles.getInstance();
			sensorManager = ESSensorManager.getSensorManager(this);
		}
		catch (Exception e)
		{
			Log.d(LOG_TAG, e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		try
		{
			sensorManager.unsubscribeFromSensorData(subscriptionId);
			Log.d(LOG_TAG, "Unsubscribed from proximity sensor subscription: "+subscriptionId);
		}
		catch (ESException e)
		{
			Log.d(LOG_TAG, e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		try
		{
			subscriptionId = sensorManager.subscribeToSensorData(SensorUtils.SENSOR_TYPE_PROXIMITY, this);
			Log.d(LOG_TAG, "Subscribed to proximity sensor with id: "+subscriptionId);
		}
		catch (ESException e)
		{
			Log.d(LOG_TAG, e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void onDataSensed(SensorData data)
	{
		try
		{
			Log.d(LOG_TAG, "Received proximity data.");
			logger.logSensorData(data);
		}
		catch (Exception e)
		{
			Log.d(LOG_TAG, e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void onCrossingLowBatteryThreshold(boolean isBelowThreshold)
	{}
}
