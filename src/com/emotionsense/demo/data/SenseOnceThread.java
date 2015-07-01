package com.emotionsense.demo.data;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.ubhave.datahandler.loggertypes.AbstractDataLogger;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class SenseOnceThread extends Thread
{
	private final int sensorType;
	private final Activity resultScreen;
	private final ESSensorManager sensorManager;
	private final AbstractDataLogger logger;
	
	public SenseOnceThread(final Activity resultScreen, final ESSensorManager sensorManager, AbstractDataLogger logger, int sensorType)
	{
		this.resultScreen = resultScreen;
		this.sensorManager = sensorManager;
		this.logger = logger;
		this.sensorType = sensorType;
	}
	
	private void toast(final String message)
	{
		resultScreen.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				Toast.makeText(resultScreen, message, Toast.LENGTH_LONG).show();
			}
		});
	}
	
	@Override
	public void run()
	{
		try
		{
			SensorData data = sensorManager.getDataFromSensor(sensorType);
			if (data != null)
			{
				logger.logSensorData(data);
				toast("Finished sensing.");
				Log.d("Test", "Finished sensing: "+SensorUtils.getSensorName(sensorType));
			}
			else
			{
				toast("Finished sensing: null data");
				Log.d("Test", "Finished sensing: null data");
			}
			
		}
		catch (ESException e)
		{
			toast(e.getLocalizedMessage());
		}
	}
}
