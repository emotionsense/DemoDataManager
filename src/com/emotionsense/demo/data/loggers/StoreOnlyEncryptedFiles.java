package com.emotionsense.demo.data.loggers;

import android.content.Context;
import android.util.Log;

import com.emotionsense.demo.data.DemoApplication;
import com.ubhave.datahandler.config.DataStorageConfig;
import com.ubhave.datahandler.except.DataHandlerException;
import com.ubhave.datahandler.loggertypes.AbstractDataLogger;
import com.ubhave.datahandler.loggertypes.AbstractStoreOnlyLogger;
import com.ubhave.sensormanager.ESException;

public class StoreOnlyEncryptedFiles extends AbstractStoreOnlyLogger
{
	private static StoreOnlyEncryptedFiles instance;
	
	public static AbstractDataLogger getInstance() throws ESException, DataHandlerException
	{
		if (instance == null)
		{
			instance = new StoreOnlyEncryptedFiles(DemoApplication.getContext());
		}
		return instance;
	}

	protected StoreOnlyEncryptedFiles(final Context context) throws DataHandlerException, ESException
	{
		super(context, DataStorageConfig.STORAGE_TYPE_FILES);
	}

	@Override
	protected String getFileStorageName()
	{
		return "Demo-Encrypted-File-Storage";
	}

	@Override
	protected String getUniqueUserId()
	{
		// Note: this should not be a static string
		return "test-user-id";
	}

	@Override
	protected String getDeviceId()
	{
		// Note: this should not be a static string
		return "test-device-id";
	}

	@Override
	protected boolean shouldPrintLogMessages()
	{
		// Note: return false to turn off Log.d messages
		return true;
	}

	@Override
	protected String getEncryptionPassword()
	{
		Log.d("MainActivity", "getEncryptionPassword()");
		// Note: return non-null password to encrypt data
		return "password";
	}
}
