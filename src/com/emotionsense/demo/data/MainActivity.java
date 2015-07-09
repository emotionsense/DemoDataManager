package com.emotionsense.demo.data;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.emotionsense.demo.data.loggers.AsyncUnencryptedDatabase;
import com.ubhave.datahandler.ESDataManager;
import com.ubhave.datahandler.except.DataHandlerException;
import com.ubhave.datahandler.loggertypes.AbstractDataLogger;
import com.ubhave.datahandler.transfer.DataUploadCallback;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class MainActivity extends Activity implements DataUploadCallback
{
	private final static String LOG_TAG = "MainActivity";

	private AbstractDataLogger logger;
	private ESSensorManager sensorManager;
	private SubscribeThread[] subscribeThreads;
	private SenseOnceThread[] pullThreads;

	private final int[] pushSensors = { SensorUtils.SENSOR_TYPE_PROXIMITY };
	private final int[] pullSensors = {}; // SensorUtils.SENSOR_TYPE_STEP_COUNTER

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		try
		{
			// TODO: change this line of code to change the type of data logger
			logger = AsyncUnencryptedDatabase.getInstance();
			sensorManager = ESSensorManager.getSensorManager(this);

			// Collect a single sample from the listed pull sensors
			pullThreads = new SenseOnceThread[pullSensors.length];
			for (int i = 0; i < pullSensors.length; i++)
			{
				pullThreads[i] = new SenseOnceThread(this, sensorManager, logger, pullSensors[i]);
				pullThreads[i].start();
			}
		}
		catch (Exception e)
		{
			Toast.makeText(this, "" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
			Log.d(LOG_TAG, e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void onResume()
	{
		super.onResume();
		// Collect a single sample from the listed push sensors
		subscribeThreads = new SubscribeThread[pushSensors.length];
		for (int i = 0; i < pushSensors.length; i++)
		{
			subscribeThreads[i] = new SubscribeThread(this, sensorManager, logger, pushSensors[i]);
			subscribeThreads[i].start();
		}
	}

	@Override
	public void onPause()
	{
		super.onPause();
		for (SubscribeThread thread : subscribeThreads)
		{
			thread.stopSensing();
		}
	}

	public void onSearchClicked(final View view)
	{
		try
		{
			long startTime = System.currentTimeMillis() - (1000L * 10);
			ESDataManager dataManager = logger.getDataManager();
			List<SensorData> recentData = dataManager.getRecentSensorData(SensorUtils.SENSOR_TYPE_PROXIMITY, startTime);
			Toast.makeText(this, "Recent events: " + recentData.size(), Toast.LENGTH_LONG).show();
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
			dataManager.postAllStoredData(this);
		}
		catch (DataHandlerException e)
		{
			Toast.makeText(this, "Exception: "+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
			Log.d(LOG_TAG, ""+e.getLocalizedMessage());
		}
		
	}

	@Override
	public void onDataUploaded()
	{
		Toast.makeText(this, "Data transferred.", Toast.LENGTH_LONG).show();
	}
	
	@Override
	public void onDataUploadFailed()
	{
		Toast.makeText(this, "Error transferring data", Toast.LENGTH_LONG).show();
	}
}
