package com.emotionsense.demo.data;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.emotionsense.demo.data.loggers.StoreOnlyUnencryptedDatabase;
import com.ubhave.datahandler.ESDataManager;
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
			// TODO: change this line of code to change the type of data logger
			logger = StoreOnlyUnencryptedDatabase.getInstance();
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
	public void onDataSensed(final SensorData data)
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
	
	public void onSearchClicked(final View view)
	{
		try
		{
			long startTime = System.currentTimeMillis() - (1000L * 10);
			ESDataManager dataManager = logger.getDataManager();
			List<SensorData> recentData = dataManager.getRecentSensorData(SensorUtils.SENSOR_TYPE_PROXIMITY, startTime);
			Toast.makeText(this, "Recent events: "+recentData.size(), Toast.LENGTH_LONG).show();
		}
		catch (Exception e)
		{
			Toast.makeText(this, "Error retrieving sensor data", Toast.LENGTH_LONG).show();
			Log.d(LOG_TAG, e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	public void onFlushClicked(final View view)
	{
		try
		{
			ESDataManager dataManager = logger.getDataManager();
			dataManager.postAllStoredData();
			Toast.makeText(this, "Data transferred.", Toast.LENGTH_LONG).show();
		}
		catch (Exception e)
		{
			Toast.makeText(this, "Error transferring data", Toast.LENGTH_LONG).show();
			Log.d(LOG_TAG, e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void onCrossingLowBatteryThreshold(boolean isBelowThreshold)
	{}
}
