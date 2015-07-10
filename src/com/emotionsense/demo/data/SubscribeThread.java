package com.emotionsense.demo.data;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.ubhave.datahandler.loggertypes.AbstractDataLogger;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class SubscribeThread extends Thread implements SensorDataListener
{
	private final int sensorType;
	private final Activity resultScreen;
	private final ESSensorManager sensorManager;
	private final AbstractDataLogger logger;

	private int subscriptionId;
	private boolean stop;

	public SubscribeThread(final Activity resultScreen, final ESSensorManager sensorManager, AbstractDataLogger logger, int sensorType)
	{
		this.sensorType = sensorType;
		this.resultScreen = resultScreen;
		this.sensorManager = sensorManager;
		this.logger = logger;
		this.stop = false;
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

	public void stopSensing()
	{
		try
		{
			stop = true;
			sensorManager.unsubscribeFromSensorData(subscriptionId);
		}
		catch (ESException e)
		{
			toast(e.getLocalizedMessage());
		}
	}

	@Override
	public void run()
	{
		try
		{
			subscriptionId = sensorManager.subscribeToSensorData(sensorType, this);
			while (!stop)
			{
				sleep(500);
			}
		}
		catch (Exception e)
		{
			toast(e.getLocalizedMessage());
		}
	}

	@Override
	public void onDataSensed(SensorData data)
	{
		try
		{
			Log.d("Test", "Received from: " + SensorUtils.getSensorName(sensorType));
			logger.logSensorData(data);
		}
		catch (Exception e)
		{
			toast(e.getLocalizedMessage());
		}
	}

	@Override
	public void onCrossingLowBatteryThreshold(boolean isBelowThreshold)
	{
		// TODO: what happens when there is low battery?
	}
}
